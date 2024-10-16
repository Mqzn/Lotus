package io.github.mqzen.menus.base.pagination;

import java.util.List;

/**
 * Provides a list of components that will be used in a paginated UI.
 * Implementations of this interface are responsible for supplying the
 * necessary components to be displayed on each page.
 */
public interface PageComponentsProvider {
	
	/**
	 * @return the components of the page
	 */
	List<? extends PageComponent> provide();
	
}
