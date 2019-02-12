package cn.offway.apollo.service;

import cn.offway.apollo.domain.PhAddress;

/**
 * 地址管理Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhAddressService{

	PhAddress save(PhAddress phAddress);
	
	PhAddress findOne(Long id);
}
