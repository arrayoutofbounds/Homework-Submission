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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
	
	/**
	 * Get all users in a list of dtos
	 * @return
	 */
	@GET
	@Path("/Users")
	@Consumes("application/xml")
	@Produces("application/xml")
	public List<org.anmol.desai.dto.User> getUsers(@QueryParam("typeOfUser") int typeOfUser, @QueryParam("start") int start, @QueryParam("size")int size ){
		
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
		
		
		
		if(typeOfUser==1){
			
			List<org.anmol.desai.dto.User> e = new ArrayList<org.anmol.desai.dto.User>();
			
			for(org.anmol.desai.dto.User u : usersReturned){
				if(u.getTypeUserDto().equals("Student")){
					e.add(u);
				}
			}
			return e;
		}
		
		if(typeOfUser==2){
			
			List<org.anmol.desai.dto.User> e = new ArrayList<org.anmol.desai.dto.User>();
			
			for(org.anmol.desai.dto.User u : usersReturned){
				if(u.getTypeUserDto().equals("Teacher")){
					e.add(u);
				}
			}
			return e;
		}
		
		// make size > 0 because if not assigned, it automatically becomes 0 and then causes problems
		if(start >= 0 && size > 0){
			if(start + size > usersReturned.size()){
				return new ArrayList<org.anmol.desai.dto.User>();
			}
			
			return usersReturned.subList(start, start + size);
		}
		
		// will only return this if all the if statements above fail or if the clien acutally wants all the users without filtering.
		return usersReturned;	
	}
	
	/**
	 * Create a new user
	 * @param dtoUser
	 * @return
	 */
	@POST
	@Path("/Users")
	@Consumes("application/xml")
	public Response createUser(org.anmol.desai.dto.User dtoUser, @HeaderParam("passwd") int value){

		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();
		
		_logger.info("hashed value of password is " + value);
		
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
	
	/**
	 * Update a user
	 * @param dtoUser
	 * @return
	 */
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
	
	
	/**
	 * Delete a user from the database
	 * @param id
	 */
	@DELETE
	@Path("/Users/{id}")
	public void deleteUser(@PathParam("id") long id){
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();
		
		_logger.info("Find the user to delete");
		org.anmol.desai.domain.User userToDelete = em.find(org.anmol.desai.domain.User.class, id);
		
		em.remove(userToDelete);
		
		em.getTransaction().commit();

		em.close();
	}
	
	
	// all the test for answers are after this
	
	
	/**
	 * Post to the list of answers for a user
	 * @param dtoAnswer
	 * @return
	 */
	@POST
	@Path("/Users/{id}/Answers")
	@Consumes("application/xml")
	public Response addAnswerToUser(@PathParam("id") long id, org.anmol.desai.dto.Answer dtoAnswer){
		
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();
		
		//get the user from the id given
		org.anmol.desai.domain.User user = em.find(org.anmol.desai.domain.User.class,id);
		
		// add answer to the user
		user.addAnswer(AnswerMapper.toDomainModel(dtoAnswer));
		
		// user already exists
		em.persist(user);
		
		em.getTransaction().commit();

		em.close();
		
		return Response.status(201).build();
		
	}
	
	/**
	 * This method creates an answer and returns a location string that has uri of that new answer.
	 * @param dtoAnswer
	 * @return
	 */
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
	
	
	
	
	
	/**
	 * This method returns all answers 
	 * @return
	 */
	@GET
	@Path("/Answers")
	@Produces("application/xml")
	public List<org.anmol.desai.dto.Answer> getAllAnswers(@Context UriInfo uriInfo){
		
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();
		
		_logger.info("Create list of domain objects and another list for dto to be returned");
		List<org.anmol.desai.domain.Answer> allAnswers = new ArrayList<org.anmol.desai.domain.Answer>();
		
		List<org.anmol.desai.dto.Answer> answersReturned = new ArrayList<org.anmol.desai.dto.Answer>();
		
		allAnswers = em.createQuery("select u FROM Answer u").getResultList(); // for table name, user the name of the domain class.
		
		em.getTransaction().commit();

		em.close();
		
		if(allAnswers == null){
			_logger.info("No answers are in the database");
		}else{
			for(org.anmol.desai.domain.Answer a : allAnswers){
				answersReturned.add(AnswerMapper.toDto(a));
			}
		}
		
		for(org.anmol.desai.dto.Answer dtoAnswer : answersReturned){
			dtoAnswer.addLink(getUriForUser(uriInfo,dtoAnswer), "User");
			dtoAnswer.addLink(getUriForSelf(uriInfo,dtoAnswer),"self");
		}
		return answersReturned;		
	}
	
	private String getUriForUser(UriInfo uriInfo, org.anmol.desai.dto.Answer a){
		URI uri = uriInfo.getBaseUriBuilder().path(HomeworkSubmissionResource.class).path(HomeworkSubmissionResource.class, "getUser").resolveTemplate("id", a.getHw().get_id()).build();
		return uri.toString();
	}
	
	/**
	 * This method returns one answer for the given id in uri.
	 * @param id
	 * @return
	 */
	@GET
	@Path("/Answers/{id}")
	@Produces("application/xml")
	public org.anmol.desai.dto.Answer getAnswer(
			@PathParam("id") long id, @Context UriInfo uriInfo) {
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
		
		// add link of itself
		dtoAnswer.addLink(getUriForSelf(uriInfo,dtoAnswer),"self");
		

		return dtoAnswer;
	}
	
	private String getUriForSelf(UriInfo uriInfo, org.anmol.desai.dto.Answer a){
		URI uri = uriInfo.getBaseUriBuilder().path(HomeworkSubmissionResource.class).path(HomeworkSubmissionResource.class, "getAnswer").resolveTemplate("id", a.get_id()).build();
		return uri.toString();
	}
	
	/**
	 * This method deletes a answer
	 * @param id
	 */
	@DELETE
	@Path("/Answers/{id}")
	public void deleteAnswer(@PathParam("id") long id){
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();

		em.getTransaction().begin();
		
		org.anmol.desai.domain.Answer answer = em.find(org.anmol.desai.domain.Answer.class,id);
		_logger.info("Answer to delete is " + answer.get_id());
		
		em.remove(answer);
		em.getTransaction().commit();
		em.close();
		
	}
	
	
	@PUT
	@Path("/Answers/{id}")
	@Consumes("application/xml")
	@Produces("application/xml")
	public Response updateAnswer(org.anmol.desai.dto.Answer dtoAnswer ){
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();
		
		org.anmol.desai.domain.Answer answer = em.find(org.anmol.desai.domain.Answer.class, dtoAnswer.get_id());
		
		answer.setBody(dtoAnswer.getBody());
		
		em.merge(answer);
		em.getTransaction().commit();
		em.close();
		
		return Response.ok(AnswerMapper.toDto(answer)).build();
	}
	
	
	/**
	 * The tests below a
	 */
	
	
	
	/**
	 * The code under here will have the tests for the hw
	 */
	
	/**
	 * Get list of homeworks that a user has
	 * @param id
	 * @return
	 */
	@GET
	@Path("/Users/{id}/Homeworks")
	@Produces("application/xml")
	public List<org.anmol.desai.dto.Homework> getAllHomworksForUser(@PathParam("id") long id){
		// Get the full Parolee object from the database.
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();
		
		org.anmol.desai.domain.User user = em.find(org.anmol.desai.domain.User.class, id);
		
		List<org.anmol.desai.domain.Homework> listHw = user.getHomeworkAssigned();
		
		List<org.anmol.desai.dto.Homework> returningHw = new ArrayList<org.anmol.desai.dto.Homework>();
		
		for(org.anmol.desai.domain.Homework homework : listHw){
			returningHw.add(HomeworkMapper.toDto(homework));
		}
		
		em.getTransaction().commit();

		em.close();
		
		return returningHw;
		
	}
	
	
	@POST
	@Path("Users/{id}/Homeworks")
	@Consumes("application/xml")
	public Response assignHomeworkToUser(@PathParam("id") long id, org.anmol.desai.dto.Homework dtoHomework){
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();
		
		org.anmol.desai.domain.User user = em.find(org.anmol.desai.domain.User.class, id);
		
		if(user.getHomeworkAssigned().contains(HomeworkMapper.toDomainModel(dtoHomework))){
			_logger.info("This homework has already been assigned to the chosen user");
		}else{
			user.addHomework(HomeworkMapper.toDomainModel(dtoHomework));
		}
		
		em.persist(user);
		em.getTransaction().commit();

		em.close();
		
		return Response.status(201).build();
	}
	
	/**
	 * Create homwork. Homeowork can be created alone and does not need any user or answer
	 * @return
	 */
	@POST
	@Path("/Homeworks")
	@Consumes("application/xml")
	public Response createHomework(org.anmol.desai.dto.Homework dtoHomework){
		
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();
		
		
		org.anmol.desai.domain.Homework homework = HomeworkMapper.toDomainModel(dtoHomework);
		
		_logger.info("Title is " + homework.getTitle());
		_logger.info("Question is " + homework.getQuestion());
		_logger.info("Due date is " + homework.getDuedate());
		
		em.persist(homework);
		em.getTransaction().commit();

		em.close();
		
		return Response.created(URI.create("/Homeworks/" + homework.get_id())).build();
		
		
	}
	
	
	@GET
	@Path("/Homeworks/{id}")
	@Produces("application/xml")
	public org.anmol.desai.dto.Homework getHomework(@PathParam("id") long id){
		
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();
		
		org.anmol.desai.domain.Homework hw = em.find(org.anmol.desai.domain.Homework.class, id);
		
		
		if(hw == null){
			_logger.info("Homework retrieved is null");
		}
		
		org.anmol.desai.dto.Homework dtoHw = HomeworkMapper.toDto(hw);
		
		_logger.info("THe id of the homework returned is " + dtoHw.get_id());
		
		em.getTransaction().commit();

		em.close();
		
		return dtoHw;	
	}
		
	
	@GET
	@Path("/Homeworks")
	@Produces("application/xml")
	public List<org.anmol.desai.dto.Homework> getAllHomeworks(){
		
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();

		em.getTransaction().begin();
		
		// create list
		List<org.anmol.desai.domain.Homework> allHw = new ArrayList<org.anmol.desai.domain.Homework>();
		
		List<org.anmol.desai.dto.Homework> hwReturned = new ArrayList<org.anmol.desai.dto.Homework>();
		
		allHw = em.createQuery("select u FROM Homework u").getResultList(); // for table name, user the name of the domain class.
		
		em.getTransaction().commit();

		em.close();
		
		if(allHw == null){
			_logger.info("No users are in the database");
		}else{
			for(org.anmol.desai.domain.Homework hw : allHw){
				hwReturned.add(HomeworkMapper.toDto(hw));
			}
		}
		
		return hwReturned;	
	}
	
	
	/**
	 * Update a Homework
	 * @param dtoHomework
	 * @return
	 */
	@PUT
	@Path("/Homeworks/{id}")
	@Consumes("application/xml")
	@Produces("application/xml")
	public Response updateHomework(org.anmol.desai.dto.Homework dtoHomework){
		
		EntityManager em = FactoryAndDbInitialisation.getInstance().getFactory().createEntityManager();
		em.getTransaction().begin();
		
		// pass in the table of the domain class you want to look in and the id .
		// User is a super class table, so just look in there with the id of the dto
		org.anmol.desai.domain.Homework hwToUpdate = em.find(org.anmol.desai.domain.Homework.class, dtoHomework.get_id());
		
		if(hwToUpdate == null){
			_logger.info("homework to update is null");
		}
		
		hwToUpdate.setQuestion(dtoHomework.getQuestion());
		hwToUpdate.setTitle(dtoHomework.getTitle());
		
		em.merge(hwToUpdate);
	
		em.getTransaction().commit();

		em.close();
		
		return Response.ok(HomeworkMapper.toDto(hwToUpdate)).build();
		
	}
	
}
