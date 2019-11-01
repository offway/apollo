package cn.offway.apollo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhReadcodeService;

import cn.offway.apollo.domain.PhReadcode;
import cn.offway.apollo.repository.PhReadcodeRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


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

	@Override
	public List<PhReadcode> findAllBybuyersid(Long id){
		return phReadcodeRepository.findAllBybuyersid(id);
	}

	@Override
	public List<PhReadcode> findByUseridCode(Long id){
		return phReadcodeRepository.findByUseridCode(id);
	}

	@Override
	public Page<PhReadcode> findByBuyersIdAndBooksId(String state,Long userid, Long id, Pageable page){
		return phReadcodeRepository.findAll(new Specification<PhReadcode>() {
			@Override
			public Predicate toPredicate(Root<PhReadcode> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				if ("1".equals(state)){
					params.add(criteriaBuilder.equal(root.get("state"), "0"));
				}else if ("2".equals(state)){
					params.add(criteriaBuilder.equal(root.get("state"), "1"));
				}
				params.add(criteriaBuilder.equal(root.get("buyersId"), userid));
				params.add(criteriaBuilder.equal(root.get("booksId"), id));
				Predicate[] predicates = new Predicate[params.size()];
				criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		},page);
	}
}
