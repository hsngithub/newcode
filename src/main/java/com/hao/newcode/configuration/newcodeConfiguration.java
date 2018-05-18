package com.hao.newcode.configuration;

import com.hao.newcode.Interceptor.PassportInterceptor;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;

import javax.xml.ws.WebServiceException;

@Component
public class newcodeConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	PassportInterceptor passportInterceptor;//有一个拦截器类的对象

	public void addInterceptors(InterceptorRegistry registry){
		registry.addInterceptor(passportInterceptor);
		super.addInterceptors(registry);
	}


}
