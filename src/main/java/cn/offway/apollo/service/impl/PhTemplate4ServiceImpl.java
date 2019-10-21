package cn.offway.apollo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhTemplate4Service;

import cn.offway.apollo.domain.PhTemplate4;
import cn.offway.apollo.repository.PhTemplate4Repository;


/**
 * 杂志模版4Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
@Service
public class PhTemplate4ServiceImpl implements PhTemplate4Service {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhTemplate4Repository phTemplate4Repository;
	
	@Override
	public PhTemplate4 save(PhTemplate4 phTemplate4){
		return phTemplate4Repository.save(phTemplate4);
	}
	
	@Override
	public PhTemplate4 findOne(Long id){
		return phTemplate4Repository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phTemplate4Repository.delete(id);
	}

	@Override
	public List<PhTemplate4> save(List<PhTemplate4> entities){
		return phTemplate4Repository.save(entities);
	}
}
