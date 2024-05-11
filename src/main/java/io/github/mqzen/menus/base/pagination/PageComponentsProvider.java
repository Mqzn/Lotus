package io.github.mqzen.menus.base.pagination;

import java.util.List;

public interface PageComponentsProvider {
	
	/**
	 * @return the components of the page
	 */
	List<PageComponent> provide();
	
}
