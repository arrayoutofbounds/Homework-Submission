package org.anmol.desai.test;

import static org.junit.Assert.fail;

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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HomeworkSubmissionTest  {

	private Logger _logger = LoggerFactory.getLogger(HomeworkSubmissionTest.class);
	
	private static final String WEB_SERVICE_URI = "http://localhost:8080/services/hwsubmission";
	
	private static Client _client;

	/**
	 * One-time setup method that creates a Web service client.
	 */
	@BeforeClass
	public static void setUpClient() {
		_client = ClientBuilder.newClient();
	}
	
	/**
	 * Runs before each unit test restore Web service database. This ensures
	 * that each test is independent; each test runs on a Web service that has
	 * been initialised with a common set of Parolees.
	 */
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
	
	/**
	 * One-time finalisation method that destroys the Web service client.
	 */
	@AfterClass
	public static void destroyClient() {
		_client.close();
	}
	
	@Test
	public void addAnswer(){
		
	}
	
	
	/**
	 * This tests sends an dto of Answer to the server and receives a dto. Then the content of the answer is printed out.
	 */
	@Test
	public void queryAnswer(){
		
		
		org.anmol.desai.dto.Answer answer = _client.target(WEB_SERVICE_URI + "/Answer/1").request().accept("application/xml").get(org.anmol.desai.dto.Answer.class);
		
		
		_logger.info("Answer is " + answer.getBody());
		
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
