package com.eshop.zipkin.db;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author Leach
 * @date 2017/3/13
 */
@Configuration
@MapperScan("com.eshop.zipkin.mapper")
@EnableTransactionManagement
public class MybatisDataSource {

    /** mybaits mapper xml搜索路径 **/
    private static final String MAPPER_LOCATIONS = "classpath*:sqlmap/**/*.xml";

    private static final String CONFIG_LOCATION = "classpath:mapperConfig.xml";

    private DruidDataSource datasource = null;
    
    @Bean
    @ConfigurationProperties("spring.datasource.druid.stat.filter")
    public StatFilter statFilter(){
    	StatFilter filter = new StatFilter();
        return filter;
    }
    
    @Bean(destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource dataSource() {
        datasource = new DruidDataSource();
        datasource.getProxyFilters().add(statFilter());
        return datasource;
    }
    
    @PreDestroy
    public void close() {
        if (datasource != null) {
            datasource.close();
        }
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(MAPPER_LOCATIONS));
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource(CONFIG_LOCATION));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }


}
