package cn.offway.apollo.controller;

import java.io.IOException;
import java.util.*;

import cn.offway.apollo.domain.*;
import cn.offway.apollo.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.offway.apollo.dto.MiniUserInfo;
import cn.offway.apollo.utils.AesCbcUtil;
import cn.offway.apollo.utils.CommonResultCode;
import cn.offway.apollo.utils.HttpClientUtil;
import cn.offway.apollo.utils.JsonResult;
import cn.offway.apollo.utils.JsonResultHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/mini")
public class MiniController {

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
	
	private static final String JSCODE2SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JsonResultHelper jsonResultHelper;
	
	@Autowired
	private PhUserInfoService phuserInfoService;

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
	
	@GetMapping(value = "/getwxacodeunlimit",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getwxacodeunlimit(String page,String scene,String width) throws IOException {
		JSONObject params = new JSONObject();
		params.put("page", page);
		params.put("scene", scene);
		params.put("width", width);
        byte[] result = HttpClientUtil.postByteArray("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+access_token,params.toJSONString());
        String resultStr = new String(result, "UTF-8");
        if(resultStr.indexOf("errcode")>=0){
			String accessTokenResult = HttpClientUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&secret="+SECRET);		
			logger.info(accessTokenResult);
			JSONObject jsonObject = JSON.parseObject(accessTokenResult);
			access_token = jsonObject.getString("access_token");
	        result = HttpClientUtil.postByteArray("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+access_token,params.toJSONString());
        }
        return result;
	}
	
	@ApiOperation("获取小程序用户SESSION")
	@PostMapping("/sendCode")
	public JsonResult sendCode(String code){
		String url = JSCODE2SESSION;
		url = url.replace("APPID", APPID).replace("SECRET", SECRET).replace("CODE", code);
		String result = HttpClientUtil.get(url);
		JSONObject jsonObject = JSON.parseObject(result);
		if(StringUtils.isNotBlank(jsonObject.getString("errcode"))){
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.PARAM_ERROR);
		}
		
		String session_key = jsonObject.getString("session_key");
		
		
		return jsonResultHelper.buildSuccessJsonResult(session_key);
	}
	
	@ApiOperation("解密小程序用户信息")
	@PostMapping("/sendDecode")
	public JsonResult sendDecode(@ApiParam("session_key") @RequestParam String sessionKey,@ApiParam("encryptedData") @RequestParam String encryptedData, @ApiParam("iv") String iv){
		
		try {
			String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
			logger.info("小程序登录用户信息:"+result);
			MiniUserInfo miniUserInfo = JSON.parseObject(result,MiniUserInfo.class);
			
			String unionid =  miniUserInfo.getUnionId();
			PhUserInfo phWxuserInfo = phuserInfoService.findByUnionid(unionid);
			
			if(null == phWxuserInfo){
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
			phWxuserInfo =  phuserInfoService.save(phWxuserInfo);
			
			return jsonResultHelper.buildSuccessJsonResult(phWxuserInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("解密小程序用户信息异常",e);
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
		}
		
	}
	@GetMapping(value = "/download",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] download(String url) throws IOException {
		return HttpClientUtil.getByteArray(url);
	}

	@ApiOperation("生成订单号")
	@GetMapping("/booksGetOrderNo")
	@Transactional
	public JsonResult booksGetOrderNo(@ApiParam("杂志ID") @RequestParam Long goodsId,@ApiParam("用户ID") @RequestParam Long userId,@ApiParam("购买数量") @RequestParam Long sum){
		PhUser user = userService.findOne(userId);
		if (null==user){
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.USER_NOT_EXISTS);
		}
		String no = orderInfoService.generateOrderNo("PH");
		PhTemplate template = templateService.findOne(goodsId);
		PhOrder order = new PhOrder();
		order.setUserId(userId);
		order.setUnionid(user.getUnionid());
		order.setTemplateId(goodsId);
		order.setStatus("0");
		order.setPrice(template.getPrice()*sum);
		order.setSum(String.valueOf(sum));
		order.setPhone(user.getPhone());
		order.setTemplateName(template.getTemplateName());
		order.setOrderNo(no);
		order.setCreateTime(new Date());
		order = orderService.save(order);
		Map<String,Object> map = new HashMap<>();
		map.put("orderId",order.getId());
		map.put("orderNo",order.getOrderNo());
		map.put("price",order.getPrice());
		map.put("status",order.getStatus());
		return jsonResultHelper.buildSuccessJsonResult(map);
	}

	@ApiOperation("获取电子刊小程序用户SESSION")
	@PostMapping("/bookssendCode")
	public JsonResult bookssendCode(String code){
		String url = JSCODE2SESSION;
		url = url.replace("APPID", BOOKSAPPID).replace("SECRET", BOOKSSECRET).replace("CODE", code);
		String result = HttpClientUtil.get(url);
//		JSONObject jsonObject = JSON.parseObject(result);
//		if(StringUtils.isNotBlank(jsonObject.getString("errcode"))){
//			return jsonResultHelper.buildFailJsonResult(CommonResultCode.PARAM_ERROR);
//		}
//
//		String session_key = jsonObject.getString("session_key");
		return jsonResultHelper.buildSuccessJsonResult(JSON.parseObject(result));
	}

	@ApiOperation("电子刊首页")
	@GetMapping("/booksIndex")
	public JsonResult booksIndex(){
		List<PhTemplate> phTemplates = new ArrayList<>();
		phTemplates = templateService.findAll();
		return jsonResultHelper.buildSuccessJsonResult(phTemplates);
	}

	@ApiOperation("电子刊排行榜详情")
	@GetMapping("/booksranking")
	public JsonResult booksranking(@ApiParam("电子刊id") @RequestParam Long id){
		List<Object> list = new ArrayList<>();
		Map<String,Object> remap = new HashMap<>();
		Map<String,Object> map = new HashMap<>();
		PhTemplate phTemplates = templateService.findOne(id);
		List<PhReadcode> readcodeList = readcodeService.findAllBybuyersid(id);
		map.put("imageurl",phTemplates.getImageUrl());
		map.put("subscribesum",phTemplates.getSubscribeSum());
		map.put("price",phTemplates.getPrice());
		map.put("templateName",phTemplates.getTemplateName());
		remap.put("title",map);
        for (PhReadcode phReadcode : readcodeList) {
        	Map<String,Object> map1 = new HashMap<>();
            PhUser phUser = userService.findOne(phReadcode.getBuyersId());
			map1.put("nickname",phUser.getNickname());
			map1.put("headimgurl",phUser.getHeadimgurl());
			map1.put("userid",phReadcode.getBuyersId());
			map1.put("sum",phReadcode.getRemark());
			list.add(map1);
        }
		remap.put("ranking",list);
		return jsonResultHelper.buildSuccessJsonResult(remap);
	}

	@ApiOperation("电子刊小程序注册/登录")
	@PostMapping("/booksregister")
	public JsonResult booksregister(
			@ApiParam("微信用户ID") @RequestParam String unionid,
			@ApiParam("微信用户昵称") @RequestParam String nickName,
			@ApiParam("微信用户头像") @RequestParam String headimgurl,
			@ApiParam("session_key") @RequestParam String sessionKey,
			@ApiParam("encryptedData,获取手机号得到") @RequestParam String encryptedData,
			@ApiParam("iv,获取手机号得到") @RequestParam String iv){

		try {
			//验证是用户
			PhUserInfo phUserInfo = null;
			if(StringUtils.isNotBlank(unionid)){
				phUserInfo = userInfoService.findByUnionid(unionid);
				if(null!=phUserInfo){
					return jsonResultHelper.buildSuccessJsonResult(phUserInfo);
				}
			}

			String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
			logger.info("解密小程序获取手机号信息:"+result);
			JSONObject jsonObject = JSON.parseObject(result);
			String purePhoneNumber = jsonObject.getString("purePhoneNumber");
			String countryCode = jsonObject.getString("countryCode");
			StringBuilder sb = new StringBuilder();
			sb.append("+").append(countryCode).append(purePhoneNumber);
			String phone = sb.toString();

			phUserInfo = userInfoService.findByPhone(phone);
			if(null!=phUserInfo){
				return jsonResultHelper.buildSuccessJsonResult(phUserInfo);
			}
			//unionid=UNIONID&nickName=NICKNAME&headimgurl=HEADIMGURL&sessionKey=SESSIONKEY&encryptedData=ENCRYPTEDDATA&iv=IV
			JSONObject params = new JSONObject();
			params.put("unionid", unionid);
			params.put("nickName", nickName);
			params.put("headimgurl", headimgurl);
			params.put("sessionKey", sessionKey);
			params.put("encryptedData", encryptedData);
			params.put("iv", iv);
			String url = APPREGISTERURL;
			HttpClientUtil.postByteArray(url,params.toJSONString());
			return jsonResultHelper.buildSuccessJsonResult(userInfoService.registered(phone,unionid,nickName,headimgurl));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("小程序注册异常",e);
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
		}
	}


}
