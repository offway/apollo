package cn.offway.apollo.service.impl;

import cn.offway.apollo.domain.PhUser;
import cn.offway.apollo.dynamic.DS;
import cn.offway.apollo.dynamic.DataSourceNames;
import cn.offway.apollo.repository.PhUserRepository;
import cn.offway.apollo.service.PhUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * 用户信息Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-30 14:38:27 Exp $
 */
@Service
public class PhUserServiceImpl implements PhUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhUserRepository phUserRepository;

    @Override
    @DS(DataSourceNames.BK)
    public PhUser save(PhUser phUser) {
        return phUserRepository.save(phUser);
    }

    @Override
    @DS(DataSourceNames.BK)
    public PhUser findOne(Long id) {
        return phUserRepository.findOne(id);
    }

    @Override
    @DS(DataSourceNames.BK)
    public void delete(Long id) {
        phUserRepository.delete(id);
    }

    @Override
    @DS(DataSourceNames.BK)
    public List<PhUser> save(List<PhUser> entities) {
        return phUserRepository.save(entities);
    }

    @Override
    @DS(DataSourceNames.BK)
    public PhUser findByUnionid(String unionid) {
        return phUserRepository.findByUnionid(unionid);
    }

    @Override
    @DS(DataSourceNames.BK)
    public PhUser findByPhone(String phone) {
        return phUserRepository.findByPhone(phone);
    }

    @Override
    @DS(DataSourceNames.BK)
    public int getMaxUserId() {
        Optional<Integer> res = phUserRepository.getMax();
        return res.isPresent() ? res.get() : 0;
    }

    @Override
    @DS(DataSourceNames.BK)
    public PhUser registered(String phone, String unionid, String nickName,
                             String headimgurl, long id) {
        PhUser phUserInfo = new PhUser();
        phUserInfo.setId(id);
        phUserInfo.setPhone(phone);
        if (StringUtils.isNotBlank(phone)) {
            nickName = StringUtils.isBlank(nickName) ? "OFFWAY_" + phone.substring(5) : nickName;
        }
        phUserInfo.setNickname(nickName);
        phUserInfo.setHeadimgurl(headimgurl);
        phUserInfo.setUnionid(unionid);
        phUserInfo.setSex("1");
        phUserInfo.setCreateTime(new Date());
        phUserInfo = save(phUserInfo);
        return phUserInfo;
    }

    @Override
    @DS(DataSourceNames.BK)
    public PhUser registered(String unionid, String nickName,
                             String headimgurl, long id) {
        PhUser phUserInfo = new PhUser();
        phUserInfo.setId(id);
        phUserInfo.setNickname(nickName);
        phUserInfo.setHeadimgurl(headimgurl);
        phUserInfo.setUnionid(unionid);
        phUserInfo.setSex("1");
        phUserInfo.setCreateTime(new Date());
        phUserInfo = save(phUserInfo);
        return phUserInfo;
    }
}
