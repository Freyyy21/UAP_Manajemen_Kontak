package com.uap.model;

import lombok.Getter;
import lombok.Setter;

// Lombok
@Setter
@Getter
public class User {
	//id
	private String id;
	private String username;
	private String password;

	public User() {}

	public User(String id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	
}