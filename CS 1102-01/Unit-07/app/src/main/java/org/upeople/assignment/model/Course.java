package org.upeople.assignment.model;

//Model class representing a Course entity
public class Course {
	private String courseId;
	private String courseName;

	// Constructor for creating a new course
	public Course(String courseId, String courseName) {
		this.courseId = courseId;
		this.courseName = courseName;
	}

	// Getters
	public String getCourseId() {
		return courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	@Override
	public String toString() {
		return courseId + " - " + courseName;
	}
}