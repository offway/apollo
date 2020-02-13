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

	List<PhOrderGoods> findByOrderNoR(String orderNo);

	List<PhOrderGoods> findByOrderNo(String orderNo, String batch);

	List<PhOrderGoods> findByOrder(String orderNo);

	List<String> orderSum(String orderNo);

	List<String> orderSumA(String orderNo);

	List<String> orderSumR(String orderNo);

	String GetMailNo(String orderNo, String batch);

	List<PhOrderGoods> findByOrderNoAndBatch(String orderNo, String batch);

	List<PhOrderGoods> findByOrderNoAndState(String orderNo, String state);
}
