package cn.offway.apollo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhOrderGoodsService;

import cn.offway.apollo.domain.PhOrderGoods;
import cn.offway.apollo.repository.PhOrderGoodsRepository;


/**
 * 订单商品Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhOrderGoodsServiceImpl implements PhOrderGoodsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhOrderGoodsRepository phOrderGoodsRepository;
	
	@Override
	public PhOrderGoods save(PhOrderGoods phOrderGoods){
		return phOrderGoodsRepository.save(phOrderGoods);
	}
	
	@Override
	public List<PhOrderGoods> save(List<PhOrderGoods> phOrderGoodss){
		return phOrderGoodsRepository.save(phOrderGoodss);
	}
	
	@Override
	public PhOrderGoods findOne(Long id){
		return phOrderGoodsRepository.findOne(id);
	}
	
	@Override
	public List<PhOrderGoods> findByOrderNo(String orderNo){
		return phOrderGoodsRepository.OrderNoAndStateIn(orderNo,"2");
	}

	@Override
	public List<PhOrderGoods> findByOrderNoR(String orderNo){
		return phOrderGoodsRepository.OrderNoAndStateInR(orderNo);
	}

	@Override
	public List<PhOrderGoods> findByOrderNo(String orderNo,String batch){
		return phOrderGoodsRepository.OrderNoAndStateIn(orderNo,"2",batch);
	}

	@Override
	public List<String> orderSum(String orderNo){
		return phOrderGoodsRepository.orderSum(orderNo);
	}

	@Override
	public List<String> orderSumR(String orderNo){
		return phOrderGoodsRepository.orderSumR(orderNo);
	}

	@Override
	public String GetMailNo(String orderNo, String batch){
		return phOrderGoodsRepository.GetMailNo(orderNo,batch);
	}

	@Override
	public List<PhOrderGoods> findByOrderNoAndBatch(String orderNo, String batch){
		return phOrderGoodsRepository.findByOrderNoAndBatch(orderNo,batch);
	}

	@Override
	public List<PhOrderGoods> findByOrderNoAndState(String orderNo, String state){
		return phOrderGoodsRepository.findByOrderNoAndState(orderNo,state);
	}
}
