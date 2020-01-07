package cn.offway.apollo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhOrderExpressInfoService;

import cn.offway.apollo.domain.PhOrderExpressInfo;
import cn.offway.apollo.domain.PhOrderInfo;
import cn.offway.apollo.repository.PhOrderExpressInfoRepository;


/**
 * 订单物流Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhOrderExpressInfoServiceImpl implements PhOrderExpressInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhOrderExpressInfoRepository phOrderExpressInfoRepository;
	
	@Override
	public PhOrderExpressInfo save(PhOrderExpressInfo phOrderExpressInfo){
		return phOrderExpressInfoRepository.save(phOrderExpressInfo);
	}
	
	@Override
	public List<PhOrderExpressInfo> save(List<PhOrderExpressInfo> phOrderExpressInfos){
		return phOrderExpressInfoRepository.save(phOrderExpressInfos);
	}
	
	@Override
	public PhOrderExpressInfo findOne(Long id){
		return phOrderExpressInfoRepository.findOne(id);
	}
	
	@Override
	public PhOrderExpressInfo findByOrderNoAndType(String orderNo,String type){
		return phOrderExpressInfoRepository.findByOrderNoAndType(orderNo, type);
	}

	@Override
	public PhOrderExpressInfo findByOrderNoAndTypeAndBatch(String orderNo,String type,String batch){
		return phOrderExpressInfoRepository.findByOrderNoAndTypeAndBatch(orderNo, type,batch);
	}
}
