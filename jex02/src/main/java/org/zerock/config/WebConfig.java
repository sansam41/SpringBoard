package org.zerock.config;


import java.nio.file.DirectoryStream.Filter;

import javax.servlet.ServletRegistration;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[] {RootConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[] {ServletConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		return new String[] {"/"};
	}
	
	protected void customizeRegistration(ServletRegistration.Dynamic registration)
	{
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
	}
	
	protected javax.servlet.Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter=new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		
		return new javax.servlet.Filter[] {characterEncodingFilter};
	}
}
