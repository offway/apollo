package cn.offway.apollo.service;

import cn.offway.apollo.domain.PhGoodsCategory;

import java.util.List;

/**
 * 商品类目Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-01 11:26:00 Exp $
 */
public interface PhGoodsCategoryService {

	PhGoodsCategory save(PhGoodsCategory phGoodsCategory);
	
	PhGoodsCategory findById(Long id);

	List<PhGoodsCategory> findByGoodsTypeNameOrderBySortAsc(String goodsTypeName);

	List<PhGoodsCategory> findByGoodsType(Long goodsTypeId);
}
