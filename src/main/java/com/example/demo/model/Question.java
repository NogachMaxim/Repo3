package com.example.demo.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "question")
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "QUESTION_ID")
	private Long id;

	@Column(name = "STRQUESTION")
	private String strQuestion;

	@ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "TEST_ID", nullable=true)
	private Test test;

	@OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
	private List<Answer> answers;
	
	public Question() {

	}

	public Question(String strQuestion) {
		super();
		this.strQuestion = strQuestion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStrQuestion() {
		return strQuestion;
	}

	public void setStrQuestion(String strQuestion) {
		this.strQuestion = strQuestion;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

}
