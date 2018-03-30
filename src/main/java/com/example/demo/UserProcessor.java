package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<User, User>{

	private static final Logger logger = LoggerFactory.getLogger(UserProcessor.class);
	/* 
	 * This class used for Process/transform Data  to object of our likes
	 * it is not always necessary that what we get in input is what we want to save we want to manipulate data than we can do with this class
	 * 
	 */
	@Override
	public User process(final User user) throws Exception {
		final String firstName = user.getFirstName();
		final String lastName = user.getLastName();
		
		final User transformedUser = new User(firstName, lastName);
		logger.info("Converting User "+ user +" into "+transformedUser);
		
		return transformedUser;
	}

}
