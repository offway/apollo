package cn.offway.apollo.controller;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.offway.apollo.domain.PhUserInfo;
import cn.offway.apollo.dto.MiniUserInfo;
import cn.offway.apollo.service.PhUserInfoService;
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
	
	private static final String JSCODE2SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JsonResultHelper jsonResultHelper;
	
	@Autowired
	private PhUserInfoService phuserInfoService;
	
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
	
	
	
}
