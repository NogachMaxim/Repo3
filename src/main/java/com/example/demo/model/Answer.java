package com.example.demo.model;

import java.util.Collection;

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
@Table(name = "answers")
public class Answer {
			
			@Id
			@GeneratedValue(strategy = GenerationType.IDENTITY)
			@Column(name = "ANSWER_ID")
			private Long id;
			
			@Column(name = "STRANSWER")
			private String strAnswer;

			@Column(name = "ISTRUE")
			private boolean isTrue;
			
			@ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
			@JoinColumn(name = "QUESTION_ID", nullable=true)
			private Question question;

			public Answer() {
				
			}

			public Answer(String strAnswer, boolean isTrue) {
				super();
				this.strAnswer = strAnswer;
				this.isTrue=isTrue;
			}

			public boolean getIsTrue() {
				return isTrue;
			}

			public void setIsTrue(boolean isTrue) {
				this.isTrue = isTrue;
			}

			public Long getId() {
				return id;
			}

			public void setId(Long id) {
				this.id = id;
			}

			public String getStrAnswer() {
				return strAnswer;
			}

			public void setStrAnswer(String strAnswer) {
				this.strAnswer = strAnswer;
			}

			public Question getQuestion() {
				return question;
			}

			public void setQuestion(Question question) {
				this.question = question;
			}
			
}
