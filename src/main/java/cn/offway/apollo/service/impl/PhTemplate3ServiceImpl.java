package cn.offway.apollo.service.impl;

import cn.offway.apollo.domain.PhTemplate3;
import cn.offway.apollo.dynamic.DS;
import cn.offway.apollo.dynamic.DataSourceNames;
import cn.offway.apollo.repository.PhTemplate3Repository;
import cn.offway.apollo.service.PhTemplate3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 杂志模版3Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
@Service
public class PhTemplate3ServiceImpl implements PhTemplate3Service {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhTemplate3Repository phTemplate3Repository;

    @Override
    @DS(DataSourceNames.BK)
    public PhTemplate3 save(PhTemplate3 phTemplate3) {
        return phTemplate3Repository.save(phTemplate3);
    }

    @Override
    @DS(DataSourceNames.BK)
    public PhTemplate3 findOne(Long id) {
        return phTemplate3Repository.findOne(id);
    }

    @Override
    @DS(DataSourceNames.BK)
    public void delete(Long id) {
        phTemplate3Repository.delete(id);
    }

    @Override
    @DS(DataSourceNames.BK)
    public List<PhTemplate3> save(List<PhTemplate3> entities) {
        return phTemplate3Repository.save(entities);
    }
}
