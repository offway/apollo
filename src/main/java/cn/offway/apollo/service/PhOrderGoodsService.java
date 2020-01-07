package cn.offway.apollo.service;

import java.util.List;

import cn.offway.apollo.domain.PhOrderGoods;

/**
 * 订单商品Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhOrderGoodsService{

	PhOrderGoods save(PhOrderGoods phOrderGoods);
	
	PhOrderGoods findOne(Long id);

	List<PhOrderGoods> save(List<PhOrderGoods> phOrderGoodss);

	List<PhOrderGoods> findByOrderNo(String orderNo);

	List<String> orderSum(String orderNo);
}
