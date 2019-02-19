package cn.offway.apollo.service.impl;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.apollo.domain.PhHoliday;
import cn.offway.apollo.repository.PhHolidayRepository;
import cn.offway.apollo.service.PhHolidayService;


/**
 * 节假日Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhHolidayServiceImpl implements PhHolidayService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhHolidayRepository phHolidayRepository;
	
	@Override
	public PhHoliday save(PhHoliday phHoliday){
		return phHolidayRepository.save(phHoliday);
	}
	
	@Override
	public PhHoliday findOne(Long id){
		return phHolidayRepository.findOne(id);
	}
	
	/**
	 * 检查是否工作日
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public boolean isWorkDay(String date) throws ParseException{
		int count = phHolidayRepository.countByHoliday(DateUtils.parseDate(date, "yyyy-MM-dd"));
		return count==0;
	}
	
	/**
	 * 检查是否工作日
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public  boolean isWorkDay(Date date) throws ParseException{
		return isWorkDay(DateFormatUtils.format(date, "yyyy-MM-dd"));
	}
	
	@Override
	public Date getNextWorkDay(String date) throws ParseException{
		Date d = DateUtils.addDays(DateUtils.parseDate(date, "yyyy-MM-dd"), 1);
		boolean iswork = isWorkDay(d);
		if(!iswork){
			return getNextWorkDay(d);
		}
		return d;
	}
	
	@Override
	public Date getNextWorkDay(Date date) throws ParseException{
		return getNextWorkDay(DateFormatUtils.format(date, "yyyy-MM-dd"));
	}
}
