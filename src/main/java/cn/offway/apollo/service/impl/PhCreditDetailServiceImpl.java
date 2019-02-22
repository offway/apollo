package cn.offway.apollo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhCreditDetailService;

import cn.offway.apollo.domain.PhCreditDetail;
import cn.offway.apollo.domain.PhOrderInfo;
import cn.offway.apollo.repository.PhCreditDetailRepository;


/**
 * 信用明细Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhCreditDetailServiceImpl implements PhCreditDetailService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhCreditDetailRepository phCreditDetailRepository;
	
	@Override
	public PhCreditDetail save(PhCreditDetail phCreditDetail){
		return phCreditDetailRepository.save(phCreditDetail);
	}
	
	@Override
	public PhCreditDetail findOne(Long id){
		return phCreditDetailRepository.findOne(id);
	}
	
	@Override
	public Page<PhCreditDetail> findByPage(final String unionid,Pageable page){
		return phCreditDetailRepository.findAll(new Specification<PhCreditDetail>() {
			
			@Override
			public Predicate toPredicate(Root<PhCreditDetail> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(unionid)){
					params.add(criteriaBuilder.equal(root.get("unionid"), unionid));
				}
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createTime")));
				return null;
			}
		}, page);
	}
}
