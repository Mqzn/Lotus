package io.github.mqzen.menus.base.pagination;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class Page implements Menu {
	
	protected Page() {
	
	}
	
	/**
	 * The number of buttons this pageView should have
	 *
	 * @param pageView null if the pagination is automatic
	 * @param opener   opener of this pagination
	 * @return The number of buttons this pageView should have
	 */
	public abstract int getPageButtonsCount(@Nullable PageView pageView, Player opener);
	
	public Slot nextPageSlot(Capacity capacity) {
		return Slot.last(capacity);
	}
	
	public Slot previousPageSlot(Capacity capacity) {
		return nextPageSlot(capacity).subtractBy(8);
	}
	
	public abstract ItemStack nextPageItem(Player player);
	
	public abstract ItemStack previousPageItem(Player player);
	
	public final Content defaultContent(Pagination pagination,
	                                    PageView pageView,
	                                    Capacity capacity,
	                                    Player player) {
		var previousButtonSlot = previousPageSlot(capacity);
		var nextButtonSlot = nextPageSlot(capacity);
		
		var content = Content.empty(capacity);

		if (!pagination.isLast(pageView))
			content.setButton(nextButtonSlot, Button.clickable(nextPageItem(player),
					ButtonClickAction.plain((menu, event) -> {
						event.setCancelled(true);
						pagination.next();
					})));
		
		if (!pagination.isFirst(pageView))
			content.setButton(previousButtonSlot, Button.clickable(previousPageItem(player),
					ButtonClickAction.plain((menu, event) -> {
						event.setCancelled(true);
						pagination.previous();
					})));
		
		return content;
	}
	
}
