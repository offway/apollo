package cn.offway.apollo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.apollo.domain.PhGoodsStock;

/**
 * 商品库存Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhGoodsStockRepository extends JpaRepository<PhGoodsStock,Long>,JpaSpecificationExecutor<PhGoodsStock> {

	List<PhGoodsStock> findByGoodsId(Long goodsId);
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="update ph_goods_stock set stock = stock-1 where goods_id in (select goods_id from ph_wardrobe where id in(?1)) and stock>0")
	int updateStock(List<Long> wrIds);
}
