package com.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.batch.primary.Employee;
import com.batch.secondary.Manager;
import com.batch.service.MyCustomProcessor;
import com.batch.service.MyCustomReader;
import com.batch.service.MyCustomWriter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfig {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
		
	@Autowired
	MyCustomReader myCustomReader;
	
	@Autowired
	MyCustomWriter myCustomWriter;

	@Autowired
	MyCustomProcessor myCustomProcessor;
	
	@Bean
	public Job createJob() {
		log.info("createJob method called..");
		return jobBuilderFactory.get("MyJob")
				.incrementer(new RunIdIncrementer())
				.flow(createStep()).end().build();
	}

	@Bean
	public Step createStep() {
		log.info("createStep method called..");
		return stepBuilderFactory.get("MyStep")
				.<Employee, Manager> chunk(1)
				.reader(myCustomReader)
				.processor(myCustomProcessor)
				.writer(myCustomWriter)
				.build();
	}
}
