package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhReadcode;

/**
 * 阅读码购买使用表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-29 15:52:21 Exp $
 */
public interface PhReadcodeRepository extends JpaRepository<PhReadcode,Long>,JpaSpecificationExecutor<PhReadcode> {

	/** 此处写一些自定义的方法 **/
}
