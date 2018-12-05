package com.hrb.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class QuestionAnswer 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qid", updatable = false, nullable = false)
	private int qid;
	@NotNull
	private String question;
	@NotNull
	private String answer;
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval = true)  
    @JoinColumn(name="option_id")
	private Options options;
	@Override
	public String toString() {
		return "QuestionAnswer [qid=" + qid + ", question=" + question + ", answer=" + answer + ", options=" + options
				+ "]";
	}
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Options getOptions() {
		return options;
	}
	public void setOptions(Options options) {
		this.options = options;
	}
}
