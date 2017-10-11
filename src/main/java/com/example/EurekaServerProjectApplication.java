package com.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableEurekaServer
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
@RestController
public class EurekaServerProjectApplication {

	
public static void main(String[] args) {
	SpringApplication.run(EurekaServerProjectApplication.class, args);
}

// This method is for resolving duplicate metrics exposed by Prometheus clients
 @RequestMapping(method=RequestMethod.GET,value="/prometheus-metrics", produces=MediaType.TEXT_PLAIN_VALUE)
 public ResponseEntity<String> getData(HttpServletRequest req){
 	System.out.println("Entering Prometheus getData method");
  	RestTemplate restTemplate = new RestTemplate();
  	String responseData = restTemplate.getForObject("http://"+req.getServerName()+":"+req.getServerPort()+"/prometheus", String.class);
  	StringTokenizer strToken =  new StringTokenizer(responseData,"\n");
  	Set<String> strSet = new LinkedHashSet<String>();
  	while(strToken.hasMoreTokens()){
     		StringBuilder str = new StringBuilder(strToken.nextToken());
     		strSet.add(str.toString());
  	}
        
  	StringBuilder strData = new StringBuilder();
  	for(String str : strSet){
     	    strData.append(str);
     	    strData.append("\n");
  	}
        return new ResponseEntity<>(strData.toString(), HttpStatus.OK);
 }
	
}
