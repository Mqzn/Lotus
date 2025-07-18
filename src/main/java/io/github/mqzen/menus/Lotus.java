package io.github.mqzen.menus;


import com.google.common.base.Preconditions;
import io.github.mqzen.menus.base.BaseMenuView;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.base.ViewOpener;
import io.github.mqzen.menus.base.serialization.MenuSerializer;
import io.github.mqzen.menus.base.serialization.SerializableMenu;
import io.github.mqzen.menus.base.serialization.SerializedMenuIO;
import io.github.mqzen.menus.base.serialization.impl.SerializedMenuYaml;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.openers.DefaultViewOpener;
import io.github.mqzen.menus.util.InventoryUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import java.util.*;

/**
 * This class represents the main handler for Lotus's API,
 * and it's also considered the core of the API itself,
 * It handles the way menu views are opened and displayed for players,
 * It also caches all open menu views along with pre-registered menus to use instead of creating a new instance
 * of your menu each time.
 *
 * @see Menu
 * @see MenuView
 */
public final class Lotus {
	
	LotusDebugger debugger = LotusDebugger.EMPTY;
	
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
	@Setter
	private SerializedMenuIO<?> menuIO;
	
	@Getter
	@Setter
	private MenuSerializer menuSerializer;
	
	@Getter
	private long updateTicks = 15L;
    
    private Lotus(Plugin plugin) {
		this.plugin = plugin;
		menuIO = new SerializedMenuYaml();
		menuSerializer = MenuSerializer.newDefaultSerializer();
		
		registerOpeners();

		Bukkit.getPluginManager().registerEvents(new LotusListener(), plugin);
	}
	
	public static Lotus load(Plugin plugin) {
		Preconditions.checkNotNull(plugin, "Plugin cannot be null");
		return new Lotus(plugin);
	}
	
	private void registerOpeners() {
		//TODO register the rest of openers
	}
	
	public void enableDebugger() {
		this.debugger = new LotusDebugger(plugin.getLogger());
	}
	
	public boolean isDebuggerEnabled() {
		return !debugger.isEmpty();
	}
	
	public void disableDebugger() {
		this.debugger = LotusDebugger.EMPTY;
	}
	
	@SuppressWarnings("unchecked")
	public Menu readYamlMenu(YamlConfiguration configuration) {
		if(!YamlConfiguration.class.isAssignableFrom(menuIO.fileType()))
			throw new IllegalArgumentException("File type of menu IO does not support Yaml serialization");
		
		SerializedMenuIO<YamlConfiguration> yamlIO = (SerializedMenuIO<YamlConfiguration>) menuIO;
		return menuSerializer.deserialize(yamlIO.read(configuration));
	}
	
	@SuppressWarnings("unchecked")
	public void writeYamlMenu(SerializableMenu menu, YamlConfiguration configuration) {
		if(!YamlConfiguration.class.isAssignableFrom(menuIO.fileType()))
			throw new IllegalArgumentException("File type of menu IO does not support Yaml serialization");
		
		SerializedMenuIO<YamlConfiguration> yamlIO = (SerializedMenuIO<YamlConfiguration>) menuIO;
		yamlIO.write(menuSerializer.serialize(menu), configuration);
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
		ViewOpener opener = getViewOpener(view.getType()).orElse(defaultOpener);
		view.openView(opener, player);
	}
	
	/**
	 * Opens a menu by creating a new view for it internally
	 *
	 * @param player the player to open the menu for
	 * @param menu   the menu to open
	 */
	public void openMenu(Player player, Menu menu) {
		openMenu(player, menu, DataRegistry.empty());
	}
	
	/**
	 * Opens a menu by creating a new view for it internally
	 * using a {@link DataRegistry}
	 *
	 * @param player the player to open the menu for
	 * @param menu   the menu to open
	 * @param registry the data cached to this menu
	 */
	public void openMenu(Player player, Menu menu, DataRegistry registry) {
		MenuView<?> playerMenuView = new BaseMenuView<>(this, menu, registry);
		openMenu(player, playerMenuView);
	}

