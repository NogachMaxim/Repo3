package com.example.demo.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "materials")
public class Material {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MATERIAL_ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "MATERIALTEXT")
	private String materialText;

	@ManyToMany(mappedBy = "materials")
	private Collection<Learner> learners;
	
	public Material() {

	}

	public Material(String name, String materialText) {
		super();
		this.name = name;
		this.materialText = materialText;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMaterialText() {
		return materialText;
	}

	public void setMaterialText(String materialText) {
		this.materialText = materialText;
	}

	public Collection<Learner> getLearners() {
		return learners;
	}

	public void setLearners(Collection<Learner> learners) {
		this.learners = learners;
	}

	
}
