package cn.offway.apollo.service;

import cn.offway.apollo.domain.PhAuth;

/**
 * 用户认证Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhAuthService{

	PhAuth save(PhAuth phAuth);
	
	PhAuth findOne(Long id);
}
