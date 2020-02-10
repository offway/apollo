package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhWardrobeAudit;
import org.springframework.data.jpa.repository.Query;

/**
 * 衣柜审核Repository接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-02-10 11:49:36 Exp $
 */
public interface PhWardrobeAuditRepository extends JpaRepository<PhWardrobeAudit,Long>,JpaSpecificationExecutor<PhWardrobeAudit> {

	/** 此处写一些自定义的方法 **/

    PhWardrobeAudit findByWardrobeId(Long id);

    @Query(nativeQuery=true,value="select count(*) from ph_wardrobe_audit where unionid = ?1 and  state = 0 and is_del = 0 and use_date>DATE_FORMAT(DATE_ADD(NOW(),INTERVAL 2 day),'%Y-%m-%d')")
    int auditCount(String unionid);
}
