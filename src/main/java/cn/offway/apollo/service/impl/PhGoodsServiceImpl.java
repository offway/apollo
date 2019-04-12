package cn.offway.apollo.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.offway.apollo.domain.PhGoods;
import cn.offway.apollo.domain.PhGoodsStock;
import cn.offway.apollo.dto.GoodsDto;
import cn.offway.apollo.repository.PhGoodsRepository;
import cn.offway.apollo.service.PhGoodsService;


/**
 * 商品表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhGoodsServiceImpl implements PhGoodsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhGoodsRepository phGoodsRepository;
	
	@Override
	public PhGoods save(PhGoods phGoods){
		return phGoodsRepository.save(phGoods);
	}
	
	@Override
	public PhGoods findOne(Long id){
		return phGoodsRepository.findOne(id);
	}
	
	@Override
	public String goodsConfig(){
		return phGoodsRepository.goodsConfig();
	}
	
	@Override
	public Page<PhGoods> findByPage(final GoodsDto goodsDto,Pageable page){
		return phGoodsRepository.findAll(new Specification<PhGoods>() {
			
			@Override
			public Predicate toPredicate(Root<PhGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(goodsDto.getBrandName())){
					params.add(criteriaBuilder.like(root.get("brandName"), "%"+goodsDto.getBrandName()+"%"));
				}
				
				if(StringUtils.isNotBlank(goodsDto.getName())){
					params.add(criteriaBuilder.like(root.get("name"), "%"+goodsDto.getName()+"%"));
				}
				
				if(null != goodsDto.getBrandId()){
					params.add(criteriaBuilder.equal(root.get("brandId"), goodsDto.getBrandId()));
				}
				
				if(StringUtils.isNotBlank(goodsDto.getIsOffway())){
					params.add(criteriaBuilder.equal(root.get("isOffway"), goodsDto.getIsOffway()));
				}
				
				if(StringUtils.isNotBlank(goodsDto.getIsRelease())){
					params.add(criteriaBuilder.equal(root.get("isRelease"), goodsDto.getIsRelease()));
				}
				
				if(StringUtils.isNotBlank(goodsDto.getCategory())){
					params.add(criteriaBuilder.equal(root.get("category"), goodsDto.getCategory()));
				}
				
				if(StringUtils.isNotBlank(goodsDto.getType())){
					
					In<String> in = criteriaBuilder.in(root.get("type"));
					in.value(goodsDto.getType());
					in.value("男女同款");
					params.add(in);
//					params.add(criteriaBuilder.equal(root.get("type"), goodsDto.getType()));
				}
				
				params.add(criteriaBuilder.equal(root.get("status"),  "1"));

				if(StringUtils.isNotBlank(goodsDto.getSize())){
					Subquery<PhGoodsStock> subquery = criteriaQuery.subquery(PhGoodsStock.class);
					Root<PhGoodsStock> subRoot = subquery.from(PhGoodsStock.class);
					subquery.select(subRoot);
					subquery.where(
							criteriaBuilder.equal(root.get("id"), subRoot.get("goodsId")),
							criteriaBuilder.equal(subRoot.get("size"), goodsDto.getSize())
							);
					params.add(criteriaBuilder.exists(subquery));
				}
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
				return null;
			}
		}, page);
	}
}
