package cn.offway.apollo.service;

import java.util.Map;

import cn.offway.apollo.domain.PhWardrobe;
import cn.offway.apollo.utils.JsonResult;

/**
 * 衣柜Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhWardrobeService{

	PhWardrobe save(PhWardrobe phWardrobe);
	
	PhWardrobe findOne(Long id);

	Map<String, Object> list(String unionid);

	void add(String unionid, Long goodsId, String color, String size, String useDate) throws Exception;

	void delete(Long id);

	void delInvalid(String unionid);


	JsonResult addOrder(Long[] wardrobeIds, Long addrId, String users) throws Exception;
}
