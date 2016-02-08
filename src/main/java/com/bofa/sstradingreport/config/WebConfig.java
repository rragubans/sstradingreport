package com.bofa.sstradingreport.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule.Priority;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * Maps all AngularJS routes to index so that they work with direct linking.
     */
    @Controller
    static class Routes {

        @RequestMapping({
                "/",
                "/tradereports",
                "/milages",
                "/gallery",
                "/tracks",
                "/location",
                "/about"
        })
        public String index() {
            return "forward:/index.html";
        }
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        super.configureContentNegotiation(configurer);
        configurer.favorParameter(true);
    }

    @Override
    public void configurePathMatch(final PathMatchConfigurer configurer) {
        configurer.setUseRegisteredSuffixPatternMatch(true);
    }

    @Bean
    public ObjectMapper jacksonObjectMapper() {
        return new ObjectMapper().registerModules(
                new JaxbAnnotationModule().setPriority(Priority.SECONDARY)
        );
    }

    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer(
            final @Value("${sstradingreport.connector.proxyName:}") String proxyName,
            final @Value("${sstradingreport.connector.proxyPort:80}") int proxyPort
    ) {
        return (ConfigurableEmbeddedServletContainer configurableContainer) -> {
            if (configurableContainer instanceof TomcatEmbeddedServletContainerFactory) {
                final TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) configurableContainer;
                containerFactory.setTldSkip("*.jar");
                if (!proxyName.isEmpty()) {
                    containerFactory.addConnectorCustomizers(connector -> {
                        connector.setProxyName(proxyName);
                        connector.setProxyPort(proxyPort);
                    });
                }
            }
        };
    }
}
