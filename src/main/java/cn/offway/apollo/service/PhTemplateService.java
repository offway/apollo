package cn.offway.apollo.service;


import java.util.List;

import cn.offway.apollo.domain.PhTemplate;

/**
 * 杂志管理Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 12:01:34 Exp $
 */
public interface PhTemplateService{

    PhTemplate save(PhTemplate phTemplate);
	
    PhTemplate findOne(Long id);

    void delete(Long id);

    List<PhTemplate> save(List<PhTemplate> entities);
}
