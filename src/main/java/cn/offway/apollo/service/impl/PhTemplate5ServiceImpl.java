package cn.offway.apollo.service.impl;

import cn.offway.apollo.domain.PhTemplate5;
import cn.offway.apollo.dynamic.DS;
import cn.offway.apollo.dynamic.DataSourceNames;
import cn.offway.apollo.repository.PhTemplate5Repository;
import cn.offway.apollo.service.PhTemplate5Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 杂志模版5Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
@Service
public class PhTemplate5ServiceImpl implements PhTemplate5Service {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhTemplate5Repository phTemplate5Repository;

    @Override
    @DS(DataSourceNames.BK)
    public PhTemplate5 save(PhTemplate5 phTemplate5) {
        return phTemplate5Repository.save(phTemplate5);
    }

    @Override
    @DS(DataSourceNames.BK)
    public PhTemplate5 findOne(Long id) {
        return phTemplate5Repository.findOne(id);
    }

    @Override
    @DS(DataSourceNames.BK)
    public void delete(Long id) {
        phTemplate5Repository.delete(id);
    }

    @Override
    @DS(DataSourceNames.BK)
    public List<PhTemplate5> save(List<PhTemplate5> entities) {
        return phTemplate5Repository.save(entities);
    }
}
