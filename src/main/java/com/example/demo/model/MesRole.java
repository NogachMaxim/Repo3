package com.example.demo.model;

public class MesRole {

	private Message mes;
	private String nameRole;
	
	public MesRole() {
		super();
	}

	public MesRole(Message mes, String nameRole) {
		super();
		this.mes = mes;
		this.nameRole = nameRole;
	}

	public Message getMes() {
		return mes;
	}

	public void setMes(Message mes) {
		this.mes = mes;
	}

	public String getNameRole() {
		return nameRole;
	}

	public void setNameRole(String nameRole) {
		this.nameRole = nameRole;
	}

	
	
}
