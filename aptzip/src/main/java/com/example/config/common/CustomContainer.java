package com.example.config.common;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomContainer implements
  WebServerFactoryCustomizer<TomcatServletWebServerFactory> { // ConfigurableServletWebServerFactory
  
  @Override
  public void customize(TomcatServletWebServerFactory factory) {
    // java.lang.IllegalArgumentException: ContextPath must start with '/' and not end with '/'
    // factory.setContextPath("/aptzip");
    // Root ContextPath must be specified using an empty string
    factory.setContextPath("");
    factory.setPort(8888);
    factory.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
    factory.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error/403"));
    factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
    factory.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/error/405"));
    factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
    factory.addErrorPages(new ErrorPage(Exception.class, "/error"));
    factory.addErrorPages(new ErrorPage(Throwable.class, "/error"));
    //factory.addAdditionalTomcatConnectors(new Connector("AJP/1.3"));
  }
  
  /*
    We need to redirect from HTTP to HTTPS. Without SSL, this application used
    port 8082. With SSL it will use port 8443. So, any request for 8082 needs to be
    redirected to HTTPS on 8443.
	*/
	// private Connector httpToHttpsRedirectConnector() {
	// 	Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
  //   connector.setScheme("http");
  //   connector.setPort(8888);
	// 	connector.setSecure(false);
	// 	connector.setRedirectPort(8443);
	// 	return connector;
	// }
}