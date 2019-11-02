package cn.offway.apollo.service.impl;

import cn.offway.apollo.domain.PhOrder;
import cn.offway.apollo.dynamic.DS;
import cn.offway.apollo.dynamic.DataSourceNames;
import cn.offway.apollo.repository.PhOrderRepository;
import cn.offway.apollo.service.PhOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 电子刊购买订单Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-31 14:58:50 Exp $
 */
@Service
public class PhOrderServiceImpl implements PhOrderService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhOrderRepository phOrderRepository;

    @Override
    @DS(DataSourceNames.BK)
    public PhOrder save(PhOrder phOrder) {
        return phOrderRepository.save(phOrder);
    }

    @Override
    @DS(DataSourceNames.BK)
    public PhOrder findOne(Long id) {
        return phOrderRepository.findOne(id);
    }

    @Override
    @DS(DataSourceNames.BK)
    public void delete(Long id) {
        phOrderRepository.delete(id);
    }

    @Override
    @DS(DataSourceNames.BK)
    public PhOrder findOne(String orderNo) {
        return phOrderRepository.findByOrderNo(orderNo);
    }

    @Override
    @DS(DataSourceNames.BK)
    public List<PhOrder> save(List<PhOrder> entities) {
        return phOrderRepository.save(entities);
    }
}
