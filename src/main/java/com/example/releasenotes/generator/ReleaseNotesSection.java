package com.example.releasenotes.generator;

import java.util.Arrays;
import java.util.List;

import com.example.releasenotes.github.payload.Issue;
import com.example.releasenotes.github.payload.Label;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

class ReleaseNotesSection {

	private final String title;

	private final String emoji;

	private final List<String> labels;

	ReleaseNotesSection(String title, String emoji, List<String> labels) {
		Assert.hasText(title, "Title must not be empty");
		Assert.hasText(emoji, "Emoji must not be empty");
		Assert.isTrue(!CollectionUtils.isEmpty(labels), "Labels must not be empty");
		this.title = title;
		this.emoji = emoji;
		this.labels = labels;
	}

	public boolean isMatchFor(Issue issue) {
		for (String candidate : this.labels) {
			for (Label label : issue.getLabels()) {
				if (label.getName().contains(candidate)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return this.emoji + " " + this.title;
	}

}
