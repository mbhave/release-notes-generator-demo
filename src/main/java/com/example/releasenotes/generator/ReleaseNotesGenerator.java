package com.example.releasenotes.generator;

import java.util.List;

import com.example.releasenotes.github.payload.Issue;
import com.example.releasenotes.github.service.GithubService;

import org.springframework.stereotype.Component;

@Component
public class ReleaseNotesGenerator {

	private final GithubService service;

	private final String organization = "spring-projects";

	public ReleaseNotesGenerator(GithubService service) {
		this.service = service;
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
		addIssuesToContent(content, issues);
		return content.toString();
	}

	private StringBuilder addIssuesToContent(StringBuilder content,
			List<Issue> issues) {
		issues.forEach((issue) -> content.append(getFormattedIssue(issue)));
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
