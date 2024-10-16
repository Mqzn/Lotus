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

/**
 * Represents a view for a paginated menu system in the Lotus framework.
 * This class extends BaseMenuView and is internally used to handle the
 * visualization and interaction of paginated content.
 */
@Getter
@ApiStatus.Internal
public final class PageView extends BaseMenuView<Page> {
	
	private final Pagination pagination;
	private final int index;

	private Capacity capacity;

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
		capacity = page.getCapacity(this.dataRegistry, opener);

		int maxButtonsCount = page.getFillRange(capacity, opener).getCount();

		if(maxButtonsCount > capacity.getTotalSize()) {
			maxButtonsCount = capacity.getTotalSize();
		}

		final Content playerDefaultContent = page.getContent(this.dataRegistry, opener, capacity);
		final Content pageDefaultContent = page.defaultContent(pagination, this, capacity, opener);

		if(pagination.trimExtraContent()) {
			playerDefaultContent.trim(maxButtonsCount);
		}

		final Content totalDefaultContent = playerDefaultContent.mergeWith(pageDefaultContent);
		final int defaultContentExpectedCapacity = capacity.getTotalSize()-maxButtonsCount;
		if(pagination.isAutomatic() && totalDefaultContent.size() > defaultContentExpectedCapacity) {
			throw new IllegalStateException(
					  String.format("Exceeded expected default capacity of the page \n " +
							    "Expected= %s, Actual= %s", defaultContentExpectedCapacity, totalDefaultContent.size())
			);
		}
		if(!pagination.isAutomatic() && totalDefaultContent.size() > capacity.getTotalSize()) {
			throw new IllegalStateException(
				String.format("Content of plain page #%s has exceeded the page's capacity(%s)", (index+1), capacity.getTotalSize())
			);
		}

		currentOpenedData = new ViewData(
				  page.getTitle(this.dataRegistry, opener), capacity, totalDefaultContent
		);
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
		//in automatic page view , we MUSTN'T initialize the data that was already pre-initialized internally in Pagination
		if (!pagination.isAutomatic())
			initialize(menu, player);
		
		currentOpenInventory = viewOpener.openMenu(api, player, this, currentOpenedData);
		currentOpener = player;
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
