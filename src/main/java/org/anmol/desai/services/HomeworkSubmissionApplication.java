package org.anmol.desai.services;

import java.util.HashSet;
import java.util.Set;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/services")
public class HomeworkSubmissionApplication extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public HomeworkSubmissionApplication()
	{	
		FactoryAndDbInitialisation resource = FactoryAndDbInitialisation.getInstance();
		singletons.add(resource);
		classes.add(HomeworkSubmissionResource.class);

		// need to add resolver to the classes set as well below
	}

	@Override
	public Set<Object> getSingletons()
	{
		return singletons;
	}

	@Override
	public Set<Class<?>> getClasses()
	{
		return classes;
	}

}
