package cn.offway.apollo.service.impl;

import cn.offway.apollo.domain.PhGoodsCategory;
import cn.offway.apollo.repository.PhGoodsCategoryRepository;
import cn.offway.apollo.service.PhGoodsCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * 商品类目Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-01 11:26:00 Exp $
 */
@Service
public class PhGoodsCategoryServiceImpl implements PhGoodsCategoryService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhGoodsCategoryRepository phGoodsCategoryRepository;
	
	@Override
	public PhGoodsCategory save(PhGoodsCategory phGoodsCategory){
		return phGoodsCategoryRepository.save(phGoodsCategory);
	}
	
	@Override
	public PhGoodsCategory findById(Long id){
		return phGoodsCategoryRepository.findOne(id);
	}
	
	@Override
	public List<PhGoodsCategory> findByGoodsTypeNameOrderBySortAsc(String goodsTypeName){
		return phGoodsCategoryRepository.findByGoodsTypeNameOrderBySortAsc(goodsTypeName);
	}

	@Override
	public List<PhGoodsCategory> findByGoodsType(Long goodsTypeId){
		return phGoodsCategoryRepository.findByGoodsTypeOrderBySort(goodsTypeId);
	}
}
