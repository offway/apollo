package cn.offway.apollo.service;

import java.util.List;

import cn.offway.apollo.domain.PhOrderExpressDetail;

/**
 * 订单物流详情Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhOrderExpressDetailService{

	PhOrderExpressDetail save(PhOrderExpressDetail phOrderExpressDetail);
	
	PhOrderExpressDetail findOne(Long id);

	List<PhOrderExpressDetail> findByMailNoOrderByAcceptTimeDesc(String mailno);
}
