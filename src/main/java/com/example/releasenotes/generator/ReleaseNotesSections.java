package com.example.releasenotes.generator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.example.releasenotes.github.payload.Issue;

import org.springframework.stereotype.Component;

@Component
class ReleaseNotesSections {

	private final List<ReleaseNotesSection> sections;

	ReleaseNotesSections() {
		this.sections = new ArrayList<>();
		add(this.sections, "New Features", ":star:", "enhancement");
		add(this.sections, "Bug Fixes", ":beetle:", "bug", "regression");
	}

	private static void add(List<ReleaseNotesSection> sections, String title,
			String emoji, String... labels) {
		sections.add(new ReleaseNotesSection(title, emoji, labels));
	}

	public Map<ReleaseNotesSection, List<Issue>> collate(List<Issue> issues) {
		SortedMap<ReleaseNotesSection, List<Issue>> collated = new TreeMap<>(
				Comparator.comparing(this.sections::indexOf));
		for (Issue issue : issues) {
			ReleaseNotesSection section = getSection(issue);
			if (section != null) {
				collated.computeIfAbsent(section, (key) -> new ArrayList<>());
				collated.get(section).add(issue);
			}
		}
		return collated;
	}

	private ReleaseNotesSection getSection(Issue issue) {
		for (ReleaseNotesSection section : this.sections) {
			if (section.isMatchFor(issue)) {
				return section;
			}
		}
		return null;
	}

}
