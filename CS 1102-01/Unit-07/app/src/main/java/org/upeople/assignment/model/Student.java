package org.upeople.assignment.model;

//Model class representing a Student entity
public class Student {
	private String id;
	private String name;

	// Constructor for creating a new student
	public Student(String id, String name) {
		this.id = id;
		this.name = name;
	}

	// Getters and setters
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return id + " - " + name;
	}
}