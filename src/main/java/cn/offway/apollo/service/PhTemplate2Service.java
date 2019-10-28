package cn.offway.apollo.service;


import java.util.List;

import cn.offway.apollo.domain.PhTemplate2;

/**
 * 杂志模版2Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
public interface PhTemplate2Service{

    PhTemplate2 save(PhTemplate2 phTemplate2);
	
    PhTemplate2 findOne(Long id);

    void delete(Long id);

    List<PhTemplate2> save(List<PhTemplate2> entities);

    List<PhTemplate2> findOneList(Long id);
}
