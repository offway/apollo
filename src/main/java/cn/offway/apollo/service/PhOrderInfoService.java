package cn.offway.apollo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.apollo.domain.PhOrderInfo;
import cn.offway.apollo.utils.JsonResult;

/**
 * 订单Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhOrderInfoService{

	PhOrderInfo save(PhOrderInfo phOrderInfo);
	
	PhOrderInfo findOne(Long id);

	String generateOrderNo(String prefix);

	List<PhOrderInfo> save(List<PhOrderInfo> phOrderInfos);

	Page<PhOrderInfo> findByPage(String unionid, String type, Pageable page);

	PhOrderInfo findByOrderNo(String orderNo);

	List<PhOrderInfo> findAll(String unionid, String type);

	int countByUnionidAndIsUpload(String unionid, String isUpload);

	int countByUnionidAndStatusIn(String unionid, List<String> status);

	int notShowImage(String unionid);

	JsonResult saveOrder(String orderNo, String sendstarttime, String mailNo, Long addrId);
}
