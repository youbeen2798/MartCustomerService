package com.nhnacademy.springmvc.config;


import com.nhnacademy.springmvc.controller.ControllerBase;
import com.nhnacademy.springmvc.interceptor.LoginInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = ControllerBase.class)
public class WebConfig implements WebMvcConfigurer, ApplicationContextAware, MessageSourceAware {

  private ApplicationContext applicationContext;
  private MessageSource messageSource;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
    registry.viewResolver(thymeleafViewResolver());
  }

  @Bean
  public LocaleResolver localeResolver() {
    return new SessionLocaleResolver();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LocaleChangeInterceptor());
    registry.addInterceptor(new LoginInterceptor())
        .excludePathPatterns("/cs/login", "/cs/logout", "/cs", "/cs/inquiry", "/cs/admin", "/cs/admin/answer", "/cs/admin/answer/**");

  }

  @Bean
  public MultipartResolver multipartResolver() {
    //multipartResolver 빈 설정
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setMaxUploadSize(-1);

    return multipartResolver;
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    //upload form 설정
    registry.addViewController("/upload").setViewName("upload");
  }

  @Bean
  public ThymeleafViewResolver thymeleafViewResolver() {
    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(templateEngine());
    viewResolver.setCharacterEncoding("UTF-8");
    viewResolver.setOrder(1);

    return viewResolver;
  }

  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());
    templateEngine.setTemplateEngineMessageSource(messageSource);

    return templateEngine;
  }

  public SpringResourceTemplateResolver templateResolver() {
    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setCharacterEncoding("UTF-8");
    templateResolver.setPrefix("/WEB-INF/view/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode("HTML5");

    return templateResolver;
  }

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.favorParameter(true)   //URL 호출 시 특정 파라미터로 리턴포맷 전달 허용 여부 ex)a.do?format=json
        .parameterName("mediaType")
        .ignoreAcceptHeader(false) //HttpRequest Header의 Accept 무시 여부
        .defaultContentType(MediaType.APPLICATION_JSON) //데이터 타입이 선언되지 않았다면 JSON 타입
        .mediaType("json", MediaType.APPLICATION_JSON) //JSON 타입 선언
        .mediaType("xml", MediaType.APPLICATION_XML); //XML 타입 선언
  }
}