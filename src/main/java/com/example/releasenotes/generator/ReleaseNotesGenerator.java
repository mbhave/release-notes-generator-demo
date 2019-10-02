package com.example.releasenotes.generator;

import java.util.List;
import java.util.Map;

import com.example.releasenotes.github.payload.Issue;
import com.example.releasenotes.github.service.GithubService;
import com.example.releasenotes.properties.ReleaseNotesProperties;

import org.springframework.stereotype.Component;

@Component
public class ReleaseNotesGenerator {

	private final GithubService service;

	private final String organization;

	private final ReleaseNotesSections sections;

	public ReleaseNotesGenerator(GithubService service, ReleaseNotesProperties properties, ReleaseNotesSections sections) {
		this.service = service;
		this.organization = properties.getGithub().getOrganization();
		this.sections = sections;
	}

	public String generate(String milestone, String repository) {
		int milestoneNumber = getMilestoneNumber(milestone, repository);
		List<Issue> issues = this.service.getIssuesForMilestone(milestoneNumber, this.organization, repository);
		return generateContent(issues);
	}

	private int getMilestoneNumber(String milestone, String repository) {
		try {
			return Integer.parseInt(milestone);
		}
		catch (NumberFormatException ex) {
			return this.service.getMilestoneNumber(milestone, this.organization, repository);
		}
	}

	private String generateContent(List<Issue> issues) {
		StringBuilder content = new StringBuilder();
		addSectionContent(content, this.sections.collate(issues));
		return content.toString();
	}

	private StringBuilder addSectionContent(StringBuilder content,
			Map<ReleaseNotesSection, List<Issue>> sectionIssues) {
		sectionIssues.forEach((section, issues) -> {
			content.append((content.length() != 0) ? "\n" : "");
			content.append("## " + section + "\n\n");
			issues.stream().map(this::getFormattedIssue).forEach(content::append);
		});
		return content;
	}

	private String getFormattedIssue(Issue issue) {
		String title = issue.getTitle();
		return "- " + title + " " + getLinkToIssue(issue) + "\n";
	}

	private String getLinkToIssue(Issue issue) {
		return "[#" + issue.getNumber() + "]" + "(" + issue.getUrl() + ")";
	}

}
