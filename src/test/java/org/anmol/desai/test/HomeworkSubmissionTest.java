package org.anmol.desai.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.anmol.desai.domain.Answer;
import org.anmol.desai.domain.Homework;
import org.anmol.desai.domain.Student;
import org.anmol.desai.domain.Teacher;
import org.anmol.desai.domain.User;
import org.anmol.desai.service.FactoryAndDbInitialisation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HomeworkSubmissionTest  {

	private Logger _logger = LoggerFactory.getLogger(HomeworkSubmissionTest.class);

	private static final String WEB_SERVICE_URI = "http://localhost:10000/services/hwsubmission";

	private static Client _client;

	private Long id;

	/**
	 * One-time setup method that creates a Web service client.
	 */
	@BeforeClass
	public static void setUpClient() {
		_client = ClientBuilder.newClient();
	}


	// apprentely this code below causes an exception...for SOME GODDAMN REASON
	/**
	 * Runs before each unit test restore Web service database. This ensures
	 * that each test is independent; each test runs on a Web service that has
	 * been initialised with a common set of Parolees.

	@Before
	public void reloadServerData() {
		Response response = _client
				.target(WEB_SERVICE_URI).request()
				.put(null);
		response.close();

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
	}

	 **/

	/**
	 * One-time finalisation method that destroys the Web service client.
	 */

	@AfterClass
	public static void destroyClient() {
		_client.close();
	}

	/**
	 * This test is most basic. It is adding users to the database. It ALSO tests GET request of a USER.
	 * Because the generator changes the id every time, a post request has to be done, followed by the needed get request
	 * to ensure that correct behaviour ensues when a get request is made.
	 */
	@Test
	public void postAndQueryUser(){

		String firstName = "Darth";
		String lastName = "Vader";
		String type = "Student";

		// first add a student and then a teacher.
		org.anmol.desai.dto.User student = new org.anmol.desai.dto.User(firstName, lastName, type);

		// get a response object that has the result of doing a "POST" method.
		Response response = _client
				.target(WEB_SERVICE_URI +"/Users").request()
				.post(Entity.xml(student));

		// if response is successful in posting then a 201 is sent back, else there is an error.
		if (response.getStatus() != 201) {
			fail("Failed to create new Student");
		}

		// get the location string from the response
		String location = response.getLocation().toString();

		response.close();


		//_logger.info(location);
		location = WEB_SERVICE_URI + "" +  location.substring(31);
		_logger.info("The uri to send to check if user was created is " + location);

		org.anmol.desai.dto.User receivedUser = null;

		receivedUser = _client.target(location).request().get(org.anmol.desai.dto.User.class);

		//_logger.info("name of newly created user is " + receivedUser.getFirstNameUserDto() + " " + receivedUser.getLastNameUserDto());


		assertEquals(student.getFirstNameUserDto(), receivedUser.getFirstNameUserDto());
		assertEquals(student.getLastNameUserDto(), receivedUser.getLastNameUserDto());
		assertEquals(student.getTypeUserDto(), receivedUser.getTypeUserDto());

		_logger.info("All tests passed. Name and type are the same as created");
	}




	/**
	 * This tests sends an dto of Answer to the server and receives a dto. Then the content of the answer is printed out.
	 * @throws SQLException 
	 */
	@Test
	public void postAndQueryAnswer(){

		_logger.info("Make a answer and post it so it can be queried. Answer consists of string, user, homework");

		String firstName = "Ian";
		String lastName = "Warren";
		String type = "Teacher";

		// first add a student and then a teacher.
		org.anmol.desai.dto.User teacher = new org.anmol.desai.dto.User(firstName, lastName, type);

		String title = "Assignment 2";
		String question = "What course is this?";
		java.util.Date duedate = new java.util.Date();

		// make a dto homework
		org.anmol.desai.dto.Homework hw = new org.anmol.desai.dto.Homework( title,  question, duedate);

		String body = "this is 325";

		org.anmol.desai.dto.Answer answer = new org.anmol.desai.dto.Answer(body, teacher, hw);

		// get a response object that has the result of doing a "POST" method.
		Response response = _client
				.target(WEB_SERVICE_URI +"/Answers").request()
				.post(Entity.xml(answer));

		// if response is successful in posting then a 201 is sent back, else there is an error.
		if (response.getStatus() != 201) {
			fail("Failed to create new Answer");
		}

		// get the location string from the response
		String location = response.getLocation().toString();

		response.close();


		//_logger.info(location);
		location = WEB_SERVICE_URI + "" +  location.substring(31);
		_logger.info("The uri to send to check if user was created is " + location);



		org.anmol.desai.dto.Answer receivedAnswer = _client.target(location).request().accept("application/xml").get(org.anmol.desai.dto.Answer.class);
		
		_logger.info("before " + duedate);
		_logger.info("after " + receivedAnswer.getHw().getDuedate());
		
		assertEquals(answer.getBody(),receivedAnswer.getBody());
		
		assertEquals(answer.getUser().getFirstNameUserDto(),receivedAnswer.getUser().getFirstNameUserDto());
		assertEquals(answer.getUser().getLastNameUserDto(),receivedAnswer.getUser().getLastNameUserDto());
		assertEquals(answer.getUser().getTypeUserDto(),receivedAnswer.getUser().getTypeUserDto());
		
		assertEquals(answer.getHw().getTitle(),receivedAnswer.getHw().getTitle());
		assertEquals(answer.getHw().getQuestion(),receivedAnswer.getHw().getQuestion());
		assertEquals(answer.getHw().getDuedate().getTime(),receivedAnswer.getHw().getDuedate().getTime());
		
		_logger.info("All tests passed. Body, User and Homework are the same");
	}





}


/*
@Test
public void persistEntitiesToDb() {
	_entityManager.getTransaction().begin();

	User student = new Student("Anmol","Desai");


	_logger.info("The student is persisted ");

	User teacher = new Teacher("Bapa","Bapa");

	_logger.info("The teacher is persisted ");

	java.util.Date d = new java.util.Date();
	Homework hw = new Homework("title","year",d);

	// each student should be assigned a hw
	student.addHomework(hw);

	// each homework has a list of assigned students
	hw.addUser(student);

	Answer a = new Answer("content",student,hw);
	student.addAnswer(a);

	_entityManager.persist(student);
	_entityManager.persist(teacher);
	_entityManager.persist(hw);




	_entityManager.getTransaction().commit();


}
 */

/*
 * This is a test that gets the collection from the user and then returns the answer body.
 * 
int size = student.getAnswers().size();

_logger.info("size is " + size);

for(Answer a2: student.getAnswers() ){
	_logger.info("answer is " + a2.getBody());
}

 */

/*
int size = hw.getUsersAssigned().size();

_logger.info("size is " + size);

for(User u: hw.getUsersAssigned() ){
	_logger.info("answer is " + u.getFirstName());
}

 */
//_entityManager.persist(a);
