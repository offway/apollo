package cn.offway.apollo.service;


import java.text.ParseException;
import java.util.List;

import cn.offway.apollo.domain.PhWardrobeAudit;
import org.springframework.transaction.annotation.Transactional;

/**
 * 衣柜审核Service接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-02-10 11:49:36 Exp $
 */
public interface PhWardrobeAuditService{

    PhWardrobeAudit save(PhWardrobeAudit phWardrobeAudit);
	
    PhWardrobeAudit findOne(Long id);

    void delete(Long id);

    List<PhWardrobeAudit> save(List<PhWardrobeAudit> entities);

    @Transactional
    PhWardrobeAudit add(String unionid, Long goodsId, String color, String size, String useDate, String useName, String content, String returnDate, String photoDate, String formId) throws ParseException;

    PhWardrobeAudit findByWardrobeId(Long id);

    int auditCount(String unionid);
}
