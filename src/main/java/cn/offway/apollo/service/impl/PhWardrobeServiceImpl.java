package cn.offway.apollo.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.apollo.domain.PhAddress;
import cn.offway.apollo.domain.PhBrand;
import cn.offway.apollo.domain.PhGoods;
import cn.offway.apollo.domain.PhOrderExpressInfo;
import cn.offway.apollo.domain.PhOrderGoods;
import cn.offway.apollo.domain.PhOrderInfo;
import cn.offway.apollo.domain.PhUserInfo;
import cn.offway.apollo.domain.PhWardrobe;
import cn.offway.apollo.repository.PhWardrobeRepository;
import cn.offway.apollo.service.PhAddressService;
import cn.offway.apollo.service.PhBrandService;
import cn.offway.apollo.service.PhGoodsService;
import cn.offway.apollo.service.PhGoodsStockService;
import cn.offway.apollo.service.PhOrderExpressInfoService;
import cn.offway.apollo.service.PhOrderGoodsService;
import cn.offway.apollo.service.PhOrderInfoService;
import cn.offway.apollo.service.PhUserInfoService;
import cn.offway.apollo.service.PhWardrobeService;
import cn.offway.apollo.utils.CommonResultCode;
import cn.offway.apollo.utils.JsonResult;
import cn.offway.apollo.utils.JsonResultHelper;


