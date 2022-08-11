package com.example.demo.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "results")
public class Result {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RESULT_ID")
	private Long id;

	@Column(name = "PROCENT")
	private Integer procent;

	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "LEARNER_ID")
	private Learner learner;

	@ManyToOne(optional = true, fetch=FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "TEST_ID", nullable=true)
	private Test test;

	public Result() {

	}

	public Result(Integer procent) {
		super();
		this.procent = procent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getProcent() {
		return procent;
	}

	public void setProcent(Integer procent) {
		this.procent = procent;
	}

	public Learner getLearner() {
		return learner;
	}

	public void setLearner(Learner learner) {
		this.learner = learner;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

}
