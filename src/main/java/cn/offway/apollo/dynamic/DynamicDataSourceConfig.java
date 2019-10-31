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

    @Bean(name = "showRoom")
    @ConfigurationProperties("spring.datasource.druid.showRoom")
    public DataSource oneDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "book")
    @ConfigurationProperties("spring.datasource.druid.book")
    public DataSource twoDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource(@Qualifier("showRoom") DataSource defaultTargetDataSource, @Qualifier("book") DataSource twoDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DataSourceNames.SR, defaultTargetDataSource);
        targetDataSources.put(DataSourceNames.BK, twoDataSource);
        return new DynamicDataSource(defaultTargetDataSource, targetDataSources);
    }
}
