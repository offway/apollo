package cn.offway.apollo.service;


import java.util.List;

import cn.offway.apollo.domain.PhOrder;

/**
 * 电子刊购买订单Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-31 14:58:50 Exp $
 */
public interface PhOrderService{

    PhOrder save(PhOrder phOrder);
	
    PhOrder findOne(Long id);

    void delete(Long id);

    List<PhOrder> save(List<PhOrder> entities);
}
