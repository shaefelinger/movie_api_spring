package de.functionfactory.movie_api.tech.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        connector.setAllowTrace(false);
        connector.setXpoweredBy(false);
        connector.setEnableLookups(false);
        connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
        connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
        connector.setProperty("preserveMethodOnForward", "true");
        return connector;
    }

}
