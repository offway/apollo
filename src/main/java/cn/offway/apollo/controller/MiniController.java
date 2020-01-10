package cn.offway.apollo.controller;

import cn.offway.apollo.domain.*;
import cn.offway.apollo.dto.MiniUserInfo;
import cn.offway.apollo.service.*;
import cn.offway.apollo.utils.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/mini")
public class MiniController {
    private static final String JSCODE2SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";
    private static final String USER_TOKEN_KEY = "USER_TOKEN";
    private static final String USER_MAX_ID_KEY = "USER_MAX_ID";
    private static String access_token = "";
    @Value("${mini.appid}")
    private String APPID;
    @Value("${mini.secret}")
    private String SECRET;
    @Value("${mini.booksappid}")
    private String BOOKSAPPID;
    @Value("${mini.bookssecret}")
    private String BOOKSSECRET;
    @Value("${mini.appregister.url}")
    private String APPREGISTERURL;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JsonResultHelper jsonResultHelper;
    @Autowired
    private PhUserInfoService userInfoService;
    @Autowired
    private PhTemplateService templateService;
    @Autowired
    private PhReadcodeService readcodeService;
    @Autowired
    private PhUserService userService;
    @Autowired
    private PhOrderService orderService;
    @Autowired
    private PhOrderInfoService orderInfoService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private WxpayService wxpayService;

