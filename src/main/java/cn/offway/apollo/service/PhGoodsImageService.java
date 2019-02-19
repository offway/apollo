package cn.offway.apollo.service;

import java.util.List;

import cn.offway.apollo.domain.PhGoodsImage;

/**
 * 商品图片Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhGoodsImageService{

	PhGoodsImage save(PhGoodsImage phGoodsImage);
	
	PhGoodsImage findOne(Long id);

	List<PhGoodsImage> findByGoodsId(Long goodsId);

	List<String> findByGoodsId(Long goodsId, String type);
}
