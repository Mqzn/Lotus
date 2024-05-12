package io.github.mqzen.menus.base;


import io.github.mqzen.menus.listeners.MenuClickListener;
import io.github.mqzen.menus.listeners.MenuCloseListener;
import io.github.mqzen.menus.listeners.MenuOpenListener;
import io.github.mqzen.menus.openers.DefaultOpener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import java.util.*;

public final class Lotus {
	
	
	private final DefaultOpener defaultOpener = new DefaultOpener();
	//TODO add more openers
	private final EnumMap<InventoryType, Opener> openers = new EnumMap<>(InventoryType.class);
	private final Map<UUID, PlayerMenu<?>> openMenus = new HashMap<>();
	private final Plugin plugin;
	@Getter
	@Setter
	private boolean allowOutsideClick = true;
	
	public Lotus(Plugin plugin) {
		this.plugin = plugin;
		registerOpeners();
		Bukkit.getPluginManager().registerEvents(new MenuClickListener(this), plugin);
		Bukkit.getPluginManager().registerEvents(new MenuOpenListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new MenuCloseListener(this), plugin);
	}
	
	private void registerOpeners() {
		//openers.put(InventoryType.ANVIL, new AnvilOpener());
	}
	
	public void openMenu(Player player, PlayerMenu<?> menu) {
		openMenus.put(player.getUniqueId(), menu);
		var opener = getOpener(menu.getType()).orElse(defaultOpener);
		menu.open(this, opener, player);
	}
	
	public void openMenu(Player player, MenuCreator creator) {
		PlayerMenu<?> playerMenu = new PlayerMenu<>(creator);
		openMenu(player, playerMenu);
	}
	
	public Optional<PlayerMenu<?>> getOpenMenus(UUID playerUUID) {
		return Optional.ofNullable(openMenus.get(playerUUID));
	}
	
	public void preClosePlayerMenu(PlayerMenu<?> menu, InventoryCloseEvent event) {
		menu.preClose(event);
		openMenus.remove(event.getPlayer().getUniqueId());
	}
	
	private Optional<Opener> getOpener(InventoryType type) {
		return Optional.ofNullable(openers.get(type));
	}
	
	public void setOpenMenus(Player clicker, PlayerMenu<?> playerMenu) {
		openMenus.put(clicker.getUniqueId(), playerMenu);
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
}
