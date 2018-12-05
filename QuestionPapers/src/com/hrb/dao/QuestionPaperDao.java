package com.hrb.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.stereotype.Repository;

import com.hrb.config.ApplicationConfiguration;
import com.hrb.models.Options;
import com.hrb.models.QuestionAnswer;
import com.hrb.models.QuestionPaper;


@Repository
public class QuestionPaperDao 
{
	Logger log = Logger.getLogger(QuestionPaperDao.class);
	public boolean addQuestion(QuestionAnswer questionAnswer, Options options, StringBuilder errorMsg)
	{
		boolean status=false;
		
		/*StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		  encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
		  encryptor.setPassword("admin");
		          HibernatePBEEncryptorRegistry registry = HibernatePBEEncryptorRegistry.getInstance();
		   registry.registerPBEStringEncryptor("configurationHibernateEncryptor", encryptor);*/
		
		Configuration configuration = new Configuration().configure().addAnnotatedClass(QuestionAnswer.class).addAnnotatedClass(Options.class);
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		Session session = sessionFactory.openSession();
		
		try
		{
			session.beginTransaction();
			Query query = session.createQuery(ApplicationConfiguration.db_addQuestion_query1);
			@SuppressWarnings("unchecked")
			List<String> questions = query.list();
			String questionPresent = questions.stream().filter(obj->obj.equalsIgnoreCase(questionAnswer.getQuestion())).findFirst().orElse(null);
			
			if(questionPresent==null)
			{
				session.save(options);
				session.save(questionAnswer);
				session.getTransaction().commit();
				status=true;
				log.info("Question and options added to the database");
			}
			else
			{
				log.info("Question already added");
				errorMsg.append("Question already added");
			}
			
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
			return status;
		}
		finally
		{
			session.close();
		}
		return status;
	}
	
	public void deleteQuestion(StringBuilder errorMsg3, StringBuilder successMsg3, String question)
	{
		Configuration configuration = new Configuration().configure().addAnnotatedClass(QuestionAnswer.class).addAnnotatedClass(Options.class);
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		Session session = sessionFactory.openSession();
		
		try
		{
			session.beginTransaction();
			Query query = session.createQuery(ApplicationConfiguration.db_deleteQuestion_query1);
			query.setParameter("index", question);
			Object obj = query.uniqueResult();
			
			if(obj==null)
			{
				log.info("Question not present");
				errorMsg3.append("Question not present");
			}
			else
			{
				int a = (int)obj;
				Query query2 = session.createQuery(ApplicationConfiguration.db_deleteQuestion_query2);
				query2.setParameter("index", a);
				QuestionAnswer questionAnswer = (QuestionAnswer)query2.uniqueResult();
				session.delete(questionAnswer);
				session.getTransaction().commit();
					
				successMsg3.append("Question \"" + question + "\" deleted");
				log.info("Question \"" + question + "\" deleted");
			}
		}
		catch (RuntimeException re) 
		{
			log.error(re.getMessage());
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	public List<QuestionPaper> generateQuestion(int count, StringBuilder errorMsg)
	{
		boolean flag=true;
		Random random = new Random();
		List<QuestionPaper> questionPaperList = new ArrayList<>();
		int i=0;
		List<String> options = null;
		QuestionPaper questionPresent = null;
		
		Configuration configuration = new Configuration().configure().addAnnotatedClass(QuestionAnswer.class).addAnnotatedClass(Options.class);
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		Session session = sessionFactory.openSession();
		
		try
		{
			Query query1 = session.createQuery(ApplicationConfiguration.db_generateQuestion_query1);
			@SuppressWarnings("unchecked")
			List<Integer> qids = query1.list();
			log.info("Retrieved the qids of QuestionAnswer table in the database");
			
			if(count>=qids.size())
			{
				log.info("Requested number of questions is larger than the number of questions available in the database. Please add more questions");
				errorMsg.append("Requested number of questions is larger than the number of questions available in the database. Please add more questions");
			}
			else
			{
				while(i<count)
				{
					Query query = session.createQuery(ApplicationConfiguration.db_generateQuestion_query2);
					query.setParameter("index", qids.get(random.nextInt(qids.size())));
					Object[] obj = (Object[])query.uniqueResult();
					QuestionPaper questionPaper = new QuestionPaper();
					options = new ArrayList<>();
						
					if(obj != null)
					{
						for(Object val : obj)
						{	
							if(val==null || val.toString().isEmpty())
							{
								log.info("Please remove the empty value field from the table");
							}
							else
							{
								if(flag)
								{
									questionPresent = questionPaperList.stream().filter(opt->opt.getQuestion().equals(val.toString())).findFirst().orElse(null);
								}
								
								if(questionPresent == null)
								{
									if(flag)
									{
										questionPaper.setQuestion(val.toString());
										log.info("Question " + "\"" + val.toString() + "\"" + " added to the list");
										flag=false;
										i++;
									}
									else if(!val.toString().isEmpty())
									{
										options.add(val.toString());
									}
								}
								else
								{
									log.info("Question " + "\"" + questionPresent.getQuestion() + "\"" + "already added to the list");
									break;
								}
							}
								
						}
						
						if(questionPresent == null)
						{
							flag=true;
							
							if((!options.isEmpty()) && (questionPaper.getQuestion()!=null))
							{
								questionPaper.setOptions(options);
								questionPaperList.add(questionPaper);	
							}
						}
					}
					else
					{
						log.info("Query returned null result");
					}
					
				}
			}
		}
		catch(HibernateException e)
		{
			log.error(e.getMessage());
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		finally
		{
			session.close();
		}
		return questionPaperList;
	}
}
