package cn.offway.apollo.service;


import java.util.List;

import cn.offway.apollo.domain.PhTemplateConfig;

/**
 * 杂志模板配置Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
public interface PhTemplateConfigService{

    PhTemplateConfig save(PhTemplateConfig phTemplateConfig);
	
    PhTemplateConfig findOne(Long id);

    void delete(Long id);

    List<PhTemplateConfig> save(List<PhTemplateConfig> entities);

    List<PhTemplateConfig> findByGoodsId(Long id);
}
