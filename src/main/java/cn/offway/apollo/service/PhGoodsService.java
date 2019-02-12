package cn.offway.apollo.service;

import cn.offway.apollo.domain.PhGoods;

/**
 * 商品表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhGoodsService{

	PhGoods save(PhGoods phGoods);
	
	PhGoods findOne(Long id);
}
