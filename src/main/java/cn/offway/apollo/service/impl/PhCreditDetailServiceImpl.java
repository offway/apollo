package cn.offway.apollo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhCreditDetailService;

import cn.offway.apollo.domain.PhCreditDetail;
import cn.offway.apollo.repository.PhCreditDetailRepository;


/**
 * 信用明细Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhCreditDetailServiceImpl implements PhCreditDetailService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhCreditDetailRepository phCreditDetailRepository;
	
	@Override
	public PhCreditDetail save(PhCreditDetail phCreditDetail){
		return phCreditDetailRepository.save(phCreditDetail);
	}
	
	@Override
	public PhCreditDetail findOne(Long id){
		return phCreditDetailRepository.findOne(id);
	}
}
