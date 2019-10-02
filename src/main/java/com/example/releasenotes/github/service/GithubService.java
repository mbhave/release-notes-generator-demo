package com.example.releasenotes.github.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.releasenotes.github.payload.Issue;
import com.example.releasenotes.github.payload.Milestone;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubService {

	private static final Pattern LINK_PATTERN = Pattern.compile("<(.+)>; rel=\"(.+)\"");

	private static final String API_URL = "https://api.github.com/repos/{organization}/{repository}/";

	private static final String MILESTONES_URI = API_URL + "milestones";

	private static final String ISSUES_URI = API_URL
			+ "issues?milestone={milestone}&state=closed";

	private final RestTemplate restTemplate;

	public GithubService(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	public int getMilestoneNumber(String milestoneTitle, String organization,
			String repository) {
		Assert.hasText(milestoneTitle, "MilestoneName must not be empty");
		List<Milestone> milestones = getAll(Milestone.class, MILESTONES_URI, organization,
				repository);
		for (Milestone milestone : milestones) {
			if (milestoneTitle.equalsIgnoreCase(milestone.getTitle())) {
				return milestone.getNumber();
			}
		}
		throw new IllegalStateException(
				"Unable to find open milestone with title '" + milestoneTitle + "'");
	}

	public List<Issue> getIssuesForMilestone(int milestoneNumber, String organization,
			String repository) {
		return getAll(Issue.class, ISSUES_URI, organization, repository, milestoneNumber);
	}

	private <T> List<T> getAll(Class<T> type, String url, Object... uriVariables) {
		List<T> all = new ArrayList<>();
		Page<T> page = getPage(type, url, uriVariables);
		while (page != null) {
			all.addAll(page.getContent());
			page = page.getNextPage();
		}
		return all;
	}

	private <T> Page<T> getPage(Class<T> type, String url, Object... uriVariables) {
		if (!StringUtils.hasText(url)) {
			return null;
		}
		ResponseEntity<T[]> response = this.restTemplate.getForEntity(url,
				arrayType(type), uriVariables);
		return new Page<T>(Arrays.asList(response.getBody()),
				() -> getPage(type, getNextUrl(response.getHeaders())));
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T[]> arrayType(Class<T> elementType) {
		return (Class<T[]>) Array.newInstance(elementType, 0).getClass();
	}

	private String getNextUrl(HttpHeaders headers) {
		String links = headers.getFirst("Link");
		for (String link : StringUtils.commaDelimitedListToStringArray(links)) {
			Matcher matcher = LINK_PATTERN.matcher(link.trim());
			if (matcher.matches() && "next".equals(matcher.group(2))) {
				return matcher.group(1);
			}
		}
		return null;
	}

}
