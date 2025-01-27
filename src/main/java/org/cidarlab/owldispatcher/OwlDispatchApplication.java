package org.cidarlab.owldispatcher;

import java.io.IOException;
import java.util.Arrays;

import org.cidarlab.owldispatcher.adaptors.PigeonClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@ComponentScan({"org.cidarlab.owldispatcher",
				"org.cidarlab.owldispatcher.adaptors",
				"org.cidarlab.owldispatcher.controller",
				"org.cidarlab.owldispatcher.DOM",
				"org.cidarlab.owldispatcher.exception",
				"org.cidarlab.owldispatcher.model"})
@SpringBootApplication
public class OwlDispatchApplication {

	public static void main(String[] args) throws UnirestException {
		ApplicationContext ctx = SpringApplication.run(OwlDispatchApplication.class, args);
		
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for( String name : beanNames ){
			System.out.println(name);
		}
		
		/*try {
			//PigeonClient.requestPigeon("p p1 2%0D%0Ar r2 3%0d%0ag g3 6%0D%0At t4 13%0d%0a# Arcs");
			PigeonClient.requestPigeon("p p 13%0D%0Ar rbs 13%0D%0Ac g 13%0D%0At t 13%0D%0Ap p 13%0D%0Ar rbs 13%0D%0Ac g 13%0D%0At t 13%0D%0Ap p 13%0D%0Ar rbs 13%0D%0Ac g 13%0D%0At t 13%0D%0Ap p 13%0D%0Ar rbs 13%0D%0Ac g 13%0D%0At t 13%0D%0Ap p 13%0D%0Ar rbs 13%0D%0Ac g 13%0D%0At t 13%0D%0Ap p 13%0D%0Ar rbs 13%0D%0Ac g 13%0D%0At t 13%0D%0Ap p 13%0D%0Ar rbs 13%0D%0Ac g 13%0D%0At t 13%0D%0Ap p 13%0D%0Ar rbs 13%0D%0Ac g 13%0D%0At t 13%0D%0A# Arcs");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}