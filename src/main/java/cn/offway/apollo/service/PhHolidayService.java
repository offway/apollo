package cn.offway.apollo.service;

import java.text.ParseException;
import java.util.Date;

import cn.offway.apollo.domain.PhHoliday;

/**
 * 节假日Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhHolidayService{

	PhHoliday save(PhHoliday phHoliday);
	
	PhHoliday findOne(Long id);

	boolean isWorkDay(String date) throws ParseException;

	boolean isWorkDay(Date date) throws ParseException;

	Date getNextWorkDay(String date) throws ParseException;

	Date getNextWorkDay(Date date) throws ParseException;
}
