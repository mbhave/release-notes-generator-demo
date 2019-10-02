package com.example.releasenotes.github.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Issue {

	private final String number;

	private final String title;

	private final List<Label> labels;

	private final String url;

	public Issue(@JsonProperty("number") String number,
			@JsonProperty("title") String title,
			@JsonProperty("labels") List<Label> labels,
			@JsonProperty("html_url") String url) {
		super();
		this.number = number;
		this.title = title;
		this.labels = labels;
		this.url = url;
	}

	public String getTitle() {
		return this.title;
	}

	public List<Label> getLabels() {
		return this.labels;
	}

	public String getUrl() {
		return this.url;
	}

	public String getNumber() {
		return this.number;
	}

}
