package cn.offway.apollo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhOrderGoods;
import org.springframework.data.jpa.repository.Query;

/**
 * 订单商品Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhOrderGoodsRepository extends JpaRepository<PhOrderGoods,Long>,JpaSpecificationExecutor<PhOrderGoods> {

	@Query(nativeQuery=true,value="select * from ph_order_goods where order_no = ?1 and state != ?2 and state!= '1' and batch = ?3 ")
	List<PhOrderGoods> OrderNoAndStateIn(String orderNo,String state,String batch);

	@Query(nativeQuery=true,value="select * from ph_order_goods where order_no = ?1 and state != ?2 ")
	List<PhOrderGoods> OrderNoAndStateIn(String orderNo,String state);

	@Query(nativeQuery=true,value="select * from ph_order_goods where order_no = ?1 and state = '1' ")
	List<PhOrderGoods> OrderNoAndStateInR(String orderNo);

	@Query(nativeQuery=true,value="select DISTINCT(batch) from ph_order_goods where order_no = ?1 and state != '2' ")
	List<String> orderSum(String orderNo);

	@Query(nativeQuery=true,value="select DISTINCT(mail_no) from ph_order_goods where order_no = ?1 and batch = ?2 ")
	String GetMailNo(String orderNo,String batch);

	@Query(nativeQuery=true,value="select DISTINCT(batch) from ph_order_goods where order_no = ?1 and state = '1' ")
	List<String> orderSumR(String orderNo);


	List<PhOrderGoods> findByOrderNoAndBatch(String orderNo,String batch);

	List<PhOrderGoods> findByOrderNoAndState(String orderNo,String state);
}
