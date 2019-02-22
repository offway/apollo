package cn.offway.apollo.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
import cn.offway.apollo.service.PhOrderInfoService;
import cn.offway.apollo.domain.PhOrderInfo;
import cn.offway.apollo.repository.PhOrderInfoRepository;


/**
 * 订单Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhOrderInfoServiceImpl implements PhOrderInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhOrderInfoRepository phOrderInfoRepository;
	
	@Override
	public PhOrderInfo save(PhOrderInfo phOrderInfo){
		return phOrderInfoRepository.save(phOrderInfo);
	}
	
	@Override
	public List<PhOrderInfo> save(List<PhOrderInfo> phOrderInfos){
		return phOrderInfoRepository.save(phOrderInfos);
	}
	
	@Override
	public PhOrderInfo findOne(Long id){
		return phOrderInfoRepository.findOne(id);
	}
	
	@Override
	public String generateOrderNo(String prefix){
		int count = phOrderInfoRepository.hasOrder(prefix);
		if(count == 0){
			phOrderInfoRepository.insert(prefix);
		}
		return phOrderInfoRepository.generateOrderNo(prefix);
	}
	
	@Override
	public PhOrderInfo findByOrderNo(String orderNo){
		return phOrderInfoRepository.findByOrderNo(orderNo);
	}
	
	@Override
	public Page<PhOrderInfo> findByPage(final String unionid,final String type,Pageable page){
		return phOrderInfoRepository.findAll(new Specification<PhOrderInfo>() {
			
			@Override
			public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				Date now = new Date();
				try {
					//0-发货中,1-使用中,2-归还中,3-已完成
					if("0".equals(type)){
						//使用日期之前
						In<String> in = criteriaBuilder.in(root.get("status"));
						in.value("0");
						in.value("1");
						params.add(in);
					}else if("1".equals(type)){
						//使用日期当天
						params.add(criteriaBuilder.equal(root.get("status"), "1"));
						params.add(criteriaBuilder.equal(root.get("useDate"), DateUtils.parseDate(DateFormatUtils.format(now, "yyyy-MM-dd"), "yyyy-MM-dd")));
					}else if("2".equals(type)){
						params.add(criteriaBuilder.equal(root.get("status"), "2"));
					}else if("3".equals(type)){
						params.add(criteriaBuilder.equal(root.get("status"), "3"));
					}else if("4".equals(type)){
						params.add(criteriaBuilder.notEqual(root.get("isUpload"), "1"));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
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
	
	@Override
	public List<PhOrderInfo> findAll(final String unionid,final String type){
		return phOrderInfoRepository.findAll(new Specification<PhOrderInfo>() {
			
			@Override
			public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				Date now = new Date();
				try {
					//0-发货中,1-使用中,2-归还中,3-已完成
					if("0".equals(type)){
						//使用日期之前
						In<String> in = criteriaBuilder.in(root.get("status"));
						in.value("0");
						in.value("1");
						params.add(in);
					}else if("1".equals(type)){
						//使用日期当天
						params.add(criteriaBuilder.equal(root.get("status"), "1"));
						params.add(criteriaBuilder.equal(root.get("useDate"), DateUtils.parseDate(DateFormatUtils.format(now, "yyyy-MM-dd"), "yyyy-MM-dd")));
					}else if("2".equals(type)){
						params.add(criteriaBuilder.equal(root.get("status"), "2"));
					}else if("3".equals(type)){
						params.add(criteriaBuilder.equal(root.get("status"), "3"));
					}else if("4".equals(type)){
						params.add(criteriaBuilder.notEqual(root.get("isUpload"), "1"));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(StringUtils.isNotBlank(unionid)){
					params.add(criteriaBuilder.equal(root.get("unionid"), unionid));
				}
				
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		});
	}
	
}
