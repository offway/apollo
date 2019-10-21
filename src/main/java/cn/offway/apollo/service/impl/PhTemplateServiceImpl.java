package cn.offway.apollo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhTemplateService;

import cn.offway.apollo.domain.PhTemplate;
import cn.offway.apollo.repository.PhTemplateRepository;


/**
 * 杂志管理Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 12:01:34 Exp $
 */
@Service
public class PhTemplateServiceImpl implements PhTemplateService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhTemplateRepository phTemplateRepository;
	
	@Override
	public PhTemplate save(PhTemplate phTemplate){
		return phTemplateRepository.save(phTemplate);
	}
	
	@Override
	public PhTemplate findOne(Long id){
		return phTemplateRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phTemplateRepository.delete(id);
	}

	@Override
	public List<PhTemplate> save(List<PhTemplate> entities){
		return phTemplateRepository.save(entities);
	}
}
