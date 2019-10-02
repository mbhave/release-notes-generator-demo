package com.example.releasenotes.web;

import com.example.releasenotes.generator.ReleaseNotesGenerator;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReleaseNotesController {

	private final ReleaseNotesGenerator generator;

	public ReleaseNotesController(ReleaseNotesGenerator generator) {
		this.generator = generator;
	}

	@GetMapping("/generate")
	public String generate(@RequestParam String milestone, @RequestParam String repository) {
		try {
			return this.generator.generate(milestone, repository);
		}
		catch (Exception ex) {
			throw new InvalidReleaseNotesGenerationRequest();
		}
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	static class InvalidReleaseNotesGenerationRequest extends RuntimeException {

	}
}
