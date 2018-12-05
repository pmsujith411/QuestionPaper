package com.hrb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ApplicationConfiguration 
{
	public static String outputFileName;
	public static String db_deleteQuestion_query1;
	public static String db_deleteQuestion_query2;
	public static String db_generateQuestion_query1;
	public static String db_generateQuestion_query2;
	public static String db_addQuestion_query1;
	
	static Logger log = Logger.getLogger(ApplicationConfiguration.class.getName());
	
	static
	{
		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("Application.properties");
		
		try 
		{
			properties.load(inputStream);
		} 
		catch (IOException e) 
		{
			log.error(e.getMessage());
		}
		
		outputFileName = properties.getProperty("outputFileName");
		db_deleteQuestion_query1 = properties.getProperty("db.deleteQuestion.query1");
		db_deleteQuestion_query2 = properties.getProperty("db.deleteQuestion.query2");
		db_generateQuestion_query1 = properties.getProperty("db.generateQuestion.query1");
		db_generateQuestion_query2 = properties.getProperty("db.generateQuestion.query2");
		db_addQuestion_query1 = properties.getProperty("db.addQuestion.query1");
		
	}
}
