package io.github.mqzen.menus.base.pagination;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
final class PageViewFactory {
	
	private PageViewFactory() {
		throw new UnsupportedOperationException();
	}
	
	static PageView createAuto(Pagination pagination, int index) {
		return new PageView(pagination, index);
	}
	
	static PageView createPlain(Pagination pagination, Page model, int index) {
		return new PageView(pagination, model, index);
	}
	
	static PageView createView(Pagination pagination, @NotNull Page model, int index) {
		return pagination.isAutomatic() ? createAuto(pagination, index) : createPlain(pagination, model, index);
	}
	
}
