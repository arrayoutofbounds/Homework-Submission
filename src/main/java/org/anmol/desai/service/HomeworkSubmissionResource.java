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
import javax.ws.rs.PUT;
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
	
	/**
	 * GET user. This method gets the user from the specified ID in the uri.
	 * @param id
	 * @return
	 */
	@GET
	@Path("/Users/{id}")
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
	@Path("/Users")
	@Produces("application/xml")
	public List<org.anmol.desai.dto.User> getUsers(){
		
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();

		em.getTransaction().begin();
		
		// create list
		List<org.anmol.desai.domain.User> allUsers = new ArrayList<org.anmol.desai.domain.User>();
		
		List<org.anmol.desai.dto.User> usersReturned = new ArrayList<org.anmol.desai.dto.User>();
		
		allUsers = em.createQuery("select u FROM User u").getResultList(); // for table name, user the name of the domain class.
		
		em.getTransaction().commit();

		em.close();
		
		if(allUsers == null){
			_logger.info("No users are in the database");
		}else{
			for(org.anmol.desai.domain.User user : allUsers){
				usersReturned.add(UserMapper.toDto(user));
			}
		}
		
		return usersReturned;	
	}

	@POST
	@Path("/Users")
	@Consumes("application/xml")
	public Response createUser(org.anmol.desai.dto.User dtoUser){

		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();

		// reading the user
		_logger.info("Read User: " + dtoUser);
		_logger.info("type of user to be created is " + dtoUser.getTypeUserDto());

		org.anmol.desai.domain.User user = UserMapper.toDomainModel(dtoUser);

		
		em.persist(user);
		//em.flush(); // flush it so next statement can be put in

		if(em.contains(user)){
			_logger.info("IT IS IN DB");
		}

		//org.anmol.desai.domain.User st = em.find(org.anmol.desai.domain.User.class, user.get_id());    


		//_logger.info("Persisted User: " + dtoUser);

		//return UserMapper.toDto(user);
		
		em.getTransaction().commit();

		em.close();

		return Response.created(URI.create("/Users/" + user.get_id())).build();


	}
	
	@PUT
	@Path("/Users/{id}")
	@Consumes("application/xml")
	@Produces("application/xml")
	public Response updateUser(org.anmol.desai.dto.User dtoUser){
		
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();
		
		// pass in the table of the domain class you want to look in and the id .
		// User is a super class table, so just look in there with the id of the dto
		org.anmol.desai.domain.User userToUpdate = em.find(org.anmol.desai.domain.User.class, dtoUser.get_id_UserDto());
		
		if(userToUpdate == null){
			_logger.info("user to update is null");
		}
		
		userToUpdate.setFirstName(dtoUser.getFirstNameUserDto());
		userToUpdate.setLastName(dtoUser.getLastNameUserDto());
		
		em.merge(userToUpdate);
	
		em.getTransaction().commit();

		em.close();
		
		return Response.ok(UserMapper.toDto(userToUpdate)).build();
		
	}


	
	
	@POST
	@Path("/Answers")
	@Consumes("application/xml")
	public Response createAnswer(org.anmol.desai.dto.Answer dtoAnswer){

		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();

		// reading the user
		_logger.info("Read Answer: " + dtoAnswer);
		
		// make a domain model answer
		org.anmol.desai.domain.Answer answer = AnswerMapper.toDomainModel(dtoAnswer);
		
		_logger.info("date is " + answer.getHw().getDuedate());

		// persist in db
		em.persist(answer.getUser());
		em.persist(answer.getHw());
		em.persist(answer);

		if(em.contains(answer)){
			_logger.info("IT IS IN DB");
		}
		
		//org.anmol.desai.domain.Homework h = em.find(org.anmol.desai.domain.Homework.class, answer.getHw().get_id());
		
		//_logger.info("after persisted date is "  + h.getDuedate());

		em.getTransaction().commit();

		em.close();

		return Response.created(URI.create("/Answers/" + answer.get_id())).build();


	}
	


	@GET
	@Path("/Answers/{id}")
	@Produces("application/xml")
	public org.anmol.desai.dto.Answer getAnswer(
			@PathParam("id") long id) {
		// Get the full Parolee object from the database.
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();

		em.getTransaction().begin();


		
		// get the answer from the database
		org.anmol.desai.domain.Answer answer = em.find(Answer.class,id);

		if(answer == null){
			_logger.info("answer is null");
		}

		// convert domain found to dto and return it
		org.anmol.desai.dto.Answer dtoAnswer = AnswerMapper.toDto(answer);


		_logger.info("id is " + dtoAnswer.get_id());
		//_logger.info("hw duedate is " + dtoAnswer.getHw().getDuedate());

		em.getTransaction().commit();

		em.close();

		return dtoAnswer;
	}


}
