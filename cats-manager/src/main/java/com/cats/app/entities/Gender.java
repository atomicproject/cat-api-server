package com.cats.app.entities;

/*
 * Enum for gender
 * 
 */
public enum Gender {

	MALE("Mâle"), FEMALE("Femelle");
	
	private String sex;
	
	private Gender(String sex) {
		this.sex = sex;
	}
	
	public String getSex() {
		return sex;
	}
	
	public String toString() {
		return this.sex;
	}
	
}
