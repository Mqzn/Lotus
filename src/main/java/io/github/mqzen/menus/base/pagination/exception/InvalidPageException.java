package io.github.mqzen.menus.base.pagination.exception;

public final class InvalidPageException extends Exception {
	
	public InvalidPageException(int page) {
		super("PageView '#" + page + "' doesn't exist !");
	}
	
}
