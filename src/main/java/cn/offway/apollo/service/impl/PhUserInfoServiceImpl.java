package cn.offway.apollo.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhUserInfoService;

import cn.offway.apollo.domain.PhUserInfo;
import cn.offway.apollo.repository.PhUserInfoRepository;

import java.util.Date;


/**
 * 用户信息Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhUserInfoServiceImpl implements PhUserInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhUserInfoRepository phUserInfoRepository;
	
	@Override
	public PhUserInfo save(PhUserInfo phUserInfo){
		return phUserInfoRepository.save(phUserInfo);
	}
	
	@Override
	public PhUserInfo findOne(Long id){
		return phUserInfoRepository.findOne(id);
	}

	@Override
	public PhUserInfo findByUnionid(String unionid) {
		return phUserInfoRepository.findByUnionid(unionid);
	}

	@Override
	public PhUserInfo findByPhone(String phone){
		return phUserInfoRepository.findByPhone(phone);
	}

	@Override
	public PhUserInfo registered(String phone, String unionid, String nickName,
								 String headimgurl){
		PhUserInfo phUserInfo = new PhUserInfo();
		phUserInfo.setPhone(phone);
		if(StringUtils.isNotBlank(phone)){
			nickName = StringUtils.isBlank(nickName)?"OFFWAY_"+phone.substring(5):nickName;
		}
		phUserInfo.setNickname(nickName);
		phUserInfo.setHeadimgurl(headimgurl);
		phUserInfo.setUnionid(unionid);
		phUserInfo.setSex("1");
		phUserInfo = save(phUserInfo);
		return phUserInfo;
	}
}
