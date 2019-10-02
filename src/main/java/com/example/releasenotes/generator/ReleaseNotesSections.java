package com.example.releasenotes.generator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.example.releasenotes.github.payload.Issue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class ReleaseNotesSections {

	private final List<ReleaseNotesSection> sections;

	ReleaseNotesSections(@Value("${releasenotes.sections.titles: New Features,Bug Fixes}") List<String> sectionTitles,
			@Value("${releasenotes.sections.emojis: :star:,:beetle:}") List<String> sectionEmojis,
			@Value("${releasenotes.sections.labels: enhancement,regression}") List<String> sectionLabels) {
		this.sections = new ArrayList<>();
		for (int i = 0; i < sectionTitles.size(); i++) {
			add(this.sections, sectionTitles.get(i), sectionEmojis.get(i), sectionLabels.get(i));
		}
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
