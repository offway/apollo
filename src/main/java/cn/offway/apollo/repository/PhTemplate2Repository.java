package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhTemplate2;

import java.util.List;

/**
 * 杂志模版2Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
public interface PhTemplate2Repository extends JpaRepository<PhTemplate2,Long>,JpaSpecificationExecutor<PhTemplate2> {

	/** 此处写一些自定义的方法 **/
	List<PhTemplate2> findById(Long id);
}
