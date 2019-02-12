package cn.offway.apollo.service;

import cn.offway.apollo.domain.PhBanner;

/**
 * Banner管理Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhBannerService{

	PhBanner save(PhBanner phBanner);
	
	PhBanner findOne(Long id);
}
