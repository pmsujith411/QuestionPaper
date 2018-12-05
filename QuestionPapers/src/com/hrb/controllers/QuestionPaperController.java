package com.hrb.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hrb.config.ApplicationConfiguration;
import com.hrb.models.Options;
import com.hrb.models.QuestionAnswer;
import com.hrb.services.QuestionPaperService;

@Controller
public class QuestionPaperController 
{
	Logger log = Logger.getLogger(QuestionPaperController.class);
	@RequestMapping("/")
	public ModelAndView home1Page() 
	{
		return new ModelAndView("homePage");
	}
	
	@RequestMapping("/addQuestion")
	public ModelAndView addQuestion(HttpServletRequest request)
	{
		boolean status=false;
		ModelAndView modelAndView=new ModelAndView();
		StringBuilder errorMsg = new StringBuilder();
		StringBuilder successMsg = new StringBuilder();
						
		Options options = new Options();
		
		if(request.getParameter("option1")==null && request.getParameter("option2")==null)
		{
			log.info("Please add atleast 2 Options");
		}
		else if(request.getParameter("question") == null)
		{
			log.info("Question field cannot be empty");
		}
		else
		{
			options.setOption1(request.getParameter("option1"));
			options.setOption2(request.getParameter("option2"));
			options.setOption3(request.getParameter("option3"));
			options.setOption4(request.getParameter("option4"));
			
			QuestionAnswer questionAnswer = new QuestionAnswer();
			questionAnswer.setQuestion(request.getParameter("question"));
			questionAnswer.setOptions(options);	
			
			switch(request.getParameter("answer"))
			{
				case "1":	questionAnswer.setAnswer(options.getOption1());
							break;
				case "2": 	questionAnswer.setAnswer(options.getOption2());
							break;
				case "3": 	questionAnswer.setAnswer(options.getOption3());
							break;
				case "4": 	questionAnswer.setAnswer(options.getOption4());
							break;
			}	
			
			status=new QuestionPaperService().questionAddition(questionAnswer, options, errorMsg);
		}
		
		if(status)
		{
			if(successMsg.length() == 0)
			{
				successMsg.append("Successfully added the Question");
			}
			
			log.info("Successfully added the Question");
		}
		else
		{
			if(errorMsg.length() == 0)
			{
				errorMsg.append("Unable to add the question");
			}
			
			log.info("Unable to add the question");
			
		}
			
		modelAndView.addObject("errorMsg", errorMsg);
		modelAndView.addObject("successMsg", successMsg);
		modelAndView.setViewName("homePage");
		return modelAndView;
	}
	
	@RequestMapping("/genQuestion")
	public ModelAndView generateQuestion(HttpServletRequest request, HttpServletResponse response)
	{
		StringBuilder successMsg2 = new StringBuilder();
		StringBuilder errorMsg2 = new StringBuilder();
		ModelAndView modelAndView = new ModelAndView();
		String regex = "[0-9]+";
			
		if(request.getParameter("count")==null)
		{
			log.info("Null value in input field");
			errorMsg2.append("Null value in input field");
		}
		else if((request.getParameter("count").matches(regex)) && (Integer.parseInt(request.getParameter("count")) != 0))
		{
			@SuppressWarnings("resource")
			XWPFDocument document = new XWPFDocument(); 
        
			String cwd = System.getProperty("user.dir");
			System.out.println("path 1 " + cwd);
			File currentDirFile = new File(".");
			String helper = currentDirFile.getAbsolutePath();
			System.out.println("path 2 " + helper);
			
			FileOutputStream out;
        
			try 
			{
				out = new FileOutputStream(new File(cwd + "//" + ApplicationConfiguration.outputFileName));
				
				XWPFParagraph paragraph2 = document.createParagraph();
				paragraph2.setAlignment(ParagraphAlignment.CENTER);
				XWPFRun run2 = paragraph2.createRun();
				run2.setFontFamily("Times New Roman");
				run2.setFontSize(16);
				run2.setBold(true);
				run2.setText("H&R Block Exam December 2018");
				
				XWPFParagraph paragraph3 = document.createParagraph();
				paragraph3.setBorderBottom(Borders.BASIC_BLACK_DASHES);
				paragraph3.setAlignment(ParagraphAlignment.RIGHT);
				XWPFRun run3 = paragraph3.createRun();
				run3.setFontFamily("Times New Roman");
				run3.setFontSize(10);
				run3.setText("Duration: 1Hour");
				run3.addBreak();
			
				XWPFParagraph paragraph = document.createParagraph();
				paragraph.setAlignment(ParagraphAlignment.LEFT);
				XWPFRun run = paragraph.createRun();
				run.setFontFamily("Times New Roman");
				run.setFontSize(12);
				
				new QuestionPaperService().questionGenerator(Integer.parseInt(request.getParameter("count")), run, successMsg2, errorMsg2);
					
				if(errorMsg2.length()==0)
				{
					document.write(out);
				    out.close();
				          
				    Path file = Paths.get(cwd, ApplicationConfiguration.outputFileName);
				    
				    if (Files.exists(file)) 
				    {
				    	response.setContentType("application/msword");
				    	response.addHeader("Content-Disposition", "attachment; filename=" + ApplicationConfiguration.outputFileName);
				    
				    	try 
				    	{
				    		Files.copy(file, response.getOutputStream());
				    		response.getOutputStream().flush();
				    		response.getOutputStream().close();
				    		
				    		log.info("successfully created the questionpaper with requested number of questions");
							successMsg2.append("successfully created the questionpaper with requested number of questions");
							errorMsg2.append("      ");
				    	} 
				    	catch (IOException ex) 
				    	{
				    		log.error(ex.getMessage());
				    	}
				    }
				    else
				    {
				    	log.info("unable to find the data file");
				    }		
				}
			} 
			catch (FileNotFoundException e1) 
			{
				log.error(e1.getMessage());
			} 
			catch (IOException e) 
			{
				log.error(e.getMessage());
			}			
		}
		else
		{
			log.info("Not a valida data in input field");
			errorMsg2.append("Please enter a valid data");
		}
		
		modelAndView.addObject("successMsg2", successMsg2.toString());
		modelAndView.addObject("errorMsg2", errorMsg2.toString());
		modelAndView.setViewName("homePage");
		return modelAndView;
	}
	
	@RequestMapping("/deleteQuestion")
	ModelAndView deleteQuestion(HttpServletRequest request)
	{
		ModelAndView modelAndView = new ModelAndView();
		StringBuilder successMsg3 = new StringBuilder();
		StringBuilder errorMsg3 = new StringBuilder();
		String question = request.getParameter("question");
		
		if(question == null || question.equals(""))
		{
			errorMsg3.append("Please enter the data");
			log.info("Input field is empty");
		}
		else
		{
			new QuestionPaperService().questionDeletion(errorMsg3, successMsg3, question);
		}
		
		modelAndView.addObject("errorMsg3", errorMsg3);
		modelAndView.addObject("successMsg3", successMsg3);
		modelAndView.setViewName("homePage");
		return modelAndView;
		
	}
}
