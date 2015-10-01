package org.anmol.desai.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Date;
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


	// apparently this code below causes an exception...for SOME GODDAMN REASON
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
	 * Query all users and print list of users received to show result.
	 */
	@Test
	public void queryAllUsers(){
		List<org.anmol.desai.dto.User> users = _client
				.target(WEB_SERVICE_URI +"/Users").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.User>>() {
				});


		for(org.anmol.desai.dto.User u : users){
			_logger.info(u +"");
		}
	}

	@Test
	public void queryUsersWithPaginationAndFiltering(){

		List<org.anmol.desai.dto.User> users = _client
				.target(WEB_SERVICE_URI +"/Users?" + "typeOfUser=1").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.User>>() {
				});

		for(org.anmol.desai.dto.User u : users){
			_logger.info("Only returned student " + u.toString() );
		}


		List<org.anmol.desai.dto.User> users2 = _client
				.target(WEB_SERVICE_URI +"/Users?" + "typeOfUser=2").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.User>>() {
				});

		for(org.anmol.desai.dto.User u : users2){
			_logger.info("Only returned teacher " + u.toString() );
		}

		List<org.anmol.desai.dto.User> users3 = _client
				.target(WEB_SERVICE_URI +"/Users?" + "start=0&&size=3").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.User>>() {
				});

		if(users3.isEmpty()){
			_logger.info("Cannot match start and size index for existing list of users");
		}else{

			_logger.info("Start = 0 and Size = 3.");
			_logger.info("Result will be from index 0 to index 2 inclusive");
			for(org.anmol.desai.dto.User u : users3){
				_logger.info(u.toString() );
			}
		}

	}


	/**
	 * Get all users. Pick the first one in the list and update it via PUT.
	 * Then print out the updated and compare them to before updating.
	 */
	@Test
	public void updateUser(){

		List<org.anmol.desai.dto.User> users = _client
				.target(WEB_SERVICE_URI +"/Users").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.User>>() {
				});

		org.anmol.desai.dto.User u = users.get(0);

		_logger.info("Before update: ");
		_logger.info("First name is " + u.getFirstNameUserDto());
		_logger.info("First name is " + u.getLastNameUserDto());
		_logger.info("First name is " + u.getTypeUserDto());


		u.setFirstName("im");
		u.setLastName("awesome");

		Response response2 = _client.target(WEB_SERVICE_URI + "/Users/" + u.get_id_UserDto()).request().put(Entity.xml(u));

		if (response2.getStatus() != 200)
			fail("Failed to update User");

		org.anmol.desai.dto.User returnedUser = response2.readEntity(org.anmol.desai.dto.User.class);

		response2.close();

		_logger.info("After update: ");
		_logger.info("First name is " + returnedUser.getFirstNameUserDto());
		_logger.info("First name is " + returnedUser.getLastNameUserDto());
		_logger.info("First name is " + returnedUser.getTypeUserDto());

	}

	/**
	 * Make a user and persist it. Then go and delete that user.
	 */
	@Test
	public void deleteUser(){
		String firstName = "Jignesh";
		String lastName = "Desai";
		String type = "Teacher";

		// first add a student and then a teacher.
		org.anmol.desai.dto.User teacher = new org.anmol.desai.dto.User(firstName, lastName, type);

		// get a response object that has the result of doing a "POST" method.
		Response response = _client
				.target(WEB_SERVICE_URI +"/Users").request()
				.post(Entity.xml(teacher));

		// if response is successful in posting then a 201 is sent back, else there is an error.
		if (response.getStatus() != 201) {
			fail("Failed to create new User");
		}

		_logger.info("User was created successfully");

		// get the location string from the response
		String location = response.getLocation().toString();

		response.close();


		//_logger.info(location);
		location = WEB_SERVICE_URI + "" +  location.substring(31);

		_logger.info("The uri to send to check if user was created is " + location);

		Response r = _client.target(location).request().delete();

		if (r.getStatus() != 204) {
			fail("Failed to delete new User");
		}

		_logger.info("User was deleted successfully");

		r.close();

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
				.target(WEB_SERVICE_URI +"/Users").request().header("passwd",("value").hashCode())
				.post(Entity.xml(student));

		// if response is successful in posting then a 201 is sent back, else there is an error.
		if (response.getStatus() != 201) {
			fail("Failed to create new User");
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
	 * The tests below are for Answers
	 */

	/**
	 * This gets all answers
	 */
	@Test
	public void getAllAnswers(){

		List<org.anmol.desai.dto.Answer> answers = _client
				.target(WEB_SERVICE_URI +"/Answers").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.Answer>>() {
				});

		for(org.anmol.desai.dto.Answer a : answers){
			_logger.info(a.toString());
		}

	}


	/**
	 * This tests sends an dto of Answer to the server and receives a dto. Then the content of the answer is printed out.
	 * 
	 * GET and POST of answer is tested.
	 * 
	 * @throws SQLException 
	 */
	@Test
	public void postAndQueryAnswer(){

		//_logger.info("Make a answer and post it so it can be queried. Answer consists of string, user, homework");

		_logger.info("Make dto User");
		String firstName = "Ian";
		String lastName = "Warren";
		String type = "Teacher";

		// first add a student and then a teacher.
		org.anmol.desai.dto.User teacher = new org.anmol.desai.dto.User(firstName, lastName, type);

		_logger.info("Make dto Homework");
		String title = "Assignment 2";
		String question = "What course is this?";
		java.util.Date duedate = new java.util.Date();

		// make a dto homework
		org.anmol.desai.dto.Homework hw = new org.anmol.desai.dto.Homework( title,  question, duedate);

		_logger.info("Make dto Answer");
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

		_logger.info("Answer was posted successfully");

		// get the location string from the response
		String location = response.getLocation().toString();

		response.close();


		//_logger.info(location);
		location = WEB_SERVICE_URI + "" +  location.substring(31);
		_logger.info("The uri to send to check if Answer was created is " + location);



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

		_logger.info("All tests passed. Post and get of answer was successful");
	}


	/**
	 * This is about updating the answer
	 */
	@Test
	public void updateAnswer(){
		List<org.anmol.desai.dto.Answer> answers = _client
				.target(WEB_SERVICE_URI +"/Answers").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.Answer>>() {
				});

		org.anmol.desai.dto.Answer answer = answers.get(0);

		_logger.info("Before update " + answer.toString());

		answer.setBody("Changing this answer body");

		Response response = _client.target(WEB_SERVICE_URI + "/Answers/" + answer.get_id()).request()
				.put(Entity.xml(answer));

		if (response.getStatus() != 200)
			fail("Failed to update Answer");

		org.anmol.desai.dto.Answer returnedAnswer = response.readEntity(org.anmol.desai.dto.Answer.class);

		response.close();

		_logger.info("After update " + returnedAnswer.toString());

	}


	@Test
	public void deleteAnswer(){

		List<org.anmol.desai.dto.Answer> answers = _client
				.target(WEB_SERVICE_URI +"/Answers").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.Answer>>() {
				});

		if(answers.size() >= 1){

			org.anmol.desai.dto.Answer answer = answers.get(0);

			Response r = _client.target(WEB_SERVICE_URI + "/Answers/" + answer.get_id()).request().delete();

			if (r.getStatus() != 204) {
				fail("Failed to delete new User");
			}

			_logger.info(answer.toString() + " was deleted successfully");

			r.close();

		}else{
			_logger.info("The list is empty so there is nothing to delete");
		}


	}

	/**
	 * Following tests are all about getting, posting etc of homwork
	 */
	@Test
	public void getHomeworksForUser(){

		// get list of hw
		// then get the first one...who is a student

		List<org.anmol.desai.dto.User> users = _client
				.target(WEB_SERVICE_URI +"/Users").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.User>>() {
				});

		org.anmol.desai.dto.User u = users.get(0);

		List<org.anmol.desai.dto.Homework> dtoHomeworks = _client.target(WEB_SERVICE_URI + "/Users/" + u.get_id_UserDto() + "/Homeworks").request().accept("application/xml").get(new GenericType<List<org.anmol.desai.dto.Homework>>() {
		});

		_logger.info(u.getFirstNameUserDto() + " " + u.getLastNameUserDto() + " has " + dtoHomeworks.size() + " homeworks assigned to them");


	}

	/**
	 * POST and GET of homework is tested. Homework made does not have answer. This shows that it is independent.
	 */
	@Test
	public void postAndQueryHomework(){

		String title = "Assignment 4";
		String question = "What is Assignment 4?";
		java.util.Date duedate = new Date();

		org.anmol.desai.dto.Homework dtoHw = new org.anmol.desai.dto.Homework(title, question, duedate);

		Response response = _client
				.target(WEB_SERVICE_URI + "/Homeworks").request()
				.post(Entity.xml(dtoHw));
		if (response.getStatus() != 201) {
			fail("Failed to create new Homewprk");
		}

		String location = response.getLocation().toString();

		response.close();



		//_logger.info(location);
		location = WEB_SERVICE_URI + "" +  location.substring(31);
		_logger.info("The uri to send to check if Homework was created is " + location);


		org.anmol.desai.dto.Homework receivedHomework = _client.target(location).request().accept("application/xml").get(org.anmol.desai.dto.Homework.class);


		assertEquals(dtoHw.getTitle(),receivedHomework.getTitle());
		assertEquals(dtoHw.getQuestion(),receivedHomework.getQuestion());
		assertEquals(dtoHw.getDuedate().getTime(),receivedHomework.getDuedate().getTime());

		_logger.info("All tests passed. Post and get of homework was successful");


	}


	/**
	 * This test will update the homework
	 */
	@Test
	public void updateHomework(){

		// GET ALL HOMEWORKS IS USED
		List<org.anmol.desai.dto.Homework> hwks = _client
				.target(WEB_SERVICE_URI +"/Homeworks").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.Homework>>() {
				});

		org.anmol.desai.dto.Homework hw = hwks.get(0);

		_logger.info("Before:");
		_logger.info("" + hw.getTitle());
		_logger.info("" + hw.getQuestion());
		_logger.info("" + hw.getDuedate());

		hw.setQuestion("This question has been edited");
		hw.setTitle("This title has been edited");

		Response response = _client.target(WEB_SERVICE_URI + "/Homeworks/" + hw.get_id()).request().put(Entity.xml(hw));

		if (response.getStatus() != 200)
			fail("Failed to update Homework");

		org.anmol.desai.dto.Homework returnedHw = response.readEntity(org.anmol.desai.dto.Homework.class);

		response.close();

		_logger.info("After update: ");
		_logger.info("" + returnedHw.getQuestion());
		_logger.info("" + returnedHw.getTitle());
		_logger.info("" + returnedHw.getDuedate());


	}

	/**
	 * add homework to list of homeworks for a user.
	 */
	@Test
	public void assignHomeworkToUser(){


		List<org.anmol.desai.dto.User> users = _client
				.target(WEB_SERVICE_URI +"/Users").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.User>>() {
				});

		org.anmol.desai.dto.User user = users.get(0);

		List<org.anmol.desai.dto.Homework> hwks = _client
				.target(WEB_SERVICE_URI +"/Homeworks").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.Homework>>() {
				});

		org.anmol.desai.dto.Homework hw = hwks.get(0);


		Response response = _client.target(WEB_SERVICE_URI + "/Users/" + user.get_id_UserDto() +"/Homeworks").request()
				.post(Entity.xml(hw));

		if (response.getStatus() != 201)
			fail("Failed to assign Homework to user");

		response.close();

		_logger.info(user.toString() + " was assigned " + hw.toString());

	}

	@Test
	public void assignAnswerGivenToUser(){

		List<org.anmol.desai.dto.User> users = _client
				.target(WEB_SERVICE_URI +"/Users").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.User>>() {
				});

		org.anmol.desai.dto.User user = users.get(0);

		// get all the hwks that the user has assigned to itself already.
		List<org.anmol.desai.dto.Homework> hwks = _client
				.target(WEB_SERVICE_URI +"/Users/" + user.get_id_UserDto() + "/Homeworks").request()
				.accept("application/xml")
				.get(new GenericType<List<org.anmol.desai.dto.Homework>>() {
				});

		// assuming the first user has atleast one homework assigned to it
		org.anmol.desai.dto.Homework hw = hwks.get(0);

		String body = "This is the answer assigned";

		org.anmol.desai.dto.Answer a = new org.anmol.desai.dto.Answer(body, user, hw);

		Response response = _client.target(WEB_SERVICE_URI + "/Users/" + user.get_id_UserDto() +"/Answers").request()
				.post(Entity.xml(a));

		if (response.getStatus() != 201)
			fail("Failed to assign Answer to user");

		response.close();

		_logger.info(a.toString() + " was assigned " + user.toString() + " for " + hw.toString());

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
