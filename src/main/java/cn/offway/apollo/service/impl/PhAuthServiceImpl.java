package cn.offway.apollo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.apollo.domain.PhAuth;
import cn.offway.apollo.domain.PhCode;
import cn.offway.apollo.domain.PhUserInfo;
import cn.offway.apollo.dto.AuthDto;
import cn.offway.apollo.properties.QiniuProperties;
import cn.offway.apollo.repository.PhAuthRepository;
import cn.offway.apollo.service.PhAuthService;
import cn.offway.apollo.service.PhCodeService;
import cn.offway.apollo.service.PhUserInfoService;
import cn.offway.apollo.utils.CommonResultCode;
import cn.offway.apollo.utils.JsonResult;
import cn.offway.apollo.utils.JsonResultHelper;


/**
 * 用户认证Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhAuthServiceImpl implements PhAuthService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhAuthRepository phAuthRepository;
	
	@Autowired
	private JsonResultHelper jsonResultHelper;
	
	@Autowired
	private PhUserInfoService phUserInfoService;
	
	@Autowired
	private PhCodeService phCodeService;
	
	@Autowired
	private QiniuProperties qiniuProperties;
	
	@Override
	public PhAuth save(PhAuth phAuth){
		return phAuthRepository.save(phAuth);
	}
	
	@Override
	public PhAuth findOne(Long id){
		return phAuthRepository.findOne(id);
	}
	
	@Override
	public PhAuth findByUnionid(String unionid){
		return phAuthRepository.findByUnionid(unionid);
	}
	
	@Override
	public int countByUnionidAndStatusIn(String unionid,List<String> status){
		return phAuthRepository.countByUnionidAndStatusIn(unionid, status);
	}
	
	@Override
	public JsonResult auth(AuthDto authDto){

		PhCode phCode = phCodeService.findByCodeAndStatusAndPhoneAndRealName(authDto.getCode(), "0", authDto.getPhone(), authDto.getRealName());
		if(null == phCode){
			//邀请码无效
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.CODE_ERROR);
		}
		
		
		PhAuth phAuth = new PhAuth();
		BeanUtils.copyProperties(authDto, phAuth);
		
		
		phAuth.setCodeId(phCode.getId());;
		phAuth.setIdcardObverse(qiniuProperties.getUrl()+"/"+phAuth.getIdcardObverse());
		phAuth.setIdcardPositive(qiniuProperties.getUrl()+"/"+phAuth.getIdcardPositive());
		phAuth.setInCert(qiniuProperties.getUrl()+"/"+phAuth.getInCert());
		
		String unionid = phAuth.getUnionid();
		List<String> status = new ArrayList<>();
		status.add("0");
		status.add("1");
		int count = countByUnionidAndStatusIn(unionid, status);
		if(count==0){
			PhUserInfo phUserInfo = phUserInfoService.findByUnionid(unionid);
			phAuth.setNickname(phUserInfo.getNickname());
			phAuth.setHeadimgurl(phUserInfo.getHeadimgurl());
			phAuth.setCreateTime(new Date());
			phAuth.setStatus("0");
			save(phAuth);
			
			phCode.setStatus("1");
			phCodeService.save(phCode);
		}
		
		return jsonResultHelper.buildSuccessJsonResult(null);
	}
	
}
