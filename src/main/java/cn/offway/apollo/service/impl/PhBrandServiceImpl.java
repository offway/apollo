package cn.offway.apollo.service.impl;

import cn.offway.apollo.domain.PhBanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhBrandService;

import cn.offway.apollo.domain.PhBrand;
import cn.offway.apollo.repository.PhBrandRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
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
		//return phBrandRepository.findAll();
		return phBrandRepository.findAll(new Specification<PhBrand>() {
			@Override
			public Predicate toPredicate(Root<PhBrand> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				criteriaQuery.where(criteriaBuilder.equal(root.get("status"),"1"));
				return null;
			}
		},new Sort("id"));
	}
}
