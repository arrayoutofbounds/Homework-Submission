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
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HomeworkSubmissionTest extends JpaTest {

	private Logger _logger = LoggerFactory.getLogger(HomeworkSubmissionTest.class);

	@Test
	public void persistUser() {
		_entityManager.getTransaction().begin();

		User student = new Student("Anmol","Desai");



		_logger.info("The student is persisted ");

		User teacher = new Teacher("Bapa","Bapa");
		
		java.util.Date d = new java.util.Date();
		Homework hw = new Homework("title","year",d);
		
		Answer a = new Answer("content",student,hw);
		

		_entityManager.persist(student);
		_entityManager.persist(teacher);
		_entityManager.persist(hw);
		_entityManager.persist(a);

		_entityManager.getTransaction().commit();
		
	}

}
