package cn.offway.apollo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhAuthService;

import cn.offway.apollo.domain.PhAuth;
import cn.offway.apollo.repository.PhAuthRepository;


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
	
	@Override
	public PhAuth save(PhAuth phAuth){
		return phAuthRepository.save(phAuth);
	}
	
	@Override
	public PhAuth findOne(Long id){
		return phAuthRepository.findOne(id);
	}
	
	@Override
	public int countByUnionidAndStatusIn(String unionid,List<String> status){
		return phAuthRepository.countByUnionidAndStatusIn(unionid, status);
	}
}
