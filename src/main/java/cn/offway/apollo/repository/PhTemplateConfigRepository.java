package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhTemplateConfig;

import java.util.List;

/**
 * 杂志模板配置Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
public interface PhTemplateConfigRepository extends JpaRepository<PhTemplateConfig,Long>,JpaSpecificationExecutor<PhTemplateConfig> {

	/** 此处写一些自定义的方法 **/

	List<PhTemplateConfig> findByGoodsIdOrderBySort(Long id);
}
