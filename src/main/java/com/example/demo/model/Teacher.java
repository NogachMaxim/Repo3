package com.example.demo.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "teachers")
public class Teacher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TEACHER_ID")
	private Long id;

	@Column(name = "FIRSTNAME")
	private String surname;

	@Column(name = "NAME")
	private String name;

	@Column(name = "LASTNAME")
	private String patronymic;

	@Column(name = "PASSWORDT")
	private String password;

	@OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
	private Collection<Learner> learners;

	public Teacher() {

	}

	public Teacher(String surname, String name, String patronymic, String password) {
		super();
		this.surname = surname;
		this.name = name;
		this.patronymic = patronymic;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public Collection<Learner> getLearners() {
		return learners;
	}

	public void setLearners(Collection<Learner> learners) {
		this.learners = learners;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
