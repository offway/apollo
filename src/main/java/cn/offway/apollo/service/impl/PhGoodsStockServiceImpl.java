package cn.offway.apollo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhGoodsStockService;

import cn.offway.apollo.domain.PhGoodsStock;
import cn.offway.apollo.repository.PhGoodsStockRepository;


/**
 * 商品库存Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhGoodsStockServiceImpl implements PhGoodsStockService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhGoodsStockRepository phGoodsStockRepository;
	
	@Override
	public PhGoodsStock save(PhGoodsStock phGoodsStock){
		return phGoodsStockRepository.save(phGoodsStock);
	}
	
	@Override
	public PhGoodsStock findOne(Long id){
		return phGoodsStockRepository.findOne(id);
	}
}
