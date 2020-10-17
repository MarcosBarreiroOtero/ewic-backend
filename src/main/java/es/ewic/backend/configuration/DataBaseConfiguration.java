package es.ewic.backend.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataBaseConfiguration {

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
		localSessionFactoryBean.setDataSource(lazyDataSource());
		localSessionFactoryBean.setPackagesToScan("es.ewic.backend.model");
		localSessionFactoryBean.setHibernateProperties(hibernateProperties());
		return localSessionFactoryBean;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUsername("ewic");
		dataSource.setPassword("ewic");
		dataSource.setUrl("jdbc:mysql://localhost:/ewic?serverTimezone=UTC");

		return dataSource;
	}

	@Bean
	public DataSource lazyDataSource() {
		LazyConnectionDataSourceProxy dataSource = new LazyConnectionDataSourceProxy(dataSource());
		return dataSource;
	}

	public Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		properties.put("hibernate.show_sql", "yes");
		properties.put("format_sql", "true");
		properties.put("use_sql_comments", "true");

		// TODO revisar. Método para habilitar el tipo de fetch Lazy pero ralentiza la
		// aplicación
		properties.put("hibernate.enable_lazy_load_no_trans", "true");
		return properties;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(sessionFactory().getObject());
		return hibernateTransactionManager;
	}
}
