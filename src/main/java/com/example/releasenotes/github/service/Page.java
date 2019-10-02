package com.example.releasenotes.github.service;

import java.util.List;
import java.util.function.Supplier;

class Page<T> {

	private final List<T> content;

	private final Supplier<Page<T>> nextPageSupplier;

	Page(List<T> content, Supplier<Page<T>> nextPageSupplier) {
		this.content = content;
		this.nextPageSupplier = nextPageSupplier;
	}

	public List<T> getContent() {
		return this.content;
	}

	public Page<T> getNextPage() {
		return this.nextPageSupplier.get();
	}

}
