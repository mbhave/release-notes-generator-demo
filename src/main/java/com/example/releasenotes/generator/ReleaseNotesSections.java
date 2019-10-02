package com.example.releasenotes.generator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.example.releasenotes.github.payload.Issue;
import com.example.releasenotes.properties.ReleaseNotesProperties;
import com.example.releasenotes.properties.ReleaseNotesProperties.Section;

import org.springframework.stereotype.Component;

@Component
class ReleaseNotesSections {

	private final List<ReleaseNotesSection> sections;

	ReleaseNotesSections(ReleaseNotesProperties properties) {
		this.sections = new ArrayList<>();
		addSections(properties.getSections());
	}

	private void addSections(List<Section> propertySections) {
		if (propertySections.isEmpty()) {
			add(this.sections, "New Features", ":star:", "enhancement");
			add(this.sections, "Bug Fixes", ":beetle:", "bug", "regression");
			return;
		}
		List<ReleaseNotesSection> releaseNotesSections = propertySections.stream().map(this::adapt).collect(Collectors.toList());
		this.sections.addAll(releaseNotesSections);
	}

	private static void add(List<ReleaseNotesSection> sections, String title,
			String emoji, String... labels) {
		sections.add(new ReleaseNotesSection(title, emoji, labels));
	}

	private ReleaseNotesSection adapt(Section propertySection) {
		return new ReleaseNotesSection(propertySection.getTitle(), propertySection.getEmoji(),
				propertySection.getLabels());
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
