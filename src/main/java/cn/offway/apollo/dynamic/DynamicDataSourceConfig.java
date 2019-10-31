package cn.offway.apollo.dynamic;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamicDataSourceConfig {
    @Qualifier("showRoom")
    @Bean
    @ConfigurationProperties("spring.datasource.druid.showRoom")
    public DataSource oneDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Qualifier("book")
    @Bean
    @ConfigurationProperties("spring.datasource.druid.book")
    public DataSource twoDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource(DataSource defaultTargetDataSource, DataSource twoDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DataSourceNames.SR, defaultTargetDataSource);
        targetDataSources.put(DataSourceNames.BK, twoDataSource);
        return new DynamicDataSource(defaultTargetDataSource, targetDataSources);
    }
}
