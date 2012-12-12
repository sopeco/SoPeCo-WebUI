package org.sopeco.frontend.server.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Test implements Serializable {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "stringName")
	private String name;

	public Test() {
	}

	public Test(String name) {
		this.name = name;
		this.id = name;
	}

}
