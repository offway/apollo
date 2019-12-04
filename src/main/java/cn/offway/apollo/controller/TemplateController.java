package cn.offway.apollo.controller;

import cn.offway.apollo.domain.*;
import cn.offway.apollo.service.*;
import cn.offway.apollo.utils.CommonResultCode;
import cn.offway.apollo.utils.JsonResult;
import cn.offway.apollo.utils.JsonResultHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/template")
public class TemplateController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String USER_TOKEN_KEY = "USER_TOKEN";

    @Autowired
    private JsonResultHelper jsonResultHelper;

    @Autowired
    private PhTemplateService templateService;

    @Autowired
    private PhTemplateConfigService templateConfigService;

    @Autowired
    private PhLockService lockService;

    @Autowired
    private PhTemplate1Service template1Service;

    @Autowired
    private PhTemplate2Service template2Service;

    @Autowired
    private PhTemplate3Service template3Service;

    @Autowired
    private PhTemplate4Service template4Service;

    @Autowired
    private PhTemplate5Service template5Service;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PhUserService userService;

    @Autowired
    private PhReadcodeService readcodeService;


    private Map<String, Object> transform(Object obj, PhTemplate template) {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String,Object> map = mapper.convertValue(obj,HashMap.class);
        Map<String, Object> newObj = new HashMap<>();
        if (obj instanceof PhTemplate1) {
            PhTemplate1 template1 = (PhTemplate1) obj;
            //样式：1样式一、2样式二、3样式三、4样式四、5样式五、6样式六
            newObj.put("type", "1");
            //杂志id
            newObj.put("magazine_id", template1.getGoodsId());
            //电子刊音频
            newObj.put("bgm_url", template.getAudioUrl());
            /* 类型[0-重叠,1-不重叠] **/
            newObj.put("cover_style", template1.getType());
            //底图
            newObj.put("tail_pic", template1.getImageUnderUrl());
            //文字图
            newObj.put("text_pic", template1.getImageTextUrl());
            //文字底图
            newObj.put("text_pic_bg", template1.getImageUnderTextUrl());
            /* 滚动区域类型[0-图片,1-文字] **/
            newObj.put("rolling_type", template1.getRollingType());
            /* 浮窗位置[0-左下,1-左上,2-正局中,3-右上,4-右下] **/
            newObj.put("rolling_position", template1.getRollingPosition());
            /* 滚动区域图片, 滚动区域类型为0时必填 **/
            newObj.put("rolling_pic", template1.getRollingImageUrl());
            /* 滚动区域文字, 滚动区域类型为1时必填 **/
            newObj.put("rolling_text", template1.getRollingText());
        } else if (obj instanceof List) {
            //样式：1样式一、2样式二、3样式三、4样式四、5样式五、6样式六
            newObj.put("type", "2");
            List<PhTemplate2> template2s = (List<PhTemplate2>) obj;
            List<Map<String, Object>> arr = new ArrayList<>();
            for (PhTemplate2 template2 : template2s) {
                //杂志id
                newObj.put("magazine_id", template2.getGoodsId());
                //电子刊音频
                newObj.put("bgm_url", template.getAudioUrl());
                Map<String, Object> innerMap = new HashMap<>();
                /* 展示类型[0-效果图,1-文字] **/
                innerMap.put("show_style", template2.getType());
                /* 操作方式[0-左滑,1-自动] **/
                innerMap.put("action_mode", template2.getWay());
                //图片链接
                innerMap.put("pic", template2.getImageUrl());
                //底图链接
                innerMap.put("bg_pic", template2.getImageUnderUrl());
                //提示文案
                innerMap.put("text", template2.getPromptText());
                //提示文案字体
                if (StringUtils.isNotBlank(template2.getRemark())) {
                    JSONObject jsonObject = JSONObject.parseObject(template2.getRemark());
                    innerMap.put("text_style", jsonObject.toJavaObject(Map.class));
                }
                /* 位置[0-文字左上滑出,1-文字右上滑出,2-文字左下滑出,3-文字右下滑出,4-文字居中显示,5-全屏居中显示] **/
                innerMap.put("location", template2.getLocation());
                arr.add(innerMap);
            }
            newObj.put("items", arr);
        } else if (obj instanceof PhTemplate3) {
            PhTemplate3 template3 = (PhTemplate3) obj;
            //样式：1样式一、2样式二、3样式三、4样式四、5样式五、6样式六
            newObj.put("type", "3");
            //杂志id
            newObj.put("magazine_id", template3.getGoodsId());
            //电子刊音频
            newObj.put("bgm_url", template.getAudioUrl());
            //标题
            newObj.put("title", template3.getImageTag());
            //背景图
            newObj.put("background", template3.getImageUnder());
            List<Map<String, Object>> arr = new ArrayList<>();
            JSONArray jsonArray = JSONArray.parseArray(template3.getContent());
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                Map<String, Object> innerMap = new HashMap<>();
                //角色名
                innerMap.put("role_name", jsonObject.getString("role"));
                //角色头像
                innerMap.put("role_icon", jsonObject.getString("icon"));
                //内容类型[0-音频,1-文字,2-表情]
                innerMap.put("content_type", jsonObject.getString("contentType"));
                /* 操作方式[0-左滑,1-自动] **/
                innerMap.put("action_mode", jsonObject.getString("way"));
                //音频url
                innerMap.put("audio_url", jsonObject.getString("audioUrl"));
                //音频参数
                innerMap.put("audio_url_cfg", jsonObject.getString("audioUrlCfg"));
                //文本内容
                innerMap.put("content_txt", jsonObject.getString("contentTxt"));
                //表情URL
                innerMap.put("emotion", jsonObject.getString("emotion"));
                //表情参数[0-240*240,1-480*480]
                innerMap.put("emotion_cfg", jsonObject.getString("emotionCfg"));
                arr.add(innerMap);
            }
            newObj.put("items", arr);
        } else if (obj instanceof PhTemplate4) {
            PhTemplate4 template4 = (PhTemplate4) obj;
            //样式：1样式一、2样式二、3样式三、4样式四、5样式五、6样式六
            newObj.put("type", "5");
            //杂志id
            newObj.put("magazine_id", template4.getGoodsId());
            //电子刊音频
            newObj.put("bgm_url", template.getAudioUrl());
            /* 类型[0-类型1(两张图片),1-类型2(三张图片),2-类型3(三张图片),3-类型4(三张图片),4-类型5(两张图片),5-类型6(两张图片),6-类型7(两张图片),7-类型8(两张图片)] **/
            newObj.put("layout_style", template4.getType());
            List<String> arr = new ArrayList<>();
            if (StringUtils.isNotBlank(template4.getImageOneUrl())) {
                arr.add(template4.getImageOneUrl());
            }
            if (StringUtils.isNotBlank(template4.getImageTwoUrl())) {
                arr.add(template4.getImageTwoUrl());
            }
            if (StringUtils.isNotBlank(template4.getImageThreeUrl())) {
                arr.add(template4.getImageThreeUrl());
            }
            newObj.put("pics", arr);
//            //图片1
//            newObj.put("pic_1",template4.getImageOneUrl());
//            //图片2
//            newObj.put("pic_2",template4.getImageTwoUrl());
//            //图片3
//            newObj.put("pic_3",template4.getImageThreeUrl());
        } else if (obj instanceof PhTemplate5) {
            PhTemplate5 template5 = (PhTemplate5) obj;
            //样式：1样式一、2样式二、3样式三、4样式四、5样式五、6样式六
            newObj.put("type", "4");
            //杂志id
            newObj.put("magazine_id", template5.getGoodsId());
            //电子刊音频
            newObj.put("bgm_url", template.getAudioUrl());
            /* 类型[0-底图,1-无边框] **/
            newObj.put("video_style", template5.getType());
            //底图
            newObj.put("base_pic", template5.getImageUnderUrl());
            //视频
            newObj.put("video_url", template5.getVideoUrl());
            //视频封面
            newObj.put("video_cover", template5.getVideoCoverUrl());
            //提示文字
            newObj.put("text", template5.getPromptText());
        }
        return newObj;
    }

    private Map<String, Object> processLock(PhLock lock) {
        Map<String, Object> lockObj = new HashMap<>();
        /* 是否解锁[0-否,1-是] **/
        lockObj.put("isLock", lock.getUnlock());
        //解锁订阅数
        lockObj.put("subscribeCount", lock.getSubscribeCount());
        //提示文字
        lockObj.put("promptText", lock.getPromptText());
        if ("4".equals(lock.getTemplateType())){
            if ("1".equals(lock.getContentType())){
                //提示图片
                lockObj.put("promptImage", lock.getPromptImage());
            }else {
                //字体颜色大小
                if (StringUtils.isNotBlank(lock.getAttribute())) {
                    JSONObject jsonObject = JSONObject.parseObject(lock.getAttribute());
                    lockObj.put("attribute", jsonObject.toJavaObject(Map.class));
                }
                //内容类型[0-文字,1-图片]
                lockObj.put("contentType", lock.getContentType());
            }
        }
        return lockObj;
    }


    @ApiOperation("杂志列表")
    @GetMapping("/list")
    public JsonResult list(@ApiParam("杂志id") @RequestParam Long id, @ApiParam("unionid") @RequestParam String unionid, @ApiParam("token") @RequestParam String token) {
        String userToken = stringRedisTemplate.opsForValue().get(USER_TOKEN_KEY + "_" + unionid);
        if ("".equals(userToken) || userToken == null) {
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.USER_NOT_EXISTS);
        } else if (!userToken.equals(token)) {
            return jsonResultHelper.buildFailJsonResult(CommonResultCode.USER_NOT_EXISTS);
        } else {
            PhUser user = userService.findByUnionid(unionid);
            PhReadcode readcode = readcodeService.findByUseIdAndBooksIdAndState(user.getId(), id, "1");
            if (null == readcode) {
                return jsonResultHelper.buildFailJsonResult(CommonResultCode.USER_PERMISSIONS_ERROR);
            } else {
                PhTemplate template = templateService.findOne(id);
                if (null != template) {
                    List<PhTemplateConfig> templateConfigs = templateConfigService.findByGoodsId(template.getId());
                    List<Map<String, Object>> list = new ArrayList<>();
                    template.setReadingNumber(template.getReadingNumber() + 1);
                    templateService.save(template);
                    for (PhTemplateConfig templateConfig : templateConfigs) {
                        PhLock lock;
                        Map<String, Object> obj;
                        switch (templateConfig.getName()) {
                            case "template1":
                                PhTemplate1 template1 = template1Service.findOne(templateConfig.getTemplateId());
                                obj = transform(template1, template);
                                lock = lockService.findByGoodsidAndTemplateTypeAndTemplateId(template.getId(), "0", template1.getId());
                                obj.put("lock", processLock(lock));
                                list.add(obj);
                                break;
                            case "template2":
                                List<PhTemplate2> template2s = template2Service.findList(templateConfig.getId());
                                obj = transform(template2s, template);
                                lock = lockService.findByPid(templateConfig.getId());
                                obj.put("lock", processLock(lock));
                                list.add(obj);
                                break;
                            case "template3":
                                PhTemplate3 template3 = template3Service.findOne(templateConfig.getTemplateId());
                                obj = transform(template3, template);
                                lock = lockService.findByPid(templateConfig.getId());
                                obj.put("lock", processLock(lock));
                                list.add(obj);
                                break;
                            case "template4":
                                PhTemplate4 template4 = template4Service.findOne(templateConfig.getTemplateId());
                                obj = transform(template4, template);
                                lock = lockService.findByGoodsidAndTemplateTypeAndTemplateId(template.getId(), "3", template4.getId());
                                obj.put("lock", processLock(lock));
                                list.add(obj);
                                break;
                            case "template5":
                                PhTemplate5 template5 = template5Service.findOne(templateConfig.getTemplateId());
                                obj = transform(template5, template);
                                lock = lockService.findByGoodsidAndTemplateTypeAndTemplateId(template.getId(), "4", template5.getId());
                                obj.put("lock", processLock(lock));
                                list.add(obj);
                                break;
                            default:
                                break;
                        }
                    }
                    return jsonResultHelper.buildSuccessJsonResult(list);
                } else {
                    return jsonResultHelper.buildFailJsonResult(CommonResultCode.MAGAZINES_DONOT_EXIST);
                }
            }
        }
    }

}
