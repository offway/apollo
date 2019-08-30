package cn.offway.apollo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhBrandService;

import cn.offway.apollo.domain.PhBrand;
import cn.offway.apollo.repository.PhBrandRepository;

import java.util.List;


/**
 * 品牌库Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhBrandServiceImpl implements PhBrandService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhBrandRepository phBrandRepository;
	
	@Override
	public PhBrand save(PhBrand phBrand){
		return phBrandRepository.save(phBrand);
	}
	
	@Override
	public PhBrand findOne(Long id){
		return phBrandRepository.findOne(id);
	}

	@Override
	public List<PhBrand> findAll(){
		return phBrandRepository.findAll();
	}
}
