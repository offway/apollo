package cn.offway.apollo.service;


import java.util.List;

import cn.offway.apollo.domain.PhTemplate1;

/**
 * 杂志模版1Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
public interface PhTemplate1Service{

    PhTemplate1 save(PhTemplate1 phTemplate1);
	
    PhTemplate1 findOne(Long id);

    void delete(Long id);

    List<PhTemplate1> save(List<PhTemplate1> entities);
}
