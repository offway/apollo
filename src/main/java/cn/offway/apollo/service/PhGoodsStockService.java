package cn.offway.apollo.service;

import java.util.List;

import cn.offway.apollo.domain.PhGoodsStock;

/**
 * 商品库存Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhGoodsStockService{

	PhGoodsStock save(PhGoodsStock phGoodsStock);
	
	PhGoodsStock findOne(Long id);

	List<PhGoodsStock> findByGoodsId(Long goodsId);

	int updateStock(Long wrId);

	PhGoodsStock findByGoodsIdAndSizeAndColor(Long goodsId, String size, String color);
}
