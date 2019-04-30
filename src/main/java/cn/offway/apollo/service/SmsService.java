package cn.offway.apollo.service;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsBatchSend;


@Service
public class SmsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private YunpianClient yunpianClient;
	
	@Value("${is-prd}")
	private boolean isPrd;
	
	
	/**
	 * 发送短信批量
	 * @param phone 多个,相隔
	 * @param message
	 * @param ip
	 * @return
	 */
	public boolean sendMsgBatch(String phone,String message){
		
		boolean result = false;
		if(isPrd){
			Map<String, String> param = yunpianClient.newParam(2);
			param.put(YunpianClient.MOBILE, phone);
			param.put(YunpianClient.TEXT, message);
			
			logger.info("短信批量发送请求:"+JSON.toJSONString(param));
			Result<SmsBatchSend> r = yunpianClient.sms().batch_send(param);
			logger.info("短信批量发送响应:"+JSON.toJSONString(r));
			if(0 == r.getCode()){
				result = true;
			}
		}else{
			result = true;
		}
		return result;
	}
}
