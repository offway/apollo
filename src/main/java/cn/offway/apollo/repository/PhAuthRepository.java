package cn.offway.apollo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.apollo.domain.PhAuth;

/**
 * 用户认证Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhAuthRepository extends JpaRepository<PhAuth,Long>,JpaSpecificationExecutor<PhAuth> {

	int countByUnionidAndStatusIn(String unionid,List<String> status);
	
	PhAuth findByUnionid(String unionid);
}
