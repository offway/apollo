package cn.offway.apollo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhLockService;

import cn.offway.apollo.domain.PhLock;
import cn.offway.apollo.repository.PhLockRepository;


/**
 * 解锁条件表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
@Service
public class PhLockServiceImpl implements PhLockService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhLockRepository phLockRepository;
	
	@Override
	public PhLock save(PhLock phLock){
		return phLockRepository.save(phLock);
	}
	
	@Override
	public PhLock findOne(Long id){
		return phLockRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phLockRepository.delete(id);
	}

	@Override
	public List<PhLock> save(List<PhLock> entities){
		return phLockRepository.save(entities);
	}
}
