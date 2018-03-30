# SpringBoot_BatchProcessing
SpringBoot_BatchProcessing
/**
* 
* reader() ->  creates ItemReader which looks for sample-data.csv file and parse each item to transform information into User object
* processer() ->  creates instance of our UserProcessor 
* writer() -> create itemWriter which is aimed to JDBC Destination and gets dataSource created by @EnableBatchProcessing it also includes 		SQL statement needed to insert User to db.
* csvToDbJob() ->  defines Job. Jobs are built from Steps each job involve reader, processor and writer.
*		 In this Job Defination we need incrementer because jobs use a database to maintain execution state.You then list each 			step, of which 	this job has only one step.
 * The job ends, and the Java API produces a perfectly configured job. 

  
 * csvToDbStep() -> In the step definition, you define how much data to write at a time.
 * In this case, it writes up to ten records at a time.
 * Next, you configure the reader, processor, and writer using the injected bits from earlier.
 
 
 * chunk() is prefixed <User,User> because it’s a generic method.
 * This represents the input and output types of each "chunk" of processing, and lines up with ItemReader<User> and ItemWriter<User>.
	 * JobCompletionNotificationListener ->  listens for when a job is BatchStatus.COMPLETED, and then uses JdbcTemplate to inspect the results.
	 * @Configuration tags the class as a source of bean definitions for the application context
	 * @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
	 * Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath.
	 * This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet
	 * 
	 * @ComponentScan tells Spring to look for other components, configurations, and services in the which package, allowing it to find the controllers.
	 */
