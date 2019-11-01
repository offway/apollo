package cn.offway.apollo.service;


import java.util.List;

import cn.offway.apollo.domain.PhReadcode;

/**
 * 阅读码购买使用表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-29 15:52:21 Exp $
 */
public interface PhReadcodeService{

    PhReadcode save(PhReadcode phReadcode);
	
    PhReadcode findOne(Long id);

    void delete(Long id);

    List<PhReadcode> save(List<PhReadcode> entities);

    List<PhReadcode> findAllBybuyersid(Long id);

    List<PhReadcode> findByUseridCode(Long id);

    List<PhReadcode> findByBuyersIdAndBooksId(Long userid,Long id);
}
