package io.github.mqzen.menus.base.pagination;

import com.google.common.collect.Lists;
import io.github.mqzen.menus.Lotus;
import io.github.mqzen.menus.base.pagination.exception.InvalidPageException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * This class represents a container of many menu views described as 'page views"
 * where each view is cached and have a specific index.
 * It's either automatic or plain, check the wiki for more details about this.
 */
public interface Pagination {
	
	static Pagination.Builder.Automatic auto(Lotus manager) {
		return new Builder.Automatic(manager);
	}
	
	static Pagination.Builder.Plain plain(Lotus manager) {
		return new Builder.Plain(manager);
	}

	/**
	 * it's always zero if {@link Pagination#paginate(Player)} hasn't been called yet !
	 * @return the total number of pages created
	 */
	int getMaximumPages();

	/**
	 * Moves to next page
	 */
	void next();
	
	/**
	 * moves to previous page
	 */
	void previous();
	
	/**
	 * The handler of all menus
	 *
	 * @return the lotus api
	 */
	@NotNull Lotus getLotusAPI();
	
	/**
	 * An automatic paginated menu is a pagination that automatically create pages depending on
	 * the number of items inserted into the pagination
	 *
	 * @return whether the paginated menu is automatic
	 */
	boolean isAutomatic();

	/**
	 * The creator of every page
	 *
	 * @return the page creator instance for creating a page inside of this pagination
	 */
	@NotNull Page getPageCreator();
	
	/**
	 * ViewOpener for this pagination menu
	 *
	 * @return null if the menu is not open
	 */
	@Nullable Player getCurrentOpener();
	
	@ApiStatus.Internal
	void setCurrentOpener(Player player);
	
	/**
	 * @return The current index of the page being open.
	 */
	int getCurrentPageIndex();
	
	/**
	 * @return the components of pagination
	 */
	List<PageComponent> getPageComponents();
	
	/**
	 * The page menu with index
	 *
	 * @param index index of the page-menu to fetch
	 * @return the page menu with specific index
	 * @see PageView
	 */
	@NotNull Optional<PageView> getPageOrDefault(int index, int defaultIndex);
	
	
	/**
	 * Gets all pages
	 *
	 * @return all current pages
	 */
	@NotNull Collection<? extends PageView> getPages();
	
	/**
	 * uses the components of the provider
	 *
	 * @param provider providing the components per page
	 */
	void setComponentProvider(PageComponentsProvider provider);
	
	/**
	 * Add the components
	 *
	 * @param components of the pagination
	 */
	void addComponents(List<PageComponent> components);
	
	/**
	 * Creates the pages for the player opening
	 *
	 * @param opener the opener
	 */
	@ApiStatus.Internal
	void paginate(Player opener);
	
	/**
	 * Checks whether this page is the last one
	 *
	 * @param index the index of the page
	 * @return whether this page is the last one
	 */
	boolean isLast(int index);
	
	
	/**
	 * Checks whether this page is the first one
	 *
	 * @param index the index of the page
	 * @return whether this page is the first one
	 */
	boolean isFirst(int index);
	
	
	/**
	 * Opens a page with specific index
	 *
	 * @param pageIndex the index of the page view to open
	 * @param opener    the player to open the view for
	 * @throws InvalidPageException if page is not registered in the pagination
	 */
	void openPage(int pageIndex, Player opener) throws InvalidPageException;
	
	/**
	 * Opens the first page view (index = 0) of the pagination
	 *
	 * @param opener the viewer of this page view
	 * @throws InvalidPageException if pagination is empty without any page components or no pages are registered in general
	 */
	void open(Player opener) throws InvalidPageException;
	
	/**
	 * Sets the page (caching it manually)
	 *
	 * @param index   the index of the page
	 * @param creator the creator for this page
	 */
	void setPage(int index, Page creator);
	
	/**
	 * The page menu with index
	 *
	 * @param index index of the page-menu to fetch
	 * @return the page menu with specific index
	 * @see PageView
	 */
	default @NotNull Optional<PageView> getPage(int index) {
		return getPageOrDefault(index, 0);
	}
	
	
	/**
	 * Checks whether this page is the first one
	 *
	 * @param pageView the view of the page
	 * @return whether this page is the first one
	 */
	default boolean isFirst(PageView pageView) {
		return isFirst(pageView.getIndex());
	}
	
	/**
	 * Checks whether this page is the last one
	 *
	 * @param pageView the view of the page
	 * @return whether this page is the last one
	 */
	default boolean isLast(PageView pageView) {
		return isLast(pageView.getIndex());
	}

	/**
	 * @return Whether to trim page-content
	 * if the content exceeds that of the max buttons count
	 * which is decided by {@link Page#getPageButtonsCount(PageView, Player)}
	 */
	boolean trimExtraContent();

	abstract class Builder {
		
		protected final Lotus manager;
		protected boolean auto;
		protected boolean trimExtra;

		protected Builder(Lotus manager,
		                  boolean auto) {
			this.manager = manager;
			this.auto = auto;
		}
		
		protected abstract Pagination build();
		
		public static class Automatic extends Builder {
			
			private boolean trimExtra = false;
			private final LinkedList<PageComponent> components = Lists.newLinkedList();
			private Page creator;
			private PageComponentsProvider provider;
			
			protected Automatic(Lotus manager) {
				super(manager, true);
			}
			
			public Automatic creator(Page creator) {
				this.creator = creator;
				return this;
			}
			
			public Automatic componentProvider(PageComponentsProvider provider) {
				this.provider = provider;
				return this;
			}
			
			public Automatic components(PageComponent... components) {
				this.components.addAll(Arrays.asList(components));
				return this;
			}


			public Automatic trimExtraContents(boolean trimExtra) {
				this.trimExtra = trimExtra;
				return this;
			}
			
			@Override
			public Pagination build() {
				if (creator == null)
					throw new IllegalStateException("Didn't set creator for this pagination");
				
				PaginationImpl impl = new PaginationImpl(manager, trimExtra, auto, creator);
				if (provider != null)
					impl.setComponentProvider(provider);
				
				impl.addComponents(components);
				return impl;
			}
		}
		
		public static class Plain extends Builder {
			
			private final Map<Integer, Page> creators = new HashMap<>();
			
			protected Plain(Lotus manager) {
				super(manager, false);
			}
			
			public Plain page(int index, Page creator) {
				creators.put(index, creator);
				return this;
			}
			public Plain trimExtraContents(boolean trimExtra) {
				this.trimExtra = trimExtra;
				return this;
			}
			
			@Override
			public Pagination build() {
				PaginationImpl impl = new PaginationImpl(manager, trimExtra, auto, null);
				creators.forEach(impl::setPage);
				impl.initLastPage();
				return impl;
			}
		}
		
	}


}
