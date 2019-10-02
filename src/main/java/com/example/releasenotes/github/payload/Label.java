package com.example.releasenotes.github.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Label {

	private final String name;

	public Label(@JsonProperty("name") String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
