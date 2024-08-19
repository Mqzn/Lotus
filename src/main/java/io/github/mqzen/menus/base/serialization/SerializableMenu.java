package io.github.mqzen.menus.base.serialization;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.itembuilder.LegacyItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;

import static io.github.mqzen.menus.base.serialization.SimpleMenuSerializer.PLAYER_PLACEHOLDER;

public final class SerializableMenu implements Menu {
	
	private final String name;
	private final String titleString;
	private final Capacity capacity;
	private final Content content;
	
	public SerializableMenu(
					String name, String titleString,
					Capacity capacity, Content content
	) {
		this.name = name;
		this.titleString = titleString;
		this.capacity = capacity;
		this.content = content;
	}
	
	public SerializableMenu(DataRegistry dataRegistry) {
		this(
			dataRegistry.getData("name"),
			dataRegistry.getData("title"),
			dataRegistry.getData("capacity"),
			dataRegistry.getData("content")
		);
	}
	
	/**
	 * @return The unique name for this menu
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * @param extraData the data container for this menu for extra data
	 * @param opener    the player who is opening this menu
	 * @return the title for this menu
	 */
	@Override
	public @NotNull MenuTitle getTitle(DataRegistry extraData, Player opener) {
		return MenuTitles.createLegacy(titleString()
						.replace(PLAYER_PLACEHOLDER, opener.getName()));
	}
	
	/**
	 * @param extraData the data container for this menu for extra data
	 * @param opener    the player who is opening this menu
	 * @return the capacity/size for this menu
	 */
	@Override
	public @NotNull Capacity getCapacity(DataRegistry extraData, Player opener) {
		return capacity;
	}
	
	/**
	 * Creates the content for the menu
	 *
	 * @param extraData the data container for this menu for extra data
	 * @param opener    the player opening this menu
	 * @param capacity  the capacity set by the user above
	 * @return the content of the menu to add (this includes items)
	 */
	@Override
	public @NotNull Content getContent(DataRegistry extraData, Player opener, Capacity capacity) {
		
		Content modified = content();
		for (Slot slot : modified.getButtonMap().keySet()) {
			modified.setButton(slot, modified.getButton(slot).map((b) -> {
				if (b.getItem() == null) return b;
				ItemStack oldItem = b.getItem();
				String oldDisplay = oldItem.getItemMeta().getDisplayName();
				List<String> oldLore = oldItem.getItemMeta().getLore();
				oldLore.replaceAll((str) -> str.replace(PLAYER_PLACEHOLDER, opener.getName()));
				
				return b.setItem(LegacyItemBuilder
								.legacy(oldItem)
								.setDisplay(oldDisplay.replace(PLAYER_PLACEHOLDER, opener.getName()))
								.setLore(oldLore)
								.build());
			}).orElse(null));
		}
		
		return modified;
	}
	
	public String titleString() {
		return titleString;
	}
	
	public Capacity capacity() {
		return capacity;
	}
	
	public Content content() {
		return content;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (SerializableMenu) obj;
		return Objects.equals(this.name, that.name) &&
						Objects.equals(this.titleString, that.titleString) &&
						Objects.equals(this.capacity, that.capacity) &&
						Objects.equals(this.content, that.content);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, titleString, capacity, content);
	}
	
	@Override
	public String toString() {
		return "SerializableMenu[" +
						"name=" + name + ", " +
						"titleString=" + titleString + ", " +
						"capacity=" + capacity + ", " +
						"content=" + content + ']';
	}
	
}
	