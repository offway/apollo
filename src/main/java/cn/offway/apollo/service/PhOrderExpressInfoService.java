package cn.offway.apollo.service;

import cn.offway.apollo.domain.PhOrderExpressInfo;

/**
 * 订单物流Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhOrderExpressInfoService{

	PhOrderExpressInfo save(PhOrderExpressInfo phOrderExpressInfo);
	
	PhOrderExpressInfo findOne(Long id);
}
