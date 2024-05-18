package io.github.mqzen.menus.base.pagination;

import io.github.mqzen.menus.base.BaseMenuView;
import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.ViewOpener;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.ViewData;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.ApiStatus;

@Getter
@ApiStatus.Internal
public final class PageView extends BaseMenuView<Page> {
	
	private final Pagination pagination;
	private final int index;
	
	PageView(Pagination pagination, Page creator, int index) {
		super(pagination.getLotusAPI(), creator);
		this.pagination = pagination;
		this.index = index;
		this.dataRegistry.setData("index", index);
		this.dataRegistry.setData("pagination", pagination);
	}
	
	PageView(Pagination pagination, int index) {
		super(pagination.getLotusAPI(), pagination.getPageCreator());
		this.pagination = pagination;
		this.index = index;
		this.dataRegistry.setData("index", index);
		this.dataRegistry.setData("pagination", pagination);
	}
	
	/**
	 * Initializes the menu-view's data during pre-opening phase
	 *
	 * @param page   the menu to be used in creating the menu view's data
	 * @param opener the player opening
	 * @see ViewData
	 */
	@Override
	public void initialize(Page page, Player opener) {
		Capacity capacity = page.getCapacity(this.dataRegistry, opener);
		Content originalPageContent = page.getContent(this.dataRegistry, opener, capacity);
		
		int maxButtonsCount = page.getPageButtonsCount(this, opener);
		if (originalPageContent.size() > maxButtonsCount) originalPageContent.trim(maxButtonsCount);
		
		currentOpenedData = new ViewData(page.getTitle(this.dataRegistry, opener),
			capacity, originalPageContent.mergeWith(page.defaultContent(this, capacity, opener, maxButtonsCount)));
	}
	
	/**
	 * Opens the menu view using some information
	 * such as the way on how to open this view (ViewOpener) and the
	 * player who is opening and in which the view opened will be displayed for
	 *
	 * @param viewOpener Interface defining how to open this view
	 * @param player     the player opening this view
	 */
	@Override
	public void openView(ViewOpener viewOpener, Player player) {
		currentOpener = player;
		//in automatic page view , we MUSTN'T initialize the data that was already pre-initialized internally in Pagination
		if (!pagination.isAutomatic())
			initialize(menu, player);
		
		currentOpenInventory = viewOpener.openMenu(api, player, this, currentOpenedData);
	}
	
	/**
	 * What occurs/is executed on closing of this menu view
	 *
	 * @param event the close event for the view's internal inventory
	 */
	@Override
	public void onClose(InventoryCloseEvent event) {
		super.onClose(event);
		currentOpener = null;
		currentOpenInventory = null;
	}
	
}
