package org.anmol.desai.service;

import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.anmol.desai.domain.Answer;
import org.anmol.desai.domain.User;
import org.anmol.desai.test.JpaTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/hwsubmission")
public class HomeworkSubmissionResource {

	private static final Logger _logger = LoggerFactory.getLogger(HomeworkSubmissionResource.class);

	//static EntityManagerFactory emf = Persistence.createEntityManagerFactory("homeworkPU");
	//static EntityManager em = emf.createEntityManager();


	@POST
	@Path("/User")
	@Consumes("application/xml")
	public Response createUser(org.anmol.desai.dto.User dtoUser){

		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();

		// reading the user
		_logger.debug("Read User: " + dtoUser);

		org.anmol.desai.domain.User user = UserMapper.toDomainModel(dtoUser);


		em.persist(user);

		em.flush();

		if(em.contains(user)){
			_logger.info("IT IS IN DB");
		}

		org.anmol.desai.domain.User st = em.find(org.anmol.desai.domain.User.class, user.get_id());    


		_logger.info("Persisted User: " + dtoUser);

		//return UserMapper.toDto(user);
		
		em.getTransaction().commit();

		em.close();

		return Response.created(URI.create("/User/" + user.get_id())).build();


	}


	@GET
	@Path("/User/{id}")
	@Produces("application/xml")
	public org.anmol.desai.dto.User getUser(@PathParam("id") long id){
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();

		em.getTransaction().begin();
		// get the answer from the database
		org.anmol.desai.domain.User user = em.find(org.anmol.desai.domain.User.class,id);

		if(user == null){
			_logger.info("user is not in db" + " " + id);
		}

		// convert domain found to dto and return it
		org.anmol.desai.dto.User dtoUser = UserMapper.toDto(user);


		_logger.info("id is " + dtoUser.get_id_UserDto());

		em.getTransaction().commit();

		em.close();

		return dtoUser;
	}


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
