package cn.offway.apollo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhUserService;

import cn.offway.apollo.domain.PhUser;
import cn.offway.apollo.repository.PhUserRepository;


/**
 * 用户信息Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-30 14:38:27 Exp $
 */
@Service
public class PhUserServiceImpl implements PhUserService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhUserRepository phUserRepository;
	
	@Override
	public PhUser save(PhUser phUser){
		return phUserRepository.save(phUser);
	}
	
	@Override
	public PhUser findOne(Long id){
		return phUserRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phUserRepository.delete(id);
	}

	@Override
	public List<PhUser> save(List<PhUser> entities){
		return phUserRepository.save(entities);
	}
}
