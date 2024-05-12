package io.github.mqzen.menus.base.pagination;

import com.google.common.collect.Lists;
import io.github.mqzen.menus.base.Lotus;
import io.github.mqzen.menus.base.pagination.exception.PageDoesntExistException;
import io.github.mqzen.menus.base.pagination.exception.PaginationIsEmptyException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

class PaginationImpl implements Pagination {
	
	private final Lotus manager;
	private final PageCreator creator;
	private @Nullable Player currentOpener = null;
	private final List<PageComponent> components = Lists.newArrayList();
	private final Map<Integer, Page> pages = new HashMap<>();
	private final boolean automatic;
	
	private int currentIndex = 0;
	
	PaginationImpl(Lotus manager,
	               boolean automatic, PageCreator creator) {
		if(automatic && creator == null)
			throw new IllegalStateException("Automatic pagination has no creator");
		
		this.manager = manager;
		this.automatic = automatic;
		this.creator = creator;
	}
	
	
	/**
	 * Moves to next page
	 */
	@Override
	public void next() {
		assert currentOpener != null;
		
		currentIndex++;

		try {
			openPage(currentIndex, currentOpener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * moves to previous page
	 */
	@Override
	public void previous() {
		assert currentOpener != null;
		currentIndex--;
		
		try {
			openPage(currentIndex, currentOpener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The manager of all menus
	 *
	 * @return the menu manager
	 */
	@Override
	public @NotNull Lotus getManager() {
		return manager;
	}
	
	/**
	 * An automatic paginated menu is a pagination that automatically create pages depending on
	 * the number of items inserted into the pagination
	 *
	 * @return whether the paginated menu is automatic
	 */
	@Override
	public boolean isAutomatic() {
		return automatic;
	}
	
	/**
	 * The creator of every page
	 *
	 * @return the page creator instance for creating a page inside of this pagination
	 */
	@Override
	public @NotNull PageCreator getPageCreator() {
		return creator;
	}
	
	@Override
	public void setCurrentOpener(@Nullable Player player) {
		this.currentOpener = player;
	}
	
	
	/**
	 * Opener for this pagination menu
	 *
	 * @return null if the menu is not open
	 */
	@Override
	public @Nullable Player getCurrentOpener() {
		return currentOpener;
	}
	
	/**
	 * @return The current index of the page being open.
	 */
	@Override
	public int getCurrentPageIndex() {
		return currentIndex;
	}
	
	/**
	 * @return the components of pagination
	 */
	@Override
	public List<PageComponent> getPageComponents() {
		return components;
	}
	
	/**
	 * The page menu with index
	 *
	 * @param index index of the page-menu to fetch
	 * @param defaultIndex default index if page with that index doesn't exist !
	 * @return the page menu with specific index
	 * @see Page
	 */
	@Override
	public @NotNull Optional<Page> getPageOrDefault(int index, int defaultIndex) {
		return Optional.ofNullable(pages.getOrDefault(index, pages.get(defaultIndex)));
	}
	
	/**
	 * Gets all pages
	 *
	 * @return all current pages
	 */
	@Override
	public @NotNull Collection<? extends Page> getPages() {
		return pages.values();
	}
	
	/**
	 * uses the components of the provider
	 *
	 * @param provider providing the components per page
	 */
	@Override
	public void setComponentProvider(PageComponentsProvider provider) {
		components.addAll(provider.provide());
	}
	
	/**
	 * Add the components
	 *
	 * @param components of the pagination
	 */
	@Override
	public void addComponents(List<PageComponent> components) {
		this.components.addAll(components);
	}
	
	@Override
	public synchronized void paginate(Player opener) {
		if(!automatic)
			throw new IllegalStateException("Automatic pagination is only allowed to paginate and create pages automatically");
		
		int maxPages = calculateMaxPages(opener);
		for (int pageIndex = 0; pageIndex < maxPages; pageIndex++) {
			Page page = Page.create(this, pageIndex);
			page.setData(opener);
			
			final int buttonsPerPage = page.getCreator().getPageButtonsCount(page, opener);
			
			int startIndex = pageIndex * buttonsPerPage;
			int endIndex = (pageIndex+1) * buttonsPerPage;
			
			for (int index = startIndex; index < endIndex; index++) {
				PageComponent component = components.get(index);
				if(component == null) break;
				page.addButton(component.toButton());
			}
			pages.put(pageIndex, page);
		}
		
	}
	
	private int calculateMaxPages(Player opener) {
		int buttonsCountPerPage = creator.getPageButtonsCount(null, opener);
		return (int) Math.ceil((double)  components.size() / buttonsCountPerPage);
	}
	
	
	@Override
	public boolean isLast(int index) {
		int highestIndex = pages.keySet().stream().max(Comparator.comparingInt((i)->i))
			.orElse(0);
		return index == highestIndex;
	}
	
	@Override
	public boolean isFirst(int index) {
		return index == 0;
	}
	
	@Override
	public void openPage(int pageIndex, Player opener) throws Exception {
		Page page = pages.get(pageIndex);
		
		if(page == null) {
			if(pageIndex == 0) throw new PaginationIsEmptyException();
			else throw new PageDoesntExistException(pageIndex);
		}
		Bukkit.getScheduler().runTaskLater(manager.getPlugin(), ()-> page.open(opener),1L);
	}
	
	@Override
	public void open(Player opener) throws Exception {
		if(this.isAutomatic())
			paginate(opener);
		
		setCurrentOpener(opener);
		openPage(0,opener);
	}
	
	/**
	 * Sets the page (caching it manually)
	 *
	 * @param index    the index of the page
	 * @param creator the creator for this page
	 */
	@Override
	public void setPage(int index, PageCreator creator) {
		if(automatic) throw new IllegalStateException("Manual addition of a page cannot be done while the pagination is declared automatic !");
		Page page = Page.create(this, creator, index);
		pages.put(index, page);
	}
	

	
}
