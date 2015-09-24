package org.anmol.desai.test;

import static org.junit.Assert.fail;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

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

		User student = new Student();
		student.setFirstName("Anmol");
		student.setLastName("Desai");


		_logger.info("The student is persisted ");

		User teacher = new Teacher();
		teacher.setFirstName("Bapa");
		teacher.setLastName("Bapa");

		_entityManager.persist(student);
		_entityManager.persist(teacher);

		_entityManager.getTransaction().commit();
		
	}

}