/**
 * 衣柜Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhWardrobeServiceImpl implements PhWardrobeService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhWardrobeRepository phWardrobeRepository;
	
	@Autowired
	private PhGoodsService phGoodsService; 
	
	@Autowired
	private JsonResultHelper jsonResultHelper;
	
	@Autowired
	private PhOrderInfoService phOrderInfoService;
	
	@Autowired
	private PhOrderExpressInfoService phOrderExpressInfoService;
	
	@Autowired
	private PhOrderGoodsService phOrderGoodsService;
	
	@Autowired
	private PhBrandService phBrandService;
	
	@Autowired
	private PhAddressService phAddressService;
	
	@Autowired
	private PhGoodsStockService phGoodsStockService;
	
	@Autowired
	private PhUserInfoService phUserInfoService;
	
	
	@Override
	public PhWardrobe save(PhWardrobe phWardrobe){
		return phWardrobeRepository.save(phWardrobe);
	}
	
	@Override
	public PhWardrobe findOne(Long id){
		return phWardrobeRepository.findOne(id);
	}
	
	@Override
	public void delete(Long id){
		phWardrobeRepository.delete(id);
	}
	
	@Override
	public void delInvalid(String unionid){
		phWardrobeRepository.delInvalid(unionid);
		phWardrobeRepository.delLess(unionid);
	}
	
	@Override
	public JsonResult add(String unionid,Long goodsId,String color,String size,String useDate) throws Exception{
		
		List<PhWardrobe> phWardrobes = phWardrobeRepository.findEffectByUnionid(unionid);
		if(null != phWardrobes && phWardrobes.size() >=8){
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.WARDROBE_LIMIT);
		}
		
		PhUserInfo phUserInfo = phUserInfoService.findByUnionid(unionid);
		if(phUserInfo.getCreditScore().longValue() <500L){
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.CREDITSCORE_LESS);
		}
		
		int showCount = phOrderInfoService.countByUnionidAndIsUpload(unionid, "0");
		if(showCount >0){
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.NO_SHOW_IMAGE);
		}
		
		List<String> status = new ArrayList<>();
		status.add("0");
		status.add("1");
		int countStatus = phOrderInfoService.countByUnionidAndStatusIn(unionid, status);
		if(countStatus >0){
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.NO_RETURN_IMAGE);
		}
		
		
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
		phWardrobe.setImage(phGoods.getImage());
		phWardrobe.setIsOffway(phGoods.getIsOffway());
		phWardrobe.setSize(size);
		phWardrobe.setType(phGoods.getType());
		phWardrobe.setUnionid(unionid);
		phWardrobe.setUseDate(DateUtils.parseDate(useDate, "yyyy-MM-dd"));
		
		save(phWardrobe);
		
		return jsonResultHelper.buildSuccessJsonResult(null);
	}
	
	@Override
	public Map<String, Object> list(String unionid){
		
		//所有
		List<PhWardrobe> all = phWardrobeRepository.findByUnionid(unionid);
		//有效
		List<PhWardrobe> eff = phWardrobeRepository.findEffectByUnionid(unionid);
		Map<String, Object> resultMap = new HashMap<>();
		
		Map<String, List<PhWardrobe>> effMap = new HashMap<>();
		List<PhWardrobe> invalids = new ArrayList<>();

		Date now = new Date();
		for (PhWardrobe wr : all) {
			boolean exists = false;
			for (PhWardrobe w : eff) {
				if(wr.getId().longValue() == w.getId().longValue()){
					exists = true;
					break;
				}
			}
			if(exists){
				String key = "1".equals(wr.getIsOffway())?"OFFWAY Showroom":wr.getBrandName();
				key = key+","+DateFormatUtils.format(wr.getUseDate(), "yyyy-MM-dd");
				List<PhWardrobe> wardrobes = effMap.get(key);
				if(null == wardrobes ||wardrobes.isEmpty()){
					wardrobes = new ArrayList<>();
				}
				wardrobes.add(wr);
				effMap.put(key, wardrobes);
				
			}else if(wr.getUseDate().before(now)){
				//使用时间无效
				wr.setRemark("1");
				invalids.add(wr);
			}else{
				//缺货
				wr.setRemark("0");
				invalids.add(wr);
			}
		}
		
		List<Object> effs = new ArrayList<>();
		for (String key : effMap.keySet()) {
			Map<String, Object> map = new HashMap<>();
			String[] keys = key.split(",");
			map.put("brandName", keys[0]);
			map.put("useDate", keys[1]);
			map.put("data", effMap.get(key));
			effs.add(map);
		}
		resultMap.put("effect", effs);
		resultMap.put("invalid", invalids);
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> list(List<Long> wrids){
		
		List<PhWardrobe> eff = phWardrobeRepository.findByIdIn(wrids);
		Map<String, Object> resultMap = new HashMap<>();
		
		Map<String, List<PhWardrobe>> effMap = new HashMap<>();

		for (PhWardrobe wr : eff) {
			String key = "1".equals(wr.getIsOffway())?"OFFWAY Showroom":wr.getBrandName();
			key = key+","+DateFormatUtils.format(wr.getUseDate(), "yyyy-MM-dd");
			List<PhWardrobe> wardrobes = effMap.get(key);
			if(null == wardrobes ||wardrobes.isEmpty()){
				wardrobes = new ArrayList<>();
			}
			wardrobes.add(wr);
			effMap.put(key, wardrobes);
		}
		
		List<Object> effs = new ArrayList<>();
		for (String key : effMap.keySet()) {
			Map<String, Object> map = new HashMap<>();
			String[] keys = key.split(",");
			map.put("brandName", keys[0]);
			map.put("useDate", keys[1]);
			map.put("data", effMap.get(key));
			effs.add(map);
		}
		resultMap.put("effect", effs);
		
		return resultMap;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
	public JsonResult addOrder(String unionid,Long[] wardrobeIds,Long addrId,String users) throws Exception{


		List<PhWardrobe> phWardrobes = phWardrobeRepository.findEffectByUnionid(unionid);
		if(null != phWardrobes && phWardrobes.size() >=8){
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.WARDROBE_LIMIT);
		}
		
		PhUserInfo phUserInfo = phUserInfoService.findByUnionid(unionid);
		if(phUserInfo.getCreditScore().longValue() <500L){
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.CREDITSCORE_LESS);
		}
		
		int showCount = phOrderInfoService.countByUnionidAndIsUpload(unionid, "0");
		if(showCount >0){
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.NO_SHOW_IMAGE);
		}
		
		List<String> status = new ArrayList<>();
		status.add("0");
		status.add("1");
		int countStatus = phOrderInfoService.countByUnionidAndStatusIn(unionid, status);
		if(countStatus >0){
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.NO_RETURN_IMAGE);
		}
		
		
		List<Long> wrIds = Arrays.asList(wardrobeIds);
		
		//减库存
		int count= phGoodsStockService.updateStock(wrIds);
		if(count !=wrIds.size()){
			throw new Exception("减库存失败");
		}
		
		Date now = new Date();
		List<PhOrderInfo> phOrderInfos = new ArrayList<>();
		List<PhOrderGoods> phOrderGoodss = new ArrayList<>();
		List<PhOrderExpressInfo> phOrderExpressInfos = new ArrayList<>();
		Map<Long, PhOrderInfo> map = new HashMap<>();
		List<PhWardrobe> wardrobes = phWardrobeRepository.findByIdIn(wrIds);
		for (PhWardrobe phWardrobe : wardrobes) {
			
			
			PhOrderInfo phOrderInfo = map.get(phWardrobe.getBrandId());
			if(null == phOrderInfo){
				//保存订单信息
				phOrderInfo = new PhOrderInfo();
				phOrderInfo.setBrandId(phWardrobe.getBrandId());
				phOrderInfo.setBrandLogo(phWardrobe.getBrandLogo());
				phOrderInfo.setBrandName(phWardrobe.getBrandName());
				phOrderInfo.setCreateTime(now);
				phOrderInfo.setIsOffway(phWardrobe.getIsOffway());
				phOrderInfo.setOrderNo(phOrderInfoService.generateOrderNo("PH"));
				phOrderInfo.setStatus("0");
				phOrderInfo.setUnionid(phWardrobe.getUnionid());
				phOrderInfo.setUseDate(phWardrobe.getUseDate());
				phOrderInfo.setUsers(users);
				phOrderInfos.add(phOrderInfo);
				map.put(phWardrobe.getBrandId(), phOrderInfo);
				
				//保存订单物流
				PhBrand phBrand = phBrandService.findOne(phWardrobe.getBrandId());
				PhAddress formAddress = phAddressService.findOne(phBrand.getAddrId());
				PhAddress toAddress = phAddressService.findOne(addrId);
				
				PhOrderExpressInfo phOrderExpressInfo = new PhOrderExpressInfo();
				phOrderExpressInfo.setCreateTime(now);
//				phOrderExpressInfo.setExpressOrderNo(phOrderInfoService.generateOrderNo("SF"));
				phOrderExpressInfo.setFromPhone(formAddress.getPhone());
				phOrderExpressInfo.setFromCity(formAddress.getCity());
				phOrderExpressInfo.setFromContent(formAddress.getContent());
				phOrderExpressInfo.setFromCounty(formAddress.getCounty());
				phOrderExpressInfo.setFromProvince(formAddress.getProvince());
				phOrderExpressInfo.setFromRealName(formAddress.getRealName());
				//phOrderExpressInfo.setMailNo(mailNo);
				//phOrderExpressInfo.setOrderId(orderId);
				phOrderExpressInfo.setOrderNo(phOrderInfo.getOrderNo());
				phOrderExpressInfo.setStatus("0");
				phOrderExpressInfo.setToPhone(toAddress.getPhone());
				phOrderExpressInfo.setToCity(toAddress.getCity());
				phOrderExpressInfo.setToContent(toAddress.getContent());
				phOrderExpressInfo.setToCounty(toAddress.getCounty());
				phOrderExpressInfo.setToProvince(toAddress.getProvince());
				phOrderExpressInfo.setToRealName(toAddress.getRealName());
				phOrderExpressInfo.setType("0");
				phOrderExpressInfos.add(phOrderExpressInfo);
				
			}
			//保存订单商品
			PhOrderGoods phOrderGoods = new PhOrderGoods();
			phOrderGoods.setColor(phWardrobe.getColor());
			phOrderGoods.setCreateTime(now);
			phOrderGoods.setGoodsId(phWardrobe.getGoodsId());
			phOrderGoods.setGoodsName(phWardrobe.getGoodsName());
			phOrderGoods.setImage(phWardrobe.getImage());
			//phOrderGoods.setOrderId(orderId);
			phOrderGoods.setOrderNo(phOrderInfo.getOrderNo());
			phOrderGoods.setSize(phWardrobe.getSize());
			phOrderGoodss.add(phOrderGoods);
			
		}
		
		
		phOrderInfoService.save(phOrderInfos);
		phOrderExpressInfoService.save(phOrderExpressInfos);
		phOrderGoodsService.save(phOrderGoodss);
		
		
		return jsonResultHelper.buildSuccessJsonResult(null);
	}
}
