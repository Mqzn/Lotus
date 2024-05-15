package io.github.mqzen.menus.base.pagination;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.MenuCreator;
import io.github.mqzen.menus.misc.Button;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.Slot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class PageCreator implements MenuCreator {
	
	protected PageCreator() {
	
	}
	
	/**
	 * The number of buttons this page should have
	 *
	 * @param page   null if the pagination is automatic
	 * @param opener opener of this pagination
	 * @return The number of buttons this page should have
	 */
	public abstract int getPageButtonsCount(@Nullable Page page, Player opener);
	
	public Slot nextPageSlot(Capacity capacity) {
		return Slot.last(capacity);
	}
	
	public Slot previousPageSlot(Capacity capacity) {
		return nextPageSlot(capacity).subtractBy(8);
	}
	
	public abstract ItemStack nextPageItem(Player player);
	
	public abstract ItemStack previousPageItem(Player player);
	
	public final Content defaultContent(Page page, Capacity capacity, Player player, int componentPerPage) {
		var previousButtonSlot = previousPageSlot(capacity);
		var nextButtonSlot = nextPageSlot(capacity);
		
		Pagination pagination = page.getPagination();
		
		var content = Content.empty(capacity);
		
		boolean isLast;
		
		if (!pagination.isAutomatic())
			isLast = pagination.isLast(page);
		else {
			int endIndex = (page.getIndex() + 1) * componentPerPage;
			int compSize = page.getPagination().getPageComponents().size();
			isLast = (endIndex == compSize);
		}
		
		if (!isLast)
			content.setButton(nextButtonSlot, Button.clickable(nextPageItem(player),
				(menu, event) -> {
					event.setCancelled(true);
					pagination.next();
				}));
		
		if (!pagination.isFirst(page))
			content.setButton(previousButtonSlot, Button.clickable(previousPageItem(player),
				(menu, event) -> {
					event.setCancelled(true);
					pagination.previous();
				}));
		
		return content;
	}
	
}
