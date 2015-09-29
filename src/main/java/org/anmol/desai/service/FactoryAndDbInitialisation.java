package org.anmol.desai.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.anmol.desai.domain.Answer;
import org.anmol.desai.domain.Homework;
import org.anmol.desai.domain.Student;
import org.anmol.desai.domain.Teacher;
import org.anmol.desai.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactoryAndDbInitialisation {
	
	private static Logger _logger = null;


	private static FactoryAndDbInitialisation singleton = null;

	private static EntityManagerFactory emf = null;

	// JDBC connection to the database.
	private static Connection _jdbcConnection = null;

	/* A private Constructor prevents any other 
	 * class from instantiating.
	 */
	private FactoryAndDbInitialisation(){
		
		_logger = LoggerFactory.getLogger(FactoryAndDbInitialisation.class);

		try {
			initialiseDatabase();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		EntityManager _entityManager = emf.createEntityManager();
		_entityManager.getTransaction().begin();


		User student = new Student("Anmol","Desaiiiiiiiiii");

		User teacher = new Teacher("Bapa","Bapa");
		
		User teacher2 = new Teacher("a","a");

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
		_entityManager.persist(teacher2);
		_entityManager.persist(hw);

		_entityManager.getTransaction().commit();

		_entityManager.close();
		
	

	}

	public static void initialiseDatabase() throws ClassNotFoundException,
	SQLException {

		// Load H2 database driver.
		Class.forName("org.h2.Driver");

		// Open a connection to the database. This is used solely to delete rows
		// from any database tables and to drop the tables.
		_jdbcConnection = DriverManager.getConnection(
				"jdbc:h2:~/test;mv_store=false;MVCC=TRUE", "sa", "sa");


		// Drop any existing tables.
		clearDatabase(true);

		// Create the JPA EntityManagerFactory.
		emf = Persistence.createEntityManagerFactory("homeworkPU");
	}
	
	protected static void clearDatabase(boolean dropTables) throws SQLException {
		Statement s = _jdbcConnection.createStatement();
		s.execute("SET REFERENTIAL_INTEGRITY FALSE");

		Set<String> tables = new HashSet<String>();
		ResultSet rs = s.executeQuery("select table_name "
				+ "from INFORMATION_SCHEMA.tables "
				+ "where table_type='TABLE' and table_schema='PUBLIC'");

		while (rs.next()) {
			tables.add(rs.getString(1));
		}
		rs.close();
		for (String table : tables) {
			
			s.executeUpdate("DELETE FROM " + table);
			if (dropTables) {
				s.executeUpdate("DROP TABLE " + table);
			}
		}

		s.execute("SET REFERENTIAL_INTEGRITY TRUE");
		s.close();
	}

	public EntityManagerFactory getFactory(){
		return emf;
	}

	/* Static 'instance' method */
	public static FactoryAndDbInitialisation getInstance( ) {

		if(singleton == null){
			singleton = new FactoryAndDbInitialisation(); // lazy instanstiation
		}

		return singleton;
	}
	


}
