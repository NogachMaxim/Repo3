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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "learners")
public class Learner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LEARNER_ID")
	private Long id;

	@Column(name = "FIRSTNAME")
	private String surname;

	@Column(name = "NAME")
	private String name;

	@Column(name = "LASTNAME")
	private String patronymic;
	
	@Column(name = "PASSWORDL")
	private String password;

	@ManyToOne(optional = true, fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "TEACHER_ID", nullable=true)
	private Teacher teacher;

	@OneToMany(mappedBy = "learner", fetch = FetchType.LAZY)
	private Collection<Result> results;

	@OneToMany(mappedBy = "learner", fetch = FetchType.LAZY)
	private Collection<Message> messages;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "learners_materials", joinColumns = @JoinColumn(name = "LEARNER___ID", referencedColumnName = "LEARNER_ID"), inverseJoinColumns = @JoinColumn(name = "MATERIAL___ID", referencedColumnName = "MATERIAL_ID"))
	private Collection<Material> materials;

	public Learner() {

	}

	public Learner(String surname, String name, String patronymic, String password) {
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

	public Collection<Message> getMessages() {
		return messages;
	}

	public void setMessages(Collection<Message> messages) {
		this.messages = messages;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Collection<Result> getResults() {
		return results;
	}

	public void setResults(Collection<Result> results) {
		this.results = results;
	}

	public Collection<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(Collection<Material> materials) {
		this.materials = materials;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
