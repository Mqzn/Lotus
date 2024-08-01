package io.github.mqzen.menus;


import com.google.common.base.Preconditions;
import io.github.mqzen.menus.base.BaseMenuView;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.base.ViewOpener;
import io.github.mqzen.menus.listeners.MenuClickListener;
import io.github.mqzen.menus.listeners.MenuCloseListener;
import io.github.mqzen.menus.listeners.MenuOpenListener;
import io.github.mqzen.menus.openers.DefaultViewOpener;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * This class represents the main handler for Lotus's API,
 * and it's also considered the core of the API itself
 * It handles the way menu views are opened and displayed for players
 * It also caches all open menu views along with pre-registered menus to use instead of creating a new instance
 * of your menu each time.
 *
 * @see Menu
 * @see MenuView
 */
public final class Lotus {
	
	@Getter
	private final DefaultViewOpener defaultOpener = new DefaultViewOpener();
	
	private final EnumMap<InventoryType, ViewOpener> openers = new EnumMap<>(InventoryType.class);
	private final Map<UUID, MenuView<?>> openMenus = new HashMap<>();
	private final Map<String, Menu> preRegisteredMenus = new HashMap<>();
	
	@Getter
	private final Plugin plugin;
	
	@Getter
	@Setter
	private boolean allowOutsideClick = true;

	@Getter
	private final EventPriority clickPriority;

	private final BukkitAudiences audiences;

	public Lotus(Plugin plugin, EventPriority priority) {
		this.plugin = plugin;
		this.clickPriority = priority;
		this.audiences = BukkitAudiences.create(plugin);
		registerOpeners();

		MenuClickListener listener = new MenuClickListener(this);
		Bukkit.getPluginManager().registerEvent(InventoryClickEvent.class,
				  listener, clickPriority,
				  (l, event)-> listener.onClick((InventoryClickEvent) event), plugin);

		Bukkit.getPluginManager().registerEvents(new MenuOpenListener(this), plugin);
		Bukkit.getPluginManager().registerEvents(new MenuCloseListener(this), plugin);
	}
	
	private void registerOpeners() {
		//TODO register the rest of openers
	}
	
	
	/**
	 * @param name the name of the cached menu
	 * @return the cached menu.
	 */
	public Optional<Menu> getRegisteredMenu(String name) {
		return Optional.ofNullable(preRegisteredMenus.get(name));
	}
	
	/**
	 * @param playerUUID the opener's uuid of the view to return
	 * @return the menu view opened by the player who has the uuid
	 */
	public Optional<MenuView<?>> getMenuView(UUID playerUUID) {
		return Optional.ofNullable(openMenus.get(playerUUID));
	}
	
	/**
	 * @param type the type of inventory
	 * @return the view opener from the inventory type
	 */
	public Optional<ViewOpener> getViewOpener(InventoryType type) {
		return Optional.ofNullable(openers.get(type));
	}
	
	/**
	 * Registers a menu to be stored for easier and faster
	 * opening of this menu using its name only
	 *
	 * @param menu the menu to register
	 */
	public void registerMenu(Menu menu) {
		Preconditions.checkNotNull(menu);
		Preconditions.checkNotNull(menu.getName());
		preRegisteredMenus.put(menu.getName().toLowerCase(), menu);
	}
	
	/**
	 * Caches open view of a menus with the player's uuid
	 *
	 * @param player         the player/opener of this menu
	 * @param playerMenuView the view of the menu to set opened
	 */
	public void setOpenView(Player player, MenuView<?> playerMenuView) {
		Preconditions.checkNotNull(player);
		Preconditions.checkNotNull(playerMenuView);
		openMenus.put(player.getUniqueId(), playerMenuView);
	}
	
	/**
	 * Closes an open menu view during InventoryCloseEvent
	 * it runs onClose of the menu view then removes it from open view views cache
	 *
	 * @param view  the menu view
	 * @param event the close event
	 */
	public void closeView(MenuView<?> view, InventoryCloseEvent event) {
		Preconditions.checkNotNull(view);
		Preconditions.checkNotNull(event);
		view.onClose(event);
		openMenus.remove(event.getPlayer().getUniqueId());
	}
	
	/**
	 * Opens a view instance
	 *
	 * @param player the player to open for
	 * @param view   the view to open
	 */
	public void openMenu(Player player, MenuView<?> view) {
		setOpenView(player, view);
		var opener = getViewOpener(view.getType()).orElse(defaultOpener);
		view.openView(opener, player);
	}
	
	/**
	 * Opens a menu by creating a new view for it internally
	 *
	 * @param player the player to open the menu for
	 * @param menu   the menu to open
	 */
	public void openMenu(Player player, Menu menu) {
		MenuView<?> playerMenuView = new BaseMenuView<>(this, menu);
		openMenu(player, playerMenuView);
	}
	
	/**
	 * Opens a menu using its name from a registry
	 * if the menu with this name is not registered, it will not be opened and nothing happens
	 *
	 * @param player   the player to open the menu for
	 * @param menuName the name of the menu to open
	 */
	public void openMenu(Player player, String menuName) {
		getRegisteredMenu(menuName.toLowerCase()).ifPresent((menu) -> openMenu(player, menu));
	}
	
	public void sendComponent(CommandSender sender, Component component) {
		audiences.sender(sender).sendMessage(component);
	}

}
