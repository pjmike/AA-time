package com.ctg.aatime;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author pjmike
 */
@SpringBootApplication
@MapperScan("com.ctg.aatime.dao")
@RestController
public class AatimeApplication {
	@RequestMapping("/hello")
	public String index() {
		return "hello world";
	}
	public static void main(String[] args) {
		SpringApplication.run(AatimeApplication.class, args);
	}

	/**
	 * http重定向到https
	 * <p>
	 * Springboot2.0中已经没有TomcatEmbeddedServletContainerFactory类，
	 * 改为TomcatServletWebServerFactory类
	 * </p>
	 * @return
	 */
	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(httpConnector());
		return tomcat;
	}


	@Bean
	public Connector httpConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(80);
		connector.setSecure(false);
		connector.setRedirectPort(443);
		return connector;
	}
}
