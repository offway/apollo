package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.apollo.domain.PhOrderInfo;
import java.lang.String;
import java.util.List;

/**
 * 订单Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhOrderInfoRepository extends JpaRepository<PhOrderInfo,Long>,JpaSpecificationExecutor<PhOrderInfo> {

	/**
	 * 生成订单号
	 * @return
	 */
	@Query(nativeQuery=true,value="select CONCAT(?1,DATE_FORMAT(NOW(),'%Y%m%d'),LPAD(nextval(CONCAT(?1,DATE_FORMAT(NOW(),'%Y%m%d'))), 5, 0))")
	String generateOrderNo(String prefix);
	
	@Query(nativeQuery=true,value="select count(*) from sequence where name=CONCAT(?1,DATE_FORMAT(NOW(),'%Y%m%d'))")
	int hasOrder(String prefix);
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="insert into sequence values(CONCAT(?1,DATE_FORMAT(NOW(),'%Y%m%d')),0,1)")
	int insert(String prefix);
	
	PhOrderInfo findByOrderNo(String orderNo);
	
	int countByUnionidAndStatusIn(String unionid,List<String> status);
	
	int countByUnionidAndIsUpload(String unionid,String isUpload);
	
	@Query(nativeQuery=true,value="select count(*) from ph_order_info o where o.status !='4' and o.unionid=?1 and DATE_FORMAT(NOW(),'%y-%m-%d') > DATE_ADD(o.use_date,INTERVAL 30 DAY)  and NOT EXISTS(select 1 from ph_show_image s where s.order_no = o.order_no )")
	int notShowImage(String unionid);
	
}
