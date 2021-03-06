package cn.offway.apollo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.apollo.domain.PhAddress;

/**
 * 地址管理Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhAddressRepository extends JpaRepository<PhAddress,Long>,JpaSpecificationExecutor<PhAddress> {

	List<PhAddress> findByUnionid(String unionid);
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="update ph_address set is_default='0' where unionid =?1")
	int updatePhAddress(String unionid);
	
	List<PhAddress> findByUnionidAndIsDefault(String unionid,String isDefault);
}
