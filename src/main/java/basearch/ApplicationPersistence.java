package basearch;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class ApplicationPersistence {

	@Autowired
	private DataSource dataSource;

	@Bean
	@Profile("default")
	public LocalContainerEntityManagerFactoryBean developmentEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());

		// jpa settings
		Map<String,String> properties = new HashMap<>();
		properties.put("eclipselink.weaving", "static");
		properties.put("eclipselink.logging.level", "FINE");
		properties.put("eclipselink.cache.shared.default", "false");
		factory.setJpaPropertyMap(properties);

		return factory;
	}

	@Bean
	@Profile("production")
	public LocalContainerEntityManagerFactoryBean productionEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());

		// jpa settings
		Map<String,String> properties = new HashMap<>();
		properties.put("eclipselink.weaving", "static");
		properties.put("eclipselink.logging.level", "OFF");
		factory.setJpaPropertyMap(properties);

		return factory;
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager manager = new JpaTransactionManager();
		manager.setEntityManagerFactory(entityManagerFactory);
		return manager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslator() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

}