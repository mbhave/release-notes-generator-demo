package com.example.releasenotes.github.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Milestone {

	private final int number;

	private final String title;

	public Milestone(@JsonProperty("number") int number,
			@JsonProperty("title") String title) {
		this.number = number;
		this.title = title;
	}

	public int getNumber() {
		return this.number;
	}

	public String getTitle() {
		return this.title;
	}

}
