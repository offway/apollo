package cn.offway.apollo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhShowImageService;

import cn.offway.apollo.domain.PhShowImage;
import cn.offway.apollo.repository.PhShowImageRepository;


/**
 * 晒图Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhShowImageServiceImpl implements PhShowImageService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhShowImageRepository phShowImageRepository;
	
	@Override
	public PhShowImage save(PhShowImage phShowImage){
		return phShowImageRepository.save(phShowImage);
	}
	
	@Override
	public PhShowImage findOne(Long id){
		return phShowImageRepository.findOne(id);
	}
}
