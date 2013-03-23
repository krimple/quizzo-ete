package org.phillyete.quizzo.web.config;

import java.util.List;

import org.phillyete.quizzo.web.engine.PlayerGameSession;
import org.phillyete.quizzo.web.engine.PlayerGameSessionImpl;
import org.phillyete.quizzo.web.engine.QuizModeratorSession;
import org.phillyete.quizzo.web.engine.QuizModeratorSessionImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

//@Configuration
//@EnableScheduling
//@ComponentScan(basePackages = { "org.phillyete.quizzo" })
public class WebMvcConfig {
//
//
//	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//		configurer.setDefaultTimeout(30*1000L);
//	}
//
//	@Override
//	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//		converters.add(new MappingJacksonHttpMessageConverter());
//	}
//
//	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/").setViewName("chat");
//	}
//
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/resources/**").addResourceLocations("resources/").setCachePeriod(-1);
//	}
//
//    @Bean
//	public ViewResolver viewResolver() {
//		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//		resolver.setTemplateEngine(templateEngine());
//		return resolver;
//	}
//
//	@Bean
//	public SpringTemplateEngine templateEngine() {
//		SpringTemplateEngine engine = new SpringTemplateEngine();
//		engine.setTemplateResolver(templateResolver());
//		return engine;
//	}
//
//	@Bean
//	public TemplateResolver templateResolver() {
//		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
//		resolver.setPrefix("/WEB-INF/templates/");
//		resolver.setSuffix(".html");
//		resolver.setTemplateMode("HTML5");
//		resolver.setCacheable(false);
//		return resolver;
//	}
//
//    @Bean
//    @Scope(value = WebApplicationContext.SCOPE_SESSION,
//           proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public QuizModeratorSession quizModeratorSession() {
//        return new QuizModeratorSessionImpl();
//    }
//
//    @Bean
//    @Scope(value = WebApplicationContext.SCOPE_SESSION,
//            proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public PlayerGameSession playerGameSession() {
//        return new PlayerGameSessionImpl();
//    }
//

}
