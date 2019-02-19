package cn.offway.apollo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.apollo.domain.PhWardrobe;

/**
 * 衣柜Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhWardrobeRepository extends JpaRepository<PhWardrobe,Long>,JpaSpecificationExecutor<PhWardrobe> {

	@Query(nativeQuery=true,value="select * from ph_wardrobe w where w.unionid=?1  ORDER BY w.create_time desc")
	List<PhWardrobe> findByUnionid(String unionid);
	
	@Query(nativeQuery=true,value="select * from ph_wardrobe w where w.unionid=?1 and w.use_date>DATE_FORMAT(NOW(),'%Y-%m-%d') and EXISTS(select 1 from ph_goods_stock gs where gs.goods_id = w.goods_id and gs.size = w.size and gs.color=w.color and gs.stock>0 ) ORDER BY w.create_time desc")
	List<PhWardrobe> findEffectByUnionid(String unionid);
	
	@Modifying
	@Transactional
	@Query(nativeQuery=true,value="delete from ph_wardrobe where unionid=?1 and use_date<=DATE_FORMAT(NOW(),'%Y-%m-%d')")
	int delInvalid(String unionid);
	
	@Modifying
	@Transactional
	@Query(nativeQuery=true,value="delete from ph_wardrobe w where w.unionid=?1 and EXISTS(select 1 from ph_goods_stock gs where gs.goods_id = w.goods_id and gs.size = w.size and gs.color=w.color and gs.stock<=0)")
	int delLess(String unionid);
	
	List<PhWardrobe> findByIdIn(List<Long> ids);
}
