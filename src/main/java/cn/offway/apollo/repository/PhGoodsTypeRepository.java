package cn.offway.apollo.repository;

import cn.offway.apollo.domain.PhGoodsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 商品类别Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-01 11:26:00 Exp $
 */
public interface PhGoodsTypeRepository extends JpaRepository<PhGoodsType,Long>, JpaSpecificationExecutor<PhGoodsType> {

	@Query(nativeQuery=true,value="select distinct(name) from ph_goods_type where name like ?1")
	List<String> findByNameLike(String name);
}
