package com.example.releasenotes;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.releasenotes.github.payload.Issue;
import com.example.releasenotes.github.payload.Label;
import com.example.releasenotes.github.service.GithubService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.util.FileCopyUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = "releasenotes.github.organization=test-org")
class ApplicationTests {

	@MockBean
	private GithubService githubService;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void generateReleaseNotes() throws Exception {
		given(this.githubService.getMilestoneNumber("2.0.0", "test-org",
				"test-repo")).willReturn(1);
		List<Issue> issues = getIssues();
		given(this.githubService.getIssuesForMilestone(1,
				"test-org", "test-repo")).willReturn(issues);
		String response = this.testRestTemplate.getForEntity("/generate?milestone={m}&repository={r}",
				String.class, "2.0.0", "test-repo").getBody();
		String expectedContent = FileCopyUtils.copyToString(new InputStreamReader(getClass().getResourceAsStream("sample-release-notes")));
		assertThat(response).isEqualTo(expectedContent);

	}

	private List<Issue> getIssues() {
		List<Issue> issues = new ArrayList<>();
		issues.add(newIssue("Bug 1", "1", "bug-1-url", "bug"));
		issues.add(newIssue("Enhancement 1", "2", "enhancement-1-url", "enhancement"));
		issues.add(newIssue("Enhancement 2", "4", "enhancement-2-url", "enhancement"));
		issues.add(newIssue("Bug 3", "3", "bug-3-url", "bug"));
		return issues;
	}

	private Issue newIssue(String number, String title, String url, String label) {
		List<Label> labels = Collections.singletonList(new Label(label));
		return new Issue(number, title, labels, url);
	}
}