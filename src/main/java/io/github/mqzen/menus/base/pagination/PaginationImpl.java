package io.github.mqzen.menus.base.pagination;

import com.google.common.collect.Lists;
import io.github.mqzen.menus.Lotus;
import io.github.mqzen.menus.base.pagination.exception.InvalidPageException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

class PaginationImpl implements Pagination {
	
	private final Lotus manager;
	private final Page pageModel;
	private final List<PageComponent> components = Lists.newArrayList();
	private final Map<Integer, PageView> pages = new HashMap<>();
	private int lastPage;
	private final boolean automatic;
	private @Nullable Player currentOpener = null;
	private int currentIndex = 0;
	private final boolean trimExtra;
	private int maxPages = 0;

	PaginationImpl(Lotus manager,
						boolean trimExtra,
	               boolean automatic, Page pageModel) {
		if (automatic && pageModel == null)
			throw new IllegalStateException("Automatic pagination has no creator");
		
		this.manager = manager;
		this.trimExtra = trimExtra;
		this.automatic = automatic;
		this.pageModel = pageModel;
	}


	/**
	 * it's always zero if {@link Pagination#paginate(Player)} hasn't been called yet !
	 *
	 * @return the total number of pages created
	 */
	@Override
	public int getMaximumPages() {
		return maxPages;
	}

	/**
	 * Moves to next page
	 */
	@Override
	public void next() {
		assert currentOpener != null;
		
		currentIndex++;
		
		open();
	}
	
	/**
	 * moves to previous page
	 */
	@Override
	public void previous() {
		assert currentOpener != null;
		currentIndex--;
		open();
	}

	private void open() {
		try {
			openPage(currentIndex, currentOpener);
		} catch (Exception e) {
			if(e instanceof InvalidPageException )
				return;
			e.printStackTrace();
		}
	}
	
	/**
	 * The manager of all menus
	 *
	 * @return the menu manager
	 */
	@Override
	public @NotNull Lotus getLotusAPI() {
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
	public @NotNull Page getPageCreator() {
		return pageModel;
	}
	
	/**
	 * ViewOpener for this pagination menu
	 *
	 * @return null if the menu is not open
	 */
	@Override
	public @Nullable Player getCurrentOpener() {
		return currentOpener;
	}
	
	@Override
	public void setCurrentOpener(@Nullable Player player) {
		this.currentOpener = player;
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
	 * @param index        index of the page-menu to fetch
	 * @param defaultIndex default index if page with that index doesn't exist !
	 * @return the page menu with specific index
	 * @see PageView
	 */
	@Override
	public @NotNull Optional<PageView> getPageOrDefault(int index, int defaultIndex) {
		return Optional.ofNullable(pages.getOrDefault(index, pages.get(defaultIndex)));
	}
	
	/**
	 * Gets all pages
	 *
	 * @return all current pages
	 */
	@Override
	public @NotNull Collection<? extends PageView> getPages() {
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
		if (!automatic)
			throw new IllegalStateException("Automatic pagination is only allowed to paginate and create pages automatically");

		this.maxPages = calculateMaxPages(opener);
		this.lastPage = maxPages-1;

		if(maxPages <= 0) {
			lastPage = 0;
			PageView pageView = PageViewFactory.createAuto(this, lastPage);
			pageView.initialize(this.pageModel, opener);
			pages.put(lastPage, pageView);
			return;
		}

		for (int pageIndex = 0; pageIndex < maxPages; pageIndex++) {
			PageView pageView = PageViewFactory.createAuto(this, pageIndex);
			pageView.initialize(this.pageModel, opener);
			
			final int buttonsPerPage = pageView.getMenu().getPageButtonsCount(pageView, opener);
			
			int startIndex = pageIndex * buttonsPerPage;
			int endIndex = (pageIndex + 1) * buttonsPerPage;
			
			for (int index = startIndex; index < endIndex; index++) {
				PageComponent component = getComponent(components, index);
				if (component == null) break;
				pageView.addButtons(component.toButton());
			}
			pages.put(pageIndex, pageView);
		}



	}

	private static PageComponent getComponent(List<PageComponent> components, int index) {
		try {
			return components.get(index);
		}catch (Exception ex) {
			return null;
		}
	}
	
	private int calculateMaxPages(Player opener) {
		int buttonsCountPerPage = pageModel.getPageButtonsCount(null, opener);
		return (int) Math.ceil((double) components.size() / buttonsCountPerPage);
	}
	
	
	@Override
	public boolean isLast(int index) {
		return index == lastPage;
	}
	
	@Override
	public boolean isFirst(int index) {
		return index == 0;
	}
	
	@Override
	public void openPage(int pageIndex, Player opener) throws InvalidPageException {
		PageView pageView = pages.get(pageIndex);
		
		if (pageView == null)
			throw new InvalidPageException(pageIndex);
		
		Bukkit.getScheduler().runTaskLater(manager.getPlugin(), () -> this.getLotusAPI().openMenu(opener, pageView), 1L);
	}
	
	@Override
	public void open(Player opener) throws InvalidPageException {
		if (this.isAutomatic())
			paginate(opener);
		
		setCurrentOpener(opener);
		openPage(0, opener);
	}
	
	/**
	 * Sets the page (caching it manually)
	 *
	 * @param index   the index of the page
	 * @param creator the creator for this page
	 */
	@Override
	public void setPage(int index, Page creator) {
		if (automatic)
			throw new IllegalStateException("Manual addition of a pageView cannot be done while the pagination is declared automatic !");
		PageView pageView = PageViewFactory.createView(this, creator, index);
		pages.put(index, pageView);
	}

	/**
	 * @return Whether to trim page-content
	 * if the content exceeds that of the max buttons count
	 * which is decided by {@link Page#getPageButtonsCount(PageView, Player)}
	 */
	@Override
	public boolean trimExtraContent() {
		return trimExtra;
	}


	public void initLastPage() {
		this.lastPage = pages.size()-1;
	}
}
