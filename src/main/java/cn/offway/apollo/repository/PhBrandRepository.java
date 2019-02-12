package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhBrand;

/**
 * 品牌库Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhBrandRepository extends JpaRepository<PhBrand,Long>,JpaSpecificationExecutor<PhBrand> {

	/** 此处写一些自定义的方法 **/
}
