package io.github.mqzen.menus.base.pagination.exception;

public final class PageDoesntExistException extends Exception {
	
	public PageDoesntExistException(int page) {
		super("Page '#" + page + "' doesn't exist !");
	}
	
}
