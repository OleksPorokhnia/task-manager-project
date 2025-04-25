package com.alex.project.taskmanagerproject.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MainWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{MainConfig.class, WebSocketConfiguration.class, JwtConfig.class};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }
}
