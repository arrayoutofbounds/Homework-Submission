package org.anmol.desai.services;

import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.anmol.desai.domain.Answer;

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
			_context = JAXBContext.newInstance(Answer.class);
		}catch(JAXBException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * in the if statement, check that it is one of the classes that is in the service
	 */
	@Override
	public JAXBContext getContext(Class<?> type) {
		if(type.equals(Answer.class)){
			return _context;
		}else{
			return null;
		}
	}

}
