package com.hrb.services;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import com.hrb.dao.QuestionPaperDao;
import com.hrb.models.Options;
import com.hrb.models.QuestionAnswer;
import com.hrb.models.QuestionPaper;

@Service
public class QuestionPaperService 
{
	Logger log = Logger.getLogger(QuestionPaperService.class);
	
	public boolean questionAddition(QuestionAnswer questionAnswer, Options options, StringBuilder errorMsg)
	{	
		return new QuestionPaperDao().addQuestion(questionAnswer, options, errorMsg);
	}
	
	public void questionGenerator(int count, XWPFRun run, StringBuilder successMsg, StringBuilder errorMsg)
	{
		List<QuestionPaper> questionPapers =  new QuestionPaperDao().generateQuestion(count, errorMsg);
				
		String[] optionList = {"a) ", "		b) ", "		c) ", "		d) "};
		int oNum = 0;
		int qNum = 1;
		
		if(questionPapers.size()!=count)
		{
			log.info("unable to fetch required number of questions");
			
			if(errorMsg.length() == 0)
			{
				errorMsg.append("unable to fecth required number of questions");
			}
		}
		else
		{
			for(QuestionPaper questionPaper : questionPapers)
			{
				run.setText(qNum++ + ". " + questionPaper.getQuestion());
				run.addBreak();
				
				Collections.shuffle(questionPaper.getOptions());
				
				for(String option : questionPaper.getOptions())
				{
					run.setText("     ");
					run.setText(optionList[oNum++] + option);
					run.addBreak();
				}
				
				oNum = 0;
			}
		}	
	}
	
	public void questionDeletion(StringBuilder errorMsg3, StringBuilder successMsg3, String question)
	{
		new QuestionPaperDao().deleteQuestion(errorMsg3, successMsg3, question);
	}
}
