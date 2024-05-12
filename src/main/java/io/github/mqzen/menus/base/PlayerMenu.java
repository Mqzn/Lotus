package io.github.mqzen.menus.base;

import io.github.mqzen.menus.base.pagination.Page;
import io.github.mqzen.menus.misc.*;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents player menu
 */
public class PlayerMenu<C extends MenuCreator> implements InventoryHolder {
	
	
	protected final C creator;
	
	@Getter
	protected MenuCache currentOpenedData = null;
	
	@Getter
	protected Inventory currentOpenInventory = null;
	
	@Getter
	protected Player currentOpener = null;
	
	protected final MenuData menuData = MenuData.empty();
	
	protected PlayerMenu(C creator) {
		this.creator = creator;
	}
	
	
	public @Nullable Content getContent() {
		return !isOpen() ? null : currentOpenedData.content();
	}
	
	public void addButton(Button button) {
		currentOpenedData.content()
			.addButton(button);
	}
	
	
	public void setClickedItem(@NotNull InventoryClickEvent event,
	                              @Nullable ItemStack itemStack) {
		setItem(event.getSlot(), itemStack);
	}
	
	

	public void setItem(int slot,
	                    @Nullable ItemStack newItem) {
		updateItem(Slot.of(slot), newItem);
	}
	
	public void updateItem(@NotNull Slot slot,
	                       @Nullable ItemStack newItem) {
		if(!isOpen())
			throw new IllegalStateException("Cannot update the item of a closed menus, the menus must be open");
		
		if (!currentOpener.getOpenInventory().getTopInventory().equals(currentOpenInventory))
			throw new IllegalStateException("Unexpected inventories mismatch, synchronization between cached open inventory and the opener's internal inventory has been lost !");
		
		currentOpenedData.content().updateButton(slot, (button)-> button.setItem(newItem));
		currentOpenInventory.setItem(slot.getSlot(),newItem);
	}
	
	public void updateItem(Slot slot, Consumer<ItemStack> updater) {
		ItemStack item = currentOpenInventory.getItem(slot.getSlot());
		if(item == null) return;
		updater.accept(item);
		updateItem(slot, item);
	}
	
	public void updateItemMeta(Slot slot, Consumer<ItemMeta> metaUpdater) {
		updateItem(slot,(item)-> {
			ItemMeta meta = item.getItemMeta();
			if(meta == null)return;
			metaUpdater.accept(meta);
			item.setItemMeta(meta);
		});
	}
	
	public void setClickedButton(@NotNull InventoryClickEvent event,
	                              @NotNull Button button) {
		updateButton(event.getSlot(), button);
	}
	
	public void updateButton(@NotNull Slot slot,
	                         @NotNull Button button) {
		if(!isOpen())
			throw new IllegalStateException("Cannot update the item of a closed menus, the menus must be open");
		
		if (!currentOpener.getOpenInventory().getTopInventory().equals(currentOpenInventory))
			throw new IllegalStateException("Unexpected inventories mismatch, synchronization between cached open inventory and the opener's internal inventory has been lost !");
		
		currentOpenedData.content().setButton(slot, button);
		currentOpenInventory.setItem(slot.getSlot(), button.getItem());
	}
	
	public void updateButton(int slot,
	                         @NotNull Button button) {
		updateButton(Slot.of(slot), button);
	}
	
	
	public final synchronized void open(Lotus manager, Opener opener, Player player) {
		currentOpener = player;
		if (!(this instanceof Page page)) {
			setData(creator, player);
		} else {
			if(!page.getPagination().isAutomatic()) { setData(creator, player); }
		}
		currentOpenInventory = opener.openMenu(manager, player, this, currentOpenedData);
	}
	
	public synchronized void preClose(InventoryCloseEvent event) {
		creator.onClose(this,event);
		currentOpener = null;
		currentOpenInventory = null;
		currentOpenedData = null;
	}
	
	public void executeItemAction(int clickedSlot, InventoryClickEvent e) {
		if(currentOpenedData == null)
			throw new IllegalStateException("Cannot find the data for this menus");
		
		currentOpenedData.content().getButton(clickedSlot)
			.ifPresent((button)-> button.executeOnClick(this, e));
		
	}
	@NotNull
	@Override
	public Inventory getInventory() {
		return currentOpenInventory;
	}
	
	public InventoryType getType() {
		return creator.getMenuType();
	}
	
	public final boolean isOpen() {
		return currentOpener != null
			&& currentOpenInventory != null
			&& currentOpenedData != null;
	}
	
	public final void preOpen(InventoryOpenEvent e) {
		creator.onOpen(this, e);
	}
	
	public void setData(C creator, Player opener) {
		Capacity capacity = creator.createCapacity(menuData, opener);
		currentOpenedData = new MenuCache(creator.createTitle( menuData, opener),
			capacity, creator.createContent(menuData, opener, capacity));
	}
}
