package cn.offway.apollo.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import cn.offway.apollo.domain.PhGoods;
import cn.offway.apollo.domain.PhGoodsStock;
import cn.offway.apollo.domain.PhWardrobe;
import cn.offway.apollo.service.PhGoodsService;
import cn.offway.apollo.service.PhGoodsStockService;
import cn.offway.apollo.service.PhWardrobeService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.apollo.service.PhWardrobeAuditService;

import cn.offway.apollo.domain.PhWardrobeAudit;
import cn.offway.apollo.repository.PhWardrobeAuditRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 衣柜审核Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-02-10 11:49:36 Exp $
 */
@Service
public class PhWardrobeAuditServiceImpl implements PhWardrobeAuditService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhWardrobeAuditRepository phWardrobeAuditRepository;

	@Autowired
	private PhGoodsService phGoodsService;

	@Autowired
	private PhWardrobeService phWardrobeService;

	@Autowired
	private PhGoodsStockService phGoodsStockService;
	
	@Override
	public PhWardrobeAudit save(PhWardrobeAudit phWardrobeAudit){
		return phWardrobeAuditRepository.save(phWardrobeAudit);
	}
	
	@Override
	public PhWardrobeAudit findOne(Long id){
		return phWardrobeAuditRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phWardrobeAuditRepository.delete(id);
	}

	@Override
	public List<PhWardrobeAudit> save(List<PhWardrobeAudit> entities){
		return phWardrobeAuditRepository.save(entities);
	}

	@Override
	@Transactional
	public PhWardrobeAudit add(String unionid, Long goodsId, String color, String size, String useDate, String useName, String content, String returnDate, String photoDate, String formId) throws ParseException {
		PhWardrobeAudit wardrobeAudit = new PhWardrobeAudit();
		PhGoods phGoods = phGoodsService.findOne(goodsId);
		PhWardrobe phWardrobe = new PhWardrobe();
		phWardrobe.setBrandId(phGoods.getBrandId());
		phWardrobe.setBrandLogo(phGoods.getBrandLogo());
		phWardrobe.setBrandName(phGoods.getBrandName());
		phWardrobe.setCategory(phGoods.getCategory());
		phWardrobe.setColor(color);
		phWardrobe.setCreateTime(new Date());
		phWardrobe.setGoodsId(goodsId);
		phWardrobe.setGoodsName(phGoods.getName());
		phWardrobe.setSize(size);
		String remark = phWardrobe.getColor() +"_"+ phWardrobe.getSize()+"_";
		PhGoodsStock phGoodsStock = phGoodsStockService.findByGoodsIdAndSizeAndColor(goodsId,size,color);
		phWardrobe.setImage(phGoodsStock.getImage());
		phWardrobe.setIsOffway(phGoods.getIsOffway());
		phWardrobe.setType(phGoods.getType());
		phWardrobe.setUnionid(unionid);
		phWardrobe.setState("0");
		phWardrobe.setUseDate(DateUtils.parseDate(useDate, "yyyy-MM-dd"));
		phWardrobe = phWardrobeService.save(phWardrobe);
		wardrobeAudit.setUnionid(phWardrobe.getUnionid());
		wardrobeAudit.setWardrobeId(phWardrobe.getId());
		wardrobeAudit.setUseName(useName);
		wardrobeAudit.setContent(content);
		wardrobeAudit.setGoodsId(phWardrobe.getGoodsId());
		wardrobeAudit.setGoodsName(phWardrobe.getGoodsName());
		wardrobeAudit.setImage(phWardrobe.getImage());
		wardrobeAudit.setBrandId(phWardrobe.getBrandId());
		wardrobeAudit.setBrandName(phWardrobe.getBrandName());
		wardrobeAudit.setBrandLogo(phWardrobe.getBrandLogo());
		wardrobeAudit.setSize(phWardrobe.getSize());
		wardrobeAudit.setColor(phWardrobe.getColor());
		wardrobeAudit.setPhotoDate(DateUtils.parseDate(photoDate, "yyyy-MM-dd"));
		wardrobeAudit.setUseDate(phWardrobe.getUseDate());
		wardrobeAudit.setCreateTime(new Date());
		wardrobeAudit.setState("0");
		wardrobeAudit.setIsDel("0");
		wardrobeAudit.setReturnDate(DateUtils.parseDate(returnDate, "yyyy-MM-dd"));
		wardrobeAudit.setFormId(formId);
		return phWardrobeAuditRepository.save(wardrobeAudit);
	}

	@Override
	public PhWardrobeAudit findByWardrobeId(Long id) {
		return phWardrobeAuditRepository.findByWardrobeId(id);
	}
}
