package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhTemplate5;

/**
 * 杂志模版5Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
public interface PhTemplate5Repository extends JpaRepository<PhTemplate5,Long>,JpaSpecificationExecutor<PhTemplate5> {

	/** 此处写一些自定义的方法 **/
}
