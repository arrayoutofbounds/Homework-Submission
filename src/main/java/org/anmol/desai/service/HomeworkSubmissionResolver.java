package org.anmol.desai.service;

import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.anmol.desai.domain.Answer;
import org.anmol.desai.domain.Homework;
import org.anmol.desai.domain.Student;
import org.anmol.desai.domain.Teacher;
import org.anmol.desai.domain.User;

/**
 * 
 * context resolver does the unmarshalling and marshalling for the application objects
 * in the homework submission webservice
 * @author anmol
 *
 */
public class HomeworkSubmissionResolver implements ContextResolver<JAXBContext> {
	
	private JAXBContext _context;
	
	public HomeworkSubmissionResolver() {
		try{
			// list all the classes to be done marshalling and unmarshalling in a comma seperated way in _context after Answer.class
			_context = JAXBContext.newInstance(Answer.class,Homework.class,User.class,Student.class,Teacher.class);
		}catch(JAXBException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * in the if statement, check that it is one of the classes that is in the service
	 */
	@Override
	public JAXBContext getContext(Class<?> type) {
		if(type.equals(Answer.class) || type.equals(Homework.class) || type.equals(User.class) || type.equals(Student.class) || type.equals(Teacher.class)){
			return _context;
		}else{
			return null;
		}
	}

}
