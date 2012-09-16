package com.spaceemotion.payforaccess.command;

public class Type {
	private String name;
	private String desc;

	public Type(String n, String d) {
		this.name = n;
		this.desc = d;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return desc;
	}
}
