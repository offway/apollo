package cn.offway.apollo.service.impl;

import cn.offway.apollo.domain.PhBanner;
import cn.offway.apollo.repository.PhBannerRepository;
import cn.offway.apollo.service.PhBannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;


/**
 * Banner管理Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhBannerServiceImpl implements PhBannerService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhBannerRepository phBannerRepository;

    @Override
    public PhBanner save(PhBanner phBanner) {
        return phBannerRepository.save(phBanner);
    }

    @Override
    public PhBanner findOne(Long id) {
        return phBannerRepository.findOne(id);
    }

    @Override
    public List<PhBanner> findAll() {

        return phBannerRepository.findAll(new Specification<PhBanner>() {
            @Override
            public Predicate toPredicate(Root<PhBanner> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate[] predicates = new Predicate[3];
                predicates[0] = criteriaBuilder.equal(root.get("status"),"1");
                predicates[1] = criteriaBuilder.lessThanOrEqualTo(root.get("beginTime"),new Date());
                predicates[2] = criteriaBuilder.greaterThan(root.get("endTime"),new Date());
                criteriaQuery.where(predicates);
                return null;
            }
        },new Sort("sort"));
    }
}