	/**
	 * Opens a menu using its name from a registry
	 * if the menu with this name is not registered, it will not be opened, and nothing happens
	 *
	 * @param player   the player to open the menu for
	 * @param menuName the name of the menu to open
	 */
	public void openMenu(Player player, String menuName) {
		Menu menu = getRegisteredMenu(menuName.toLowerCase()).orElse(null);
		if(menu == null) {
			debugger.warn("Failed to open menu '%s' because it has not been registered", menuName);
		}
		else {
			openMenu(player, menu);
		}
	}
	
	/**
	 * @return Retrieves the open {@link MenuView}
	 */
	public Collection<? extends MenuView<?>> getOpenViews() {
		return openMenus.values();
	}

	public void debug(String msg, Object... args) {
		debugger.debug(msg, args);
	}

	final class LotusListener implements Listener {
		@EventHandler(priority = EventPriority.LOW)
		public void onClick(InventoryClickEvent e) {
			if(e.isCancelled())
				return;

			Player clicker = (Player) e.getWhoClicked();

			Inventory topInventory = e.getInventory();
			//Inventory bottomInventory = e.getView().getBottomInventory();
			Inventory clickedInventory = e.getClickedInventory();


			MenuView<?> menu = Lotus.this.getMenuView(clicker.getUniqueId()).orElseGet(() -> {
				if (topInventory.getHolder() instanceof MenuView<?>) {
					MenuView<?> playerMenu = (MenuView<?>) topInventory.getHolder();
					Lotus.this.setOpenView(clicker, playerMenu);
					return playerMenu;
				}
				return null;
			});

			if (menu == null) {
				e.setCancelled(!Lotus.this.allowOutsideClick);
				return;
			}

			if (clickedInventory == null)
				return;

			Lotus.this.debug("Triggering InventoryClickEvent");
			menu.handleOnClick(e);
		}

		@EventHandler(priority = EventPriority.LOW)
		public void onDrag(InventoryDragEvent e) {
			Player clicker = (Player) e.getWhoClicked();
			Inventory clickedInventory = e.getInventory();

			final Inventory topInventory = InventoryUtil.getTopInventory(e);
			MenuView<?> menu = Lotus.this.getMenuView(clicker.getUniqueId()).orElseGet(() -> {
				if (topInventory.getHolder() instanceof MenuView<?>) {
					MenuView<?> playerMenu = (MenuView<?>)topInventory.getHolder();
					Lotus.this.setOpenView(clicker, playerMenu);
					return playerMenu;
				}
				return null;
			});

			if (menu == null) {
				e.setCancelled(!Lotus.this.allowOutsideClick);
				return;
			}
			/*
			if(menu != null && lastClickEvent != null && lastClickEvent.getInventory() == clickedInventory) {
				InventoryClickEvent clickEvent = new InventoryClickEvent(
					lastClickEvent.getView(), lastClickEvent.getSlotType(), lastClickEvent.getSlot(), lastClickEvent.getClick(), lastClickEvent.getAction()
				);
                menu.handleOnClick(clickEvent);
			}
			*/

			if (clickedInventory != null && clickedInventory == topInventory) {
				Lotus.this.debug("Triggering InventoryDragEvent");
				menu.onDrag(e);
			}
		}

		@EventHandler(priority = EventPriority.LOW)
		public void onClose(InventoryCloseEvent e) {
			Player closer = (Player) e.getPlayer();
			Lotus.this.debug("Triggering InventoryCloseEvent");
			Lotus.this.getMenuView(closer.getUniqueId()).ifPresent((menu) -> Lotus.this.closeView(menu, e));
		}

		@EventHandler(priority = EventPriority.LOW)
		public void onOpen(InventoryOpenEvent e) {

			Inventory inventory = e.getInventory();
			if (!(inventory.getHolder() instanceof MenuView<?>))
				return;
			MenuView<?> menu = (MenuView<?>)inventory.getHolder();
			Lotus.this.setOpenView((Player) e.getPlayer(), menu);
			menu.onOpen(e);
		}

	}

}
