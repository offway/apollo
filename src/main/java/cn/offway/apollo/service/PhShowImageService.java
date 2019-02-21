package cn.offway.apollo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.apollo.domain.PhShowImage;

/**
 * 晒图Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhShowImageService{

	PhShowImage save(PhShowImage phShowImage);
	
	PhShowImage findOne(Long id);

	Page<PhShowImage> findByPage(String unionid, Pageable page);
}
