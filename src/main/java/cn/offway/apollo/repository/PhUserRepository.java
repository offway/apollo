package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhUser;

/**
 * 用户信息Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-30 14:38:27 Exp $
 */
public interface PhUserRepository extends JpaRepository<PhUser,Long>,JpaSpecificationExecutor<PhUser> {

	/** 此处写一些自定义的方法 **/
}
