package cn.offway.apollo.service.impl;

import cn.offway.apollo.domain.PhTemplate2;
import cn.offway.apollo.dynamic.DS;
import cn.offway.apollo.dynamic.DataSourceNames;
import cn.offway.apollo.repository.PhTemplate2Repository;
import cn.offway.apollo.service.PhTemplate2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 杂志模版2Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
@Service
public class PhTemplate2ServiceImpl implements PhTemplate2Service {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhTemplate2Repository phTemplate2Repository;

    @Override
    @DS(DataSourceNames.BK)
    public PhTemplate2 save(PhTemplate2 phTemplate2) {
        return phTemplate2Repository.save(phTemplate2);
    }

    @Override
    @DS(DataSourceNames.BK)
    public PhTemplate2 findOne(Long id) {
        return phTemplate2Repository.findOne(id);
    }

    @Override
    @DS(DataSourceNames.BK)
    public void delete(Long id) {
        phTemplate2Repository.delete(id);
    }

    @Override
    @DS(DataSourceNames.BK)
    public List<PhTemplate2> save(List<PhTemplate2> entities) {
        return phTemplate2Repository.save(entities);
    }

    @Override
    @DS(DataSourceNames.BK)
    public List<PhTemplate2> findOneList(Long id) {
        return phTemplate2Repository.findById(id);
    }
}
