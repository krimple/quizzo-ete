package org.springframework.samples.async.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.examples.quizzo.repository.PlayerAnswerRepository;
import org.springframework.data.examples.quizzo.repository.PlayerRepository;
import org.springframework.data.examples.quizzo.repository.QuizRepository;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.samples.async.quizzo.engine.GameRunEngine;
import org.springframework.samples.async.quizzo.engine.inmemory.GameRunEngineInMemoryImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = { "org.springframework.samples.async" })
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    QuizRepository quizRepository;
    @Autowired
    PlayerAnswerRepository playerAnswerRepository;
    @Autowired
    PlayerRepository playerRepository;


    @Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(30*1000L);
	}

	@Override
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJacksonHttpMessageConverter());
	}

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("chat");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("resources/");
	}

//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new CORSInterceptor());
//        super.addInterceptors(registry);
//    }

    @Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		return engine;
	}

	@Bean
	public TemplateResolver templateResolver() {
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
		resolver.setPrefix("/WEB-INF/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");
		resolver.setCacheable(false);
		return resolver;
	}

    @Bean
    public GameRunEngine quizRun() {
        return new GameRunEngineInMemoryImpl(quizRepository, playerRepository, playerAnswerRepository);
    }


}
