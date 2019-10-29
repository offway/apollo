package cn.offway.apollo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhReadcodeService;

import cn.offway.apollo.domain.PhReadcode;
import cn.offway.apollo.repository.PhReadcodeRepository;


/**
 * 阅读码购买使用表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-29 15:52:21 Exp $
 */
@Service
public class PhReadcodeServiceImpl implements PhReadcodeService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhReadcodeRepository phReadcodeRepository;
	
	@Override
	public PhReadcode save(PhReadcode phReadcode){
		return phReadcodeRepository.save(phReadcode);
	}
	
	@Override
	public PhReadcode findOne(Long id){
		return phReadcodeRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phReadcodeRepository.delete(id);
	}

	@Override
	public List<PhReadcode> save(List<PhReadcode> entities){
		return phReadcodeRepository.save(entities);
	}
}
