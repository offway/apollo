package cn.offway.apollo.service;

import cn.offway.apollo.domain.PhCreditDetail;

/**
 * 信用明细Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhCreditDetailService{

	PhCreditDetail save(PhCreditDetail phCreditDetail);
	
	PhCreditDetail findOne(Long id);
}
