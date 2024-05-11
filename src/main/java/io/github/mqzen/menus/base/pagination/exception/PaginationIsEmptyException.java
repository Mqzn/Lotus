package io.github.mqzen.menus.base.pagination.exception;

public final class PaginationIsEmptyException extends Exception {
	
	public PaginationIsEmptyException() {
		super("Pagination has no items/pages");
	}
}
