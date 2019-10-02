package com.example.releasenotes.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "releasenotes")
public class ReleaseNotesProperties {

	/**
	 * GitHub properties.
	 */
	private Github github = new Github();

	/**
	 * Section definitions in the order that they should appear.
	 */
	private List<Section> sections = new ArrayList<>();

	public Github getGithub() {
		return this.github;
	}

	public List<Section> getSections() {
		return this.sections;
	}

	/**
	 * Github related properties.
	 */
	public static class Github {

		/**
		 * The username for the github user.
		 */
		private String username;

		/**
		 * The password for the github user.
		 */
		private String password;

		/**
		 * The github org this repository is under.
		 */
		private String organization = "spring-projects";

		public String getOrganization() {
			return this.organization;
		}

		public void setOrganization(String organization) {
			this.organization = organization;
		}

		public String getUsername() {
			return this.username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return this.password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}

	/**
	 * Properties for a single release notes section.
	 */
	public static class Section {

		/**
		 * The title of the section.
		 */
		private String title;

		/**
		 * The emoji character to use, for example ":star:".
		 */
		private String emoji;

		/**
		 * The labels used to identify if an issue is for the section.
		 */
		private List<String> labels = new ArrayList<>();

		public String getTitle() {
			return this.title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getEmoji() {
			return this.emoji;
		}

		public void setEmoji(String emoji) {
			this.emoji = emoji;
		}

		public List<String> getLabels() {
			return this.labels;
		}

		public void setLabels(List<String> labels) {
			this.labels = labels;
		}

	}

}
