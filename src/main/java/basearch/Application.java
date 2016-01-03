package basearch;

import io.undertow.Undertow.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;

import basearch.dao.AuthDao;
import basearch.dao.MetadataDao;
import basearch.dao.UserDao;
import basearch.dao.impl.AuthDaoImpl;
import basearch.dao.impl.MetadataDaoImpl;
import basearch.dao.impl.UserDaoImpl;
import basearch.service.AuthService;
import basearch.service.UserService;
import basearch.service.impl.AuthServiceImpl;
import basearch.service.impl.UserServiceImpl;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class,ManagementWebSecurityAutoConfiguration.class})
@ServletComponentScan
public class Application {

	@Autowired
	private MetadataDao metadataDao;
	@Autowired
	private UserDao userDao;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	@Bean
	public LocaleResolver localeResolver() {
		return new CustomLocaleResolver(metadataDao, userDao);
	}

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
    	final UndertowEmbeddedServletContainerFactory containerFactory = new UndertowEmbeddedServletContainerFactory();
    	containerFactory.addBuilderCustomizers(new UndertowBuilderCustomizer() {
			@Override
			public void customize(Builder builder) {
				String address = containerFactory.getAddress() == null ? "0.0.0.0" : containerFactory.getAddress().getHostAddress();
				builder.addHttpListener(8080, address);
			}
    	});
    	return containerFactory;
    }

}