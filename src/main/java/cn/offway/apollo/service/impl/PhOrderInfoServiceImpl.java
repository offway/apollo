package cn.offway.apollo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cn.offway.apollo.domain.PhOrderGoods;
import cn.offway.apollo.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.offway.apollo.domain.PhAddress;
import cn.offway.apollo.domain.PhOrderExpressInfo;
import cn.offway.apollo.domain.PhOrderInfo;
import cn.offway.apollo.dto.sf.ReqAddOrder;
import cn.offway.apollo.repository.PhOrderInfoRepository;
import cn.offway.apollo.utils.CommonResultCode;
import cn.offway.apollo.utils.JsonResult;
import cn.offway.apollo.utils.JsonResultHelper;


/**
 * 订单Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhOrderInfoServiceImpl implements PhOrderInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhOrderInfoRepository phOrderInfoRepository;
	
	@Autowired
	private SfExpressService sfExpressService;
	
	@Autowired
	private PhOrderExpressInfoService phOrderExpressInfoService;
	
	@Autowired
	private JsonResultHelper jsonResultHelper;
	
	@Autowired
	private PhAddressService phAddressService;

	@Autowired
	private PhOrderGoodsService phOrderGoodsService;
	
	@Override
	public PhOrderInfo save(PhOrderInfo phOrderInfo){
		return phOrderInfoRepository.save(phOrderInfo);
	}
	
	@Override
	public List<PhOrderInfo> save(List<PhOrderInfo> phOrderInfos){
		return phOrderInfoRepository.save(phOrderInfos);
	}
	
	@Override
	public PhOrderInfo findOne(Long id){
		return phOrderInfoRepository.findOne(id);
	}
	
	@Override
	public String generateOrderNo(String prefix){
		int count = phOrderInfoRepository.hasOrder(prefix);
		if(count == 0){
			phOrderInfoRepository.insert(prefix);
		}
		return phOrderInfoRepository.generateOrderNo(prefix);
	}
	
	@Override
	public PhOrderInfo findByOrderNo(String orderNo){
		return phOrderInfoRepository.findByOrderNo(orderNo);
	}
	
	@Override
	public int countByUnionidAndStatusIn(String unionid,List<String> status){
		return phOrderInfoRepository.countByUnionidAndStatusIn(unionid,status);
	}
	
	@Override
	public int countByUnionidAndIsUpload(String unionid,String isUpload){
		return phOrderInfoRepository.countByUnionidAndIsUpload(unionid, isUpload);
	}
	
	@Override
	public int notShowImage(String unionid){
		return phOrderInfoRepository.notShowImage(unionid);
	}
	
	@Override
	public Page<PhOrderInfo> findByPage(final String unionid,final String type,Pageable page){
		return phOrderInfoRepository.findAll(new Specification<PhOrderInfo>() {
			
			@Override
			public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				Date now = new Date();
				try {
					//0-发货中,1-使用中,2-归还中,3-已完成
					if("0".equals(type)){
						//使用日期之前
						In<String> in = criteriaBuilder.in(root.get("status"));
						in.value("0");
						in.value("1");
						params.add(in);
						params.add(criteriaBuilder.greaterThan(root.get("useDate"), DateUtils.parseDate(DateFormatUtils.format(now, "yyyy-MM-dd"), "yyyy-MM-dd")));

					}else if("1".equals(type)){
						//使用日期当天
						//params.add(criteriaBuilder.equal(root.get("status"), "1"));
						In<String> in = criteriaBuilder.in(root.get("status"));
						in.value("1");
						in.value("7");
						in.value("8");
						params.add(in);
						params.add(criteriaBuilder.lessThanOrEqualTo(root.get("useDate"), DateUtils.parseDate(DateFormatUtils.format(now, "yyyy-MM-dd"), "yyyy-MM-dd")));
					}else if("2".equals(type)){
						In<String> in = criteriaBuilder.in(root.get("status"));
						in.value("8");
						in.value("2");
						params.add(in);
						//params.add(criteriaBuilder.equal(root.get("status"), "2"));
					}else if("3".equals(type)){
						params.add(criteriaBuilder.equal(root.get("status"), "3"));
					}else if("4".equals(type)){
						params.add(criteriaBuilder.lessThanOrEqualTo(root.get("useDate"), DateUtils.parseDate(DateFormatUtils.format(now, "yyyy-MM-dd"), "yyyy-MM-dd")));
						params.add(criteriaBuilder.notEqual(root.get("isUpload"), "1"));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(StringUtils.isNotBlank(unionid)){
					params.add(criteriaBuilder.equal(root.get("unionid"), unionid));
				}
				
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createTime")));
				return null;
			}
		}, page);
	}
	
	@Override
	public List<PhOrderInfo> findAll(final String unionid,final String type){
		return phOrderInfoRepository.findAll(new Specification<PhOrderInfo>() {
			
			@Override
			public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				Date now = new Date();
				try {
					//0-发货中,1-使用中,2-归还中,3-已完成
					if("0".equals(type)){
						//使用日期之前
						In<String> in = criteriaBuilder.in(root.get("status"));
						in.value("0");
						in.value("1");
						params.add(in);
						params.add(criteriaBuilder.greaterThan(root.get("useDate"), DateUtils.parseDate(DateFormatUtils.format(now, "yyyy-MM-dd"), "yyyy-MM-dd")));
					}else if("1".equals(type)){
						//使用日期当天
						params.add(criteriaBuilder.equal(root.get("status"), "1"));
						params.add(criteriaBuilder.lessThanOrEqualTo(root.get("useDate"), DateUtils.parseDate(DateFormatUtils.format(now, "yyyy-MM-dd"), "yyyy-MM-dd")));
					}else if("2".equals(type)){
						params.add(criteriaBuilder.equal(root.get("status"), "2"));
					}else if("3".equals(type)){
						params.add(criteriaBuilder.equal(root.get("status"), "3"));
					}else if("4".equals(type)){
						params.add(criteriaBuilder.notEqual(root.get("isUpload"), "1"));
						params.add(criteriaBuilder.lessThanOrEqualTo(root.get("useDate"), DateUtils.parseDate(DateFormatUtils.format(now, "yyyy-MM-dd"), "yyyy-MM-dd")));

					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(StringUtils.isNotBlank(unionid)){
					params.add(criteriaBuilder.equal(root.get("unionid"), unionid));
				}
				
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		});
	}
	
	@Override
	public JsonResult saveOrder(String orderNo,String sendstarttime,String mailNo,Long addrId,String batch){
		PhOrderInfo phOrderInfo = findByOrderNo(orderNo);
		if("2".equals(phOrderInfo.getStatus())){
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.ORDER_BACK);
		}
		
		PhOrderExpressInfo expressInfo = phOrderExpressInfoService.findByOrderNoAndType(orderNo, "0");
		PhOrderExpressInfo phOrderExpressInfo = new PhOrderExpressInfo();
		phOrderExpressInfo.setCreateTime(new Date());
		phOrderExpressInfo.setExpressOrderNo(generateOrderNo("SF"));
		List<PhOrderGoods> phOrderGoods = phOrderGoodsService.findByOrderNoAndBatch(orderNo,batch);
		List<PhOrderGoods> goods = phOrderGoodsService.findByOrderNo(orderNo);
		List<PhOrderGoods> returngoods = phOrderGoodsService.findByOrderNoAndState(orderNo,"1");
		if(null != addrId){
			PhAddress phAddress = phAddressService.findOne(addrId);
			phOrderExpressInfo.setFromPhone(phAddress.getPhone());
			phOrderExpressInfo.setFromCity(phAddress.getCity());
			phOrderExpressInfo.setFromContent(phAddress.getContent());
			phOrderExpressInfo.setFromCounty(phAddress.getCounty());
			phOrderExpressInfo.setFromProvince(phAddress.getProvince());
			phOrderExpressInfo.setFromRealName(phAddress.getRealName());

		}else{
			phOrderExpressInfo.setFromPhone(expressInfo.getToPhone());
			phOrderExpressInfo.setFromCity(expressInfo.getToCity());
			phOrderExpressInfo.setFromContent(expressInfo.getToContent());
			phOrderExpressInfo.setFromCounty(expressInfo.getToCounty());
			phOrderExpressInfo.setFromProvince(expressInfo.getToProvince());
			phOrderExpressInfo.setFromRealName(expressInfo.getToRealName());
		}
		phOrderExpressInfo.setOrderNo(phOrderInfo.getOrderNo());
		phOrderExpressInfo.setToPhone(expressInfo.getFromPhone());
		phOrderExpressInfo.setToCity(expressInfo.getFromCity());
		phOrderExpressInfo.setToContent(expressInfo.getFromContent());
		phOrderExpressInfo.setToCounty(expressInfo.getFromCounty());
		phOrderExpressInfo.setToProvince(expressInfo.getFromProvince());
		phOrderExpressInfo.setToRealName(expressInfo.getFromRealName());
		phOrderExpressInfo.setType("1");
		
		
		if(StringUtils.isNotBlank(mailNo)){
			phOrderExpressInfo.setMailNo(mailNo);
			phOrderExpressInfo.setStatus("3");//运送中
			phOrderExpressInfoService.save(phOrderExpressInfo);
			if (goods.size() == (phOrderGoods.size()+returngoods.size())){
				phOrderInfo.setStatus("2");
			}else {
				phOrderInfo.setStatus("8");
			}
			save(phOrderInfo);
			List<PhOrderGoods> phOrderGoodsList = new ArrayList<>();
			for (PhOrderGoods phOrderGood : phOrderGoods) {
				phOrderGood.setReturnMailNo(mailNo);
				phOrderGood.setState("1");
				phOrderGoodsList.add(phOrderGood);
			}
			phOrderGoodsService.save(phOrderGoodsList);
			return jsonResultHelper.buildSuccessJsonResult(mailNo);
		}else{
			
			ReqAddOrder addOrder = new ReqAddOrder();
			addOrder.setD_address(phOrderExpressInfo.getToContent());
			addOrder.setD_contact(phOrderExpressInfo.getToRealName());
			addOrder.setD_tel(phOrderExpressInfo.getToPhone());
			
			addOrder.setD_province(phOrderExpressInfo.getToProvince());
			addOrder.setD_city(phOrderExpressInfo.getToCity());
			addOrder.setD_county(phOrderExpressInfo.getToCounty());
			
			addOrder.setJ_province(phOrderExpressInfo.getFromProvince());
			addOrder.setJ_city(phOrderExpressInfo.getFromCity());
			addOrder.setJ_county(phOrderExpressInfo.getFromCounty());
			addOrder.setJ_address(phOrderExpressInfo.getFromContent());
			addOrder.setJ_contact(phOrderExpressInfo.getFromRealName());
			addOrder.setJ_tel(phOrderExpressInfo.getFromPhone());
			addOrder.setOrder_source("OFFWAY");
			addOrder.setOrder_id(phOrderExpressInfo.getExpressOrderNo());
			addOrder.setPay_method("1");//付款方式：1:寄方付2:收方付3:第三方付
			addOrder.setRemark("");
			addOrder.setSendstarttime(sendstarttime);
			JsonResult result = sfExpressService.addOrder(addOrder);
			if("200".equals(result.getCode())){
				String mailNo1 = String.valueOf(result.getData());
				phOrderExpressInfo.setMailNo(mailNo1);
				phOrderExpressInfo.setStatus("1");//已下单
				phOrderExpressInfoService.save(phOrderExpressInfo);
				if (goods.size() == (phOrderGoods.size()+returngoods.size())){
					phOrderInfo.setStatus("2");
				}else {
					phOrderInfo.setStatus("8");
				}
				save(phOrderInfo);
				List<PhOrderGoods> phOrderGoodsList = new ArrayList<>();
				for (PhOrderGoods phOrderGood : phOrderGoods) {
					phOrderGood.setReturnMailNo(mailNo1);
					phOrderGood.setState("1");
					phOrderGoodsList.add(phOrderGood);
				}
				phOrderGoodsService.save(phOrderGoodsList);
			}
			return result;
		}
		
		
		
	}
	
	
}
