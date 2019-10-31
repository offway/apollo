package cn.offway.apollo.service;


import java.util.List;

import cn.offway.apollo.domain.PhUser;

/**
 * 用户信息Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-30 14:38:27 Exp $
 */
public interface PhUserService{

    PhUser save(PhUser phUser);
	
    PhUser findOne(Long id);

    void delete(Long id);

    List<PhUser> save(List<PhUser> entities);
}
