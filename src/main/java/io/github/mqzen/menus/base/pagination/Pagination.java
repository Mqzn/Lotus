package io.github.mqzen.menus.base.pagination;

import com.google.common.collect.Lists;
import io.github.mqzen.menus.base.Lotus;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface Pagination {
	
	/**
	 * Moves to next page
	 */
	void next();
	
	/**
	 * moves to previous page
	 */
	void previous();
	
	/**
	 * The manager of all menus
	 * @return the menu manager
	 */
	@NotNull Lotus getManager();
	
	/**
	 * An automatic paginated menu is a pagination that automatically create pages depending on
	 * the number of items inserted into the pagination
	 * @return whether the paginated menu is automatic
	 */
	boolean isAutomatic();
	
	/**
	 * The creator of every page
	 * @return the page creator instance for creating a page inside of this pagination
	 */
	@NotNull PageCreator getPageCreator();
	
	@ApiStatus.Internal
	void setCurrentOpener(Player player);
	
	/**
	 * Opener for this pagination menu
	 * @return null if the menu is not open
	 */
	@Nullable Player getCurrentOpener();
	
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
	 * @see Page
	 * @param index index of the page-menu to fetch
	 * @return the page menu with specific index
	 */
	@NotNull Optional<Page> getPageOrDefault(int index, int defaultIndex);
	
	
	/**
	 * The page menu with index
	 * @see Page
	 * @param index index of the page-menu to fetch
	 * @return the page menu with specific index
	 */
	default @NotNull Optional<Page> getPage(int index) {
		return getPageOrDefault(index, 0);
	}
	
	/**
	 * Gets all pages
	 * @return all current pages
	 */
	@NotNull Collection<? extends Page> getPages();
	
	/**
	 * uses the components of the provider
	 * @param provider providing the components per page
	 */
	void setComponentProvider(PageComponentsProvider provider);
	
	/**
	 * Add the components
	 * @param components of the pagination
	 */
	void addComponents(List<PageComponent> components);
	/**
	 * Creates the pages for the player opening
	 * @param opener the opener
	 */
	@ApiStatus.Internal
	void paginate(Player opener);
	
	/**
	 * Checks whether this page is the last one
	 * @param index the index of the page
	 * @return whether this page is the last one
	 */
	boolean isLast(int index);
	
	default boolean isLast(Page page) {
		return isLast(page.getIndex());
	}
	/**
	 * Checks whether this page is the first one
	 * @param index the index of the page
	 * @return whether this page is the last one
	 */
	boolean isFirst(int index);
	
	default boolean isFirst(Page page) {
		return isFirst(page.getIndex());
	}
	
	void openPage(int pageIndex, Player opener) throws Exception;
	
	void open(Player opener) throws Exception;
	
	/**
	 * Sets the page (caching it manually)
	 *
	 * @param index    the index of the page
	 * @param creator the creator for this page
	 */
	void setPage(int index, PageCreator creator);
	
	static Pagination.Builder.Automatic auto(Lotus manager) {
		return new Builder.Automatic(manager);
	}
	static Pagination.Builder.Plain plain(Lotus manager) {
		return new Builder.Plain(manager);
	}
	

	
	
	abstract class Builder {
		
		protected final Lotus manager;
		protected boolean auto;
		
		protected Builder(Lotus manager,
		                  boolean auto) {
			this.manager = manager;
			this.auto = auto;
		}
		
		
		protected abstract Pagination build();
		
		public static class Automatic extends Builder {
			
			
			private PageCreator creator;
			private final LinkedList<PageComponent> components = Lists.newLinkedList();
			private PageComponentsProvider provider;
			protected Automatic(Lotus manager) {
				super(manager, true);
			}
			
			public Automatic creator(PageCreator creator) {
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
			
			
			@Override
			public Pagination build() {
				if(creator == null)
					throw new IllegalStateException("Didn't set creator for this pagination");
				
				PaginationImpl impl = new PaginationImpl(manager,auto, creator);
				if(provider != null)
					impl.setComponentProvider(provider);
				
				impl.addComponents(components);
				return impl;
			}
		}
		
		public static class Plain extends Builder{
			
			private final Map<Integer, PageCreator> creators = new HashMap<>();
			protected Plain(Lotus manager) {
				super(manager, false);
			}
			
			public Plain page(int index, PageCreator creator) {
				creators.put(index, creator);
				return this;
			}
			
			@Override
			public Pagination build() {
				PaginationImpl impl = new PaginationImpl(manager, auto,null);
				creators.forEach(impl::setPage);
				return impl;
			}
		}
		
	}
	
	
}
