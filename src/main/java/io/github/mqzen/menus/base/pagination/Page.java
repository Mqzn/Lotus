package io.github.mqzen.menus.base.pagination;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.PlayerMenu;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.MenuCache;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Objects;

@Getter
public final class Page extends PlayerMenu<PageCreator> {
	
	private final Pagination pagination;
	private final int index;
	
	private Page(Pagination pagination, PageCreator creator, int index) {
		super(creator);
		this.pagination = pagination;
		this.index = index;
		this.menuData.setData("index", index);
		this.menuData.setData("pagination", pagination);
	}
	
	private Page(Pagination pagination, int index) {
		super(pagination.getPageCreator());
		this.pagination = pagination;
		this.index = index;
		this.menuData.setData("index", index);
		this.menuData.setData("pagination", pagination);
	}
	
	static Page create(Pagination pagination, int index) {
		return new Page(pagination, index);
	}
	
	static Page create(Pagination pagination, PageCreator creator, int index) {
		return new Page(pagination, creator, index);
	}
	
	public PageCreator getCreator() {
		return creator;
	}
	
	public void open(Player opener) {
		pagination.getManager().openMenu(Objects.requireNonNull(opener), this);
	}
	
	@Override
	public void setData(PageCreator creator, Player opener) {
		Capacity capacity = creator.createCapacity(this.menuData, opener);
		Content originalPageContent = creator.createContent(this.menuData, opener, capacity);
		
		int maxButtonsCount = creator.getPageButtonsCount(this, opener);
		if (originalPageContent.size() > maxButtonsCount) originalPageContent.trim(maxButtonsCount);
		
		currentOpenedData = new MenuCache(creator.createTitle(this.menuData, opener),
			capacity, originalPageContent.mergeWith(creator.defaultContent(this, capacity, opener)));
	}
	
	public void setData(Player opener) {
		setData(this.creator, opener);
	}
	
	@Override
	public synchronized void preClose(InventoryCloseEvent event) {
		creator.onClose(this, event);
		currentOpener = null;
		currentOpenInventory = null;
	}
}
