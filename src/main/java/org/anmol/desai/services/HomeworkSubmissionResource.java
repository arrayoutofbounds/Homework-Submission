package org.anmol.desai.services;

import java.io.InputStream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.anmol.desai.domain.Answer;
import org.anmol.desai.test.JpaTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/hwsubmission")
public class HomeworkSubmissionResource {

	private static final Logger _logger = LoggerFactory.getLogger(HomeworkSubmissionResource.class);
	
	//static EntityManagerFactory emf = Persistence.createEntityManagerFactory("homeworkPU");
	//static EntityManager em = emf.createEntityManager();
	
	
	@GET
	@Path("/Answer/{id}")
	@Produces("application/xml")
	public org.anmol.desai.dto.Answer getAnswer(
			@PathParam("id") long id) {
		// Get the full Parolee object from the database.
		 EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		
		em.getTransaction().begin();
		
		// get the answer from the database
		Answer answer = em.find(Answer.class,id);
		
		if(answer == null){
			_logger.info("answer is null");
		}
		
		// convert domain found to dto and return it
		org.anmol.desai.dto.Answer dtoAnswer = AnswerMapper.toDto(answer);

		
		_logger.info("id is " + dtoAnswer.get_id());

		em.getTransaction().commit();
		
		em.close();

		return dtoAnswer;
	}
	

}
