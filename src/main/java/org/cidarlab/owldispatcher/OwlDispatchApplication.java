package org.cidarlab.owldispatcher;

import java.util.Arrays;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"org.cidarlab.owldispatcher",
				"org.cidarlab.owldispatcher.adaptors",
				"org.cidarlab.owldispatcher.controller",
				"org.cidarlab.owldispatcher.DOM",
				"org.cidarlab.owldispatcher.exception",
				"org.cidarlab.owldispatcher.model"})
@SpringBootApplication
public class OwlDispatchApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(OwlDispatchApplication.class, args);
		
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for( String name : beanNames ){
			System.out.println(name);
		}
	}
}