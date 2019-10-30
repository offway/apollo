package cn.offway.apollo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhReadcode;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 阅读码购买使用表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-29 15:52:21 Exp $
 */
public interface PhReadcodeRepository extends JpaRepository<PhReadcode,Long>,JpaSpecificationExecutor<PhReadcode> {

	/** 此处写一些自定义的方法 **/
    @Query(nativeQuery=true,value="select id,books_id,state,code,buyers_id,use_id,use_time,create_time,count(*) as remark from ph_readcode where books_id = ?1 GROUP BY buyers_id")
    List<PhReadcode> findAllBybuyersid(Long roleId);
}