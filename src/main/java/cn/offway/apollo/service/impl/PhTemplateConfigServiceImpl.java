package cn.offway.apollo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhTemplateConfigService;

import cn.offway.apollo.domain.PhTemplateConfig;
import cn.offway.apollo.repository.PhTemplateConfigRepository;


/**
 * 杂志模板配置Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
@Service
public class PhTemplateConfigServiceImpl implements PhTemplateConfigService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhTemplateConfigRepository phTemplateConfigRepository;
	
	@Override
	public PhTemplateConfig save(PhTemplateConfig phTemplateConfig){
		return phTemplateConfigRepository.save(phTemplateConfig);
	}
	
	@Override
	public PhTemplateConfig findOne(Long id){
		return phTemplateConfigRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phTemplateConfigRepository.delete(id);
	}

	@Override
	public List<PhTemplateConfig> save(List<PhTemplateConfig> entities){
		return phTemplateConfigRepository.save(entities);
	}
}
