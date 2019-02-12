package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhShowImage;

/**
 * 晒图Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhShowImageRepository extends JpaRepository<PhShowImage,Long>,JpaSpecificationExecutor<PhShowImage> {

	/** 此处写一些自定义的方法 **/
}
