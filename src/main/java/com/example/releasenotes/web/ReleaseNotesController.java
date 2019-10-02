package com.example.releasenotes.web;

import com.example.releasenotes.generator.ReleaseNotesGenerator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReleaseNotesController {

	private final ReleaseNotesGenerator generator;

	public ReleaseNotesController(ReleaseNotesGenerator generator) {
		this.generator = generator;
	}

	@GetMapping("/generate")
	public String generate(@RequestParam String milestone, @RequestParam String repository) {
		return this.generator.generate(milestone, repository);
	}

}
