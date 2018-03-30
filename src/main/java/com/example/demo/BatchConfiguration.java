package com.example.demo;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobFactory;
	
	@Autowired
	public StepBuilderFactory stepFactory;
	
	/**
	 * 
	 * reader() ->  creates ItemReader which looks for sample-data.csv file and parse each item to transform information into User object
	 * processer() ->  creates instance of our UserProcessor 
	 * writer() -> create itemWriter which is aimed to JDBC Destination and gets dataSource created by @EnableBatchProcessing it also includes SQL statement needed to insert User to db.
	 * 
	 * 
	 * csvToDbJob() ->  defines Job. Jobs are built from Steps each job involve reader, processor and writer.
	 * In this Job Defination we need incrementer because jobs use a database to maintain execution state.You then list each step, of which this job has only one step.
	 * The job ends, and the Java API produces a perfectly configured job. 
	 * 
	 * 
	 * csvToDbStep() -> In the step definition, you define how much data to write at a time.
	 * In this case, it writes up to ten records at a time.
	 * Next, you configure the reader, processor, and writer using the injected bits from earlier.
	 * 
	 * 
	 * chunk() is prefixed <User,User> because it’s a generic method.
	 * This represents the input and output types of each "chunk" of processing, and lines up with ItemReader<User> and ItemWriter<User>.
	 * 
	 * JobCompletionNotificationListener ->  listens for when a job is BatchStatus.COMPLETED, and then uses JdbcTemplate to inspect the results.
	 * 
	 * @Configuration tags the class as a source of bean definitions for the application context
	 * @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
	 * Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath.
	 * This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet
	 * 
	 * @ComponentScan tells Spring to look for other components, configurations, and services in the which package, allowing it to find the controllers.
	 */
	
	@Bean
	public FlatFileItemReader<User> reader() {
		return new FlatFileItemReaderBuilder<User>()
				.name("userProcessor")
				.resource(new ClassPathResource("sample-data.csv"))
				.delimited()
				.names(new String[]{"firstName","lastName"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
					setTargetType(User.class);
				}})
				.build();
	}
	
	@Bean
	public UserProcessor processer() {
		return new UserProcessor();
	}
	
	
	@Bean
	public JdbcBatchItemWriter<User> writer(DataSource dataSource) {
	    return new JdbcBatchItemWriterBuilder<User>()
	        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
	        .sql("INSERT INTO user (first_name, last_name) VALUES (:firstName, :lastName)")
	        .dataSource(dataSource)
	        .build();
	}
	 
	@Bean
    public Job csvToDbJob(JobCompletionNotificationListener notificationLister, Step csvToDbStep) {
        return jobFactory.get("csvToDbJob")
            .incrementer(new RunIdIncrementer())
            .listener(notificationLister)
            .flow(csvToDbStep)
            .end()
            .build();
    }
	
	@Bean
    public Step csvToDbStep(JdbcBatchItemWriter<User> writer) {
        return stepFactory.get("csvToDbStep")
            .<User, User> chunk(10)
            .reader(reader())
            .processor(processer())
            .writer(writer)
            .build();
    }
	
	

	    
}
