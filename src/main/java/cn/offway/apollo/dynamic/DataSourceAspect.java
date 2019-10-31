package cn.offway.apollo.dynamic;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceAspect implements Ordered {
    @Override
    public int getOrder() {
        return 0;
    }

    @Pointcut("@annotation(cn.offway.apollo.dynamic.DS)")
    public void dataSourcePointCut() {
    }

    @Before("dataSourcePointCut()")
    public void around(JoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DS ds = method.getAnnotation(DS.class);
        if (ds == null) {
            // 通过判断 DataSource 中的值来判断当前方法应用哪个数据源
            DynamicDataSource.setDataSource(DataSourceNames.SR);
        } else {
            // 通过判断 DataSource 中的值来判断当前方法应用哪个数据源
            DynamicDataSource.setDataSource(ds.value());
        }
    }

    @AfterReturning("dataSourcePointCut()")
    public void after() {
        DynamicDataSource.clearDataSource();
    }
}