    @GetMapping(value = "/getwxacodeunlimit", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getwxacodeunlimit(String page, String scene, String width) throws IOException {
        JSONObject params = new JSONObject();
        params.put("page", page);
        params.put("scene", scene);
        params.put("width", width);
        byte[] result = HttpClientUtil.postByteArray("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + access_token, params.toJSONString());
        String resultStr = new String(result, StandardCharsets.UTF_8);
        if (resultStr.contains("errcode")) {
            String accessTokenResult = HttpClientUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret=" + SECRET);
            logger.info(accessTokenResult);
            JSONObject jsonObject = JSON.parseObject(accessTokenResult);
            access_token = jsonObject.getString("access_token");
            result = HttpClientUtil.postByteArray("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + access_token, params.toJSONString());
        }
        return result;
    }

    @ApiOperation("获取小程序用户SESSION")
    @PostMapping("/sendCode")
    public JsonResult sendCode(String code) {
        String url = JSCODE2SESSION;
        url = url.replace("APPID", APPID).replace("SECRET", SECRET).replace("CODE", code);
        String result = HttpClientUtil.get(url);
        JSONObject jsonObject = JSON.parseObject(result);
        if (StringUtils.isNotBlank(jsonObject.getString("errcode"))) {
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.PARAM_ERROR);
        }

        String session_key = jsonObject.getString("session_key");


        return jsonResultHelper.buildSuccessJsonResult(session_key);
    }

    @ApiOperation("解密小程序用户信息")
    @PostMapping("/sendDecode")
    public JsonResult sendDecode(@ApiParam("session_key") @RequestParam String sessionKey, @ApiParam("encryptedData") @RequestParam String encryptedData, @ApiParam("iv") String iv) {

        try {
            String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            logger.info("小程序登录用户信息:" + result);
            MiniUserInfo miniUserInfo = JSON.parseObject(result, MiniUserInfo.class);

            String unionid = miniUserInfo.getUnionId();
            PhUserInfo phWxuserInfo = userInfoService.findByUnionid(unionid);

            if (null == phWxuserInfo) {
                phWxuserInfo = new PhUserInfo();
            }
            phWxuserInfo.setCity(miniUserInfo.getCity());
            phWxuserInfo.setCountry(miniUserInfo.getCountry());
            phWxuserInfo.setCreateTime(new Date());
            phWxuserInfo.setHeadimgurl(miniUserInfo.getAvatarUrl());
            phWxuserInfo.setMiniopenid(miniUserInfo.getOpenId());
            phWxuserInfo.setNickname(miniUserInfo.getNickName());
            phWxuserInfo.setProvince(miniUserInfo.getProvince());
            phWxuserInfo.setSex(miniUserInfo.getGender());
            phWxuserInfo.setUnionid(unionid);
//			phWxuserInfo.setCreditScore(500L);
            phWxuserInfo = userInfoService.save(phWxuserInfo);

            return jsonResultHelper.buildSuccessJsonResult(phWxuserInfo);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("解密小程序用户信息异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }

    }

    @GetMapping(value = "/download", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] download(String url) throws IOException {
        return HttpClientUtil.getByteArray(url);
    }

    @ApiOperation("生成订单号")
    @GetMapping("/booksGetOrderNo")
    public JsonResult booksGetOrderNo(HttpServletRequest request,
                                      @ApiParam("杂志ID") @RequestParam Long goodsId,
                                      @ApiParam("用户ID") @RequestParam String unionid,
                                      @ApiParam("购买数量") @RequestParam Long sum,
                                      @ApiParam("支付方式[0-小程序，1-H5]") @RequestParam String type,
                                      @ApiParam("OPENID") @RequestParam String openid) {
        try {
            PhUser user = userService.findByUnionid(unionid);
            if (null == user) {
                return jsonResultHelper.buildFailJsonResult(CommonResultCode.USER_NOT_EXISTS);
            }
            String no = orderInfoService.generateOrderNo("PH");
            PhTemplate template = templateService.findOne(goodsId);
            PhOrder order = new PhOrder();
            order.setUserId(user.getId());
            order.setUnionid(user.getUnionid());
            order.setTemplateId(goodsId);
            order.setStatus("0");
            order.setPrice(template.getPrice() * sum);
            order.setSum(String.valueOf(sum));
            order.setPhone(user.getPhone());
            order.setTemplateName(template.getTemplateName());
            order.setOrderNo(no);
            order.setCreateTime(new Date());
            order = orderService.save(order);
            //微信统一下单
            String body = "电子刊购买,电子刊名称:" + template.getTemplateName();
            double amount = order.getPrice();
            if ("0".equals(type)) {
                return wxpayService.trade_JSAPI(no, IpUtil.getIpAddr(request), body, amount, openid);
            } else {
                return wxpayService.trade_JSAPI_IOS(no, IpUtil.getIpAddr(request), body, amount, openid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("生成订单号异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("获取电子刊小程序用户SESSION")
    @PostMapping("/bookssendCode")
    public JsonResult bookssendCode(String code) {
        try {
            String url = JSCODE2SESSION;
            url = url.replace("APPID", BOOKSAPPID).replace("SECRET", BOOKSSECRET).replace("CODE", code);
            String result = HttpClientUtil.get(url);
            return jsonResultHelper.buildSuccessJsonResult(JSON.parseObject(result));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取电子刊小程序用户SESSION异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("电子刊首页")
    @GetMapping("/booksIndex")
    public JsonResult booksIndex() {
        try {
            List<PhTemplate> phTemplateList = new ArrayList<>();
            List<PhTemplate> phTemplates = templateService.findAll();
            for (PhTemplate phTemplate : phTemplates) {
                String image = phTemplate.getImageUrl();
                String[] images = image.split(",");
                List<String> stringList = Arrays.asList(images);
                phTemplate.setImageUrls(stringList);
                phTemplateList.add(phTemplate);
            }
//        for (int i =0;i<phTemplates.size();i++){
//            PhTemplate phTemplate = phTemplates.get(i);
//            phTemplate.setPrice(phTemplate.getPrice()*100);
//            phTemplates.set(i,phTemplate);
//        }
            return jsonResultHelper.buildSuccessJsonResult(phTemplateList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("电子刊首页异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("电子刊获得用户token")
    @GetMapping("/booksUserToken")
    public JsonResult booksUserToken(@ApiParam("unionid") @RequestParam String unionid) {
        try {
            PhUser user = userService.findByUnionid(unionid);
            if (user == null) {
                return jsonResultHelper.buildFailJsonResult(CommonResultCode.USER_NOT_EXISTS);
            } else {
                String userToken = stringRedisTemplate.opsForValue().get(USER_TOKEN_KEY + "_" + unionid);
                Map<String, Object> map = new HashMap<>();
                if (StringUtils.isBlank(userToken)) {
                    String token = UUID.randomUUID().toString().replaceAll("-", "");
                    stringRedisTemplate.opsForValue().set(USER_TOKEN_KEY + "_" + unionid, token, 1, TimeUnit.DAYS);
                    map.put("token", token);
                } else {
                    map.put("token", userToken);
                }
                return jsonResultHelper.buildSuccessJsonResult(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("电子刊获得用户token异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("电子刊查询阅读码")
    @GetMapping("/booksGetCode")
    public JsonResult booksGetCode(
            @ApiParam("unionid") @RequestParam String unionid,
            @ApiParam("杂志ID") @RequestParam Long id,
            @ApiParam("状态[0-所有，1-未使用，2-已使用]") @RequestParam String state,
            @ApiParam("页码,从0开始") @RequestParam int page,
            @ApiParam("页大小") @RequestParam int size) {
        try {
            PhUser user = userService.findByUnionid(unionid);
            Page<PhReadcode> readcodeList = readcodeService.findByBuyersIdAndBooksId(state, user.getId(), id, new PageRequest(page, size));
            if (null != readcodeList) {
                List<Object> list = new ArrayList<>();
                for (PhReadcode readcode : readcodeList) {
                    Map<String, Object> newmap = new HashMap<>();
                    newmap.put("booksId", readcode.getBooksId());
                    newmap.put("code", readcode.getCode());
                    newmap.put("state", readcode.getState());
                    list.add(newmap);
                }
                return jsonResultHelper.buildSuccessJsonResult(list);
            } else {
                return jsonResultHelper.buildFailJsonResult(CommonResultCode.USER_CODE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("电子刊查询阅读码异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("电子刊已购买杂志个人信息")
    @GetMapping("/booksUserInfo")
    public JsonResult booksUserInfo(@ApiParam("unionid") @RequestParam String unionid) {
        try {
            List<Object> list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            PhUser user = userService.findByUnionid(unionid);
            map.put("userInfo", user);
            List<PhReadcode> readcode = readcodeService.findByUseridCode(user.getId());
            for (PhReadcode phReadcode : readcode) {
                PhTemplate template = templateService.findOne(phReadcode.getBooksId());
                Map<String, Object> map1 = new HashMap<>();
                PhReadcode phReadcode1 = readcodeService.findByUseIdAndBooksIdAndState(user.getId(), template.getId(), "1");
                if (phReadcode1 != null) {
                    template.setStatus("1");
                } else {
                    template.setStatus("0");
                }
                list.add(template);
            }

            map.put("magazine", list);
            return jsonResultHelper.buildSuccessJsonResult(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("电子刊已购买杂志个人信息异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("电子刊小程序修改用户信息")
    @PostMapping("/booksUpdateUser")
    public JsonResult booksUpdateUser(
            HttpServletRequest request,
            @ApiParam("unionid") @RequestParam String unionid,
            @ApiParam("用户昵称") @RequestParam String nickname,
            @ApiParam("用户的性别，值为1时是男性，值为2时是女性，值为0时是未知") @RequestParam String sex,
            @ApiParam("生日") @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date birthday,
            @ApiParam("用户头像") @RequestParam String headimgurl) {
        try {
            PhUser user = userService.findByUnionid(unionid);
            user.setNickname(nickname);
            user.setSex(sex);
            user.setBirthday(birthday);
            user.setHeadimgurl(headimgurl);
            userService.save(user);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("修改用户信息异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("阅读码兑换")
    @GetMapping("/booksexchange")
    public JsonResult booksexchange(@ApiParam("电子刊id") @RequestParam Long id, @ApiParam("使用者unionid") @RequestParam String unionid, @ApiParam("code") @RequestParam String code) {
        try {
            PhUser user = userService.findByUnionid(unionid);
            List<PhReadcode> readcodes = readcodeService.findByBooksIdAndStateAndUseId(id, "1", user.getId());
            if (readcodes.size() > 0) {
                return jsonResultHelper.buildFailJsonResult(CommonResultCode.ACTIVITY_PARTICIPATED);
            }
            PhReadcode readcode = readcodeService.findByBooksIdAndStateAndUseIdAndCode(id, "0", user.getId(), code);
            if (readcode != null) {
                readcode.setUseId(user.getId());
                readcode.setState("1");
                readcode.setUseTime(new Date());
                readcodeService.save(readcode);
                return jsonResultHelper.buildFailJsonResult(CommonResultCode.SUCCESS);
            } else {
                return jsonResultHelper.buildFailJsonResult(CommonResultCode.CODE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("阅读码兑换异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("购买后第一次自动使用阅读码")
    @GetMapping("/booksInsetCode")
    public JsonResult booksInsetCode(@ApiParam("电子刊id") @RequestParam Long id, @ApiParam("购买者unionid") @RequestParam String unionid) {
        try {
            PhUser user = userService.findByUnionid(unionid);
            List<PhReadcode> readcodes = readcodeService.findByBooksIdAndStateAndUseId(id, "1", user.getId());
            if (readcodes.size() > 0) {
                return jsonResultHelper.buildFailJsonResult(CommonResultCode.ACTIVITY_PARTICIPATED);
            }
            List<PhReadcode> readcodeList = readcodeService.findByBooksIdAndStateAndUseId(id, "0", user.getId());
            if (readcodeList.size() > 0) {
                PhReadcode readcode = readcodeList.get(0);
                readcode.setState("1");
                readcode.setUseId(user.getId());
                readcode.setUseTime(new Date());
                readcodeService.save(readcode);
                return jsonResultHelper.buildFailJsonResult(CommonResultCode.SUCCESS);
            } else {
                return jsonResultHelper.buildFailJsonResult(CommonResultCode.PARAM_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("购买后第一次自动使用阅读码异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("电子刊排行榜详情")
    @GetMapping("/bookranking")
    public JsonResult booksranking(@ApiParam("电子刊id") @RequestParam Long id, @ApiParam("unionid") @RequestParam String unionid) {
        try {
            List<Object> list = new ArrayList<>();
            Map<String, Object> remap = new HashMap<>();
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map0 = new HashMap<>();
            PhTemplate phTemplates = templateService.findOne(id);
            map0.put("1", phTemplates.getWelfare1());
            map0.put("2", phTemplates.getWelfare2());
            map0.put("3", phTemplates.getWelfare3());
            if (StringUtils.isNotBlank(unionid)) {
                PhUser user = userService.findByUnionid(unionid);
                PhReadcode readcode = readcodeService.findByUseIdAndBooksIdAndState(user.getId(), id, "1");
                if (readcode != null) {
                    map.put("state", "1");
                } else {
                    map.put("state", "0");
                }
            } else {
                map.put("state", "0");
            }
            List<PhReadcode> readcodeList = readcodeService.findAllBybuyersid(id);
            String image = phTemplates.getImageUrl();
            String[] images = image.split(",");
            List<String> stringList = Arrays.asList(images);
            map.put("coverImageUrl",phTemplates.getCoverImageUrl());
            map.put("imageurls", stringList);
            map.put("subscribesum", phTemplates.getSubscribeSum());
            map.put("price", phTemplates.getPrice());
            map.put("templateName", phTemplates.getTemplateName());
            map.put("nameColor", phTemplates.getNameColor());
            map.put("welfare", map0);
            remap.put("title", map);
            for (PhReadcode phReadcode : readcodeList) {
                Map<String, Object> map1 = new HashMap<>();
                PhUser phUser = userService.findOne(phReadcode.getBuyersId());
                map1.put("nickname", phUser.getNickname());
                map1.put("headimgurl", phUser.getHeadimgurl());
                map1.put("userid", phReadcode.getBuyersId());
                map1.put("sum", phReadcode.getRemark());
                list.add(map1);
            }
            remap.put("ranking", list);
            return jsonResultHelper.buildSuccessJsonResult(remap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("电子刊排行榜详情异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("电子刊排行榜详情(包含虚拟用户)")
    @GetMapping("/booksRankingAll")
    public JsonResult booksRankingAll(@ApiParam("电子刊id") @RequestParam Long id, @ApiParam("unionid") @RequestParam String unionId) {
        try {
            Map<String, Object> remap = new HashMap<>();
            Map<String, Object> map = new HashMap<>();
            PhTemplate phTemplates = templateService.findOne(id);
            map.put("imageurl", phTemplates.getImageUrl());
            map.put("subscribesum", phTemplates.getSubscribeSum());
            map.put("price", phTemplates.getPrice());
            map.put("templateName", phTemplates.getTemplateName());
            if (StringUtils.isNotBlank(unionId)) {
                PhUser user = userService.findByUnionid(unionId);
                PhReadcode readcode = readcodeService.findByUseIdAndBooksIdAndState(user.getId(), id, "1");
                map.put("state", readcode != null ? "1" : "0");
            } else {
                map.put("state", "0");
            }
            remap.put("title", map);
            List<Object> list = new ArrayList<>();
            String key = MessageFormat.format("{0}_{1}", CallbackController.KEY_RANK, id);
            for (ZSetOperations.TypedTuple obj : stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 100)) {
                Map<String, Object> map1 = new HashMap<>();
                PhUser phUser = userService.findOne(Long.valueOf(String.valueOf(obj.getValue())));
                map1.put("nickname", phUser.getNickname());
                map1.put("headimgurl", phUser.getHeadimgurl());
                map1.put("userid", phUser.getId());
                map1.put("sum", obj.getScore());
                list.add(map1);
            }
            remap.put("ranking", list);
            return jsonResultHelper.buildSuccessJsonResult(remap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("电子刊排行榜详情异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

    private void AppRegister(String unionid, String nickName, String headimgurl, String sessionKey, String encryptedData, String iv) {
        Map<String, String> params = new HashMap<>();
        params.put("unionid", unionid);
        params.put("nickName", nickName);
        params.put("headimgurl", headimgurl);
        params.put("sessionKey", sessionKey);
        params.put("encryptedData", encryptedData);
        params.put("iv", iv);
        params.put("source", "0");
        String url = APPREGISTERURL;
        HttpClientUtil.post(url, params);
    }

    @ApiOperation("电子刊小程序注册/登录")
    @PostMapping("/booksregister")
    public JsonResult booksregister(
            @ApiParam("微信用户ID") @RequestParam String unionid,
            @ApiParam("微信用户昵称") @RequestParam String nickName,
            @ApiParam("微信用户头像") @RequestParam String headimgurl,
            @ApiParam("session_key") @RequestParam String sessionKey,
            @ApiParam("encryptedData,获取手机号得到") @RequestParam String encryptedData,
            @ApiParam("iv,获取手机号得到") @RequestParam String iv) {

        try {
            String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            logger.info("解密小程序获取手机号信息:" + result);
            JSONObject jsonObject = JSON.parseObject(result);
            String purePhoneNumber = jsonObject.getString("purePhoneNumber");
            String countryCode = jsonObject.getString("countryCode");
            StringBuilder sb = new StringBuilder();
            sb.append("+").append(countryCode).append(purePhoneNumber);
            String phone = sb.toString();
            //验证是用户
            PhUser phUserInfo = null;
            if (StringUtils.isNotBlank(unionid)) {
                phUserInfo = userService.findByUnionid(unionid);
                if (null != phUserInfo) {
                    if (null != phUserInfo.getPhone()) {
                        return jsonResultHelper.buildSuccessJsonResult(phUserInfo);
                    } else {
                        phUserInfo.setPhone(phone);
                        phUserInfo = userService.save(phUserInfo);
                        AppRegister(unionid, nickName, headimgurl, sessionKey, encryptedData, iv);
                        return jsonResultHelper.buildSuccessJsonResult(phUserInfo);
                    }
                }
            }

            phUserInfo = userService.findByPhone(phone);
            if (null != phUserInfo) {
                return jsonResultHelper.buildSuccessJsonResult(phUserInfo);
            }
            //unionid=UNIONID&nickName=NICKNAME&headimgurl=HEADIMGURL&sessionKey=SESSIONKEY&encryptedData=ENCRYPTEDDATA&iv=IV
            //Map<String,String> params = new HashMap<>();
            //params.put("unionid", unionid);
            //params.put("nickName", nickName);
            //params.put("headimgurl", headimgurl);
            //params.put("sessionKey", sessionKey);
            //params.put("encryptedData", encryptedData);
            //params.put("iv", iv);
            //params.put("source","0");
            //String url = APPREGISTERURL;
            //HttpClientUtil.post(url, params);
            AppRegister(unionid, nickName, headimgurl, sessionKey, encryptedData, iv);
            if (!stringRedisTemplate.hasKey(USER_MAX_ID_KEY)) {
                int maxId = userService.getMaxUserId();
                stringRedisTemplate.opsForValue().setIfAbsent(USER_MAX_ID_KEY, String.valueOf(maxId));
            }
            long nextUserId = stringRedisTemplate.opsForValue().increment(USER_MAX_ID_KEY, 1);
            return jsonResultHelper.buildSuccessJsonResult(userService.registered(phone, unionid, nickName, headimgurl, nextUserId));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("小程序注册异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("电子刊H5注册/登录")
    @PostMapping("/booksregisterH5")
    public JsonResult booksregisterH5(
            @ApiParam("微信用户ID") @RequestParam String unionid,
            @ApiParam("微信用户昵称") @RequestParam String nickName,
            @ApiParam("微信用户头像") @RequestParam String headimgurl) {

        try {
            //验证是用户
            PhUser phUserInfo = null;
            if (StringUtils.isNotBlank(unionid)) {
                phUserInfo = userService.findByUnionid(unionid);
                if (null != phUserInfo) {
                    return jsonResultHelper.buildSuccessJsonResult(phUserInfo);
                }
            }
            if (!stringRedisTemplate.hasKey(USER_MAX_ID_KEY)) {
                int maxId = userService.getMaxUserId();
                stringRedisTemplate.opsForValue().setIfAbsent(USER_MAX_ID_KEY, String.valueOf(maxId));
            }
            long nextUserId = stringRedisTemplate.opsForValue().increment(USER_MAX_ID_KEY, 1);
            return jsonResultHelper.buildSuccessJsonResult(userService.registered(unionid, nickName, headimgurl, nextUserId));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("小程序注册异常", e);
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
        }
    }

}
