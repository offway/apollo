package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhOrder;

/**
 * 电子刊购买订单Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-31 14:58:50 Exp $
 */
public interface PhOrderRepository extends JpaRepository<PhOrder,Long>,JpaSpecificationExecutor<PhOrder> {

	/** 此处写一些自定义的方法 **/
}
