package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhTemplate;

/**
 * 杂志管理Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 12:01:34 Exp $
 */
public interface PhTemplateRepository extends JpaRepository<PhTemplate,Long>,JpaSpecificationExecutor<PhTemplate> {

	/** 此处写一些自定义的方法 **/
}
