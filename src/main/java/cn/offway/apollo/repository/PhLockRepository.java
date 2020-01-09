package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhLock;

import java.util.List;

/**
 * 解锁条件表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
public interface PhLockRepository extends JpaRepository<PhLock,Long>,JpaSpecificationExecutor<PhLock> {

	/** 此处写一些自定义的方法 **/
	PhLock findByGoodsIdAndTemplateTypeAndTemplateId(Long goodsid,String templateType,Long templateId);

	PhLock findByPid(Long pid);

	List<PhLock> findByGoodsId(Long id);
}
