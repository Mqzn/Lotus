package io.github.mqzen.menus;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ExampleMenu implements Menu {

	private final ItemStack[] fruits =  {
			  ItemBuilder.legacy(Material.APPLE).build(),
			  ItemBuilder.legacy(Material.CARROT_ITEM).build(),
			  ItemBuilder.legacy(Material.GOLDEN_APPLE).build(),
			  ItemBuilder.legacy(Material.GOLDEN_CARROT).build()
	};
	private int index = 0;
	/**
	 * @return The unique name for this menu
	 */
	@Override
	public String getName() {
		return "example menu";
	}

	/**
	 * @param extraData the data container for this menu for extra data
	 * @param opener    the player who is opening this menu
	 * @return the title for this menu
	 */
	@Override
	public @NotNull MenuTitle getTitle(DataRegistry extraData, Player opener) {
		return MenuTitles.createLegacy("&cExample Menu");
	}

	/**
	 * @param extraData the data container for this menu for extra data
	 * @param opener    the player who is opening this menu
	 * @return the capacity/size for this menu
	 */
	@Override
	public @NotNull Capacity getCapacity(DataRegistry extraData, Player opener) {
		return Capacity.ofRows(3);//Alternative approach: Capacity.ofRows(3)
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
	public @NotNull Content getContent(DataRegistry extraData,
	                                   Player opener, Capacity capacity) {

		Button borderPane = Button.clickable(
				ItemBuilder.legacy(Material.STAINED_GLASS_PANE, 1, (short) 5).build(),
				ButtonClickAction.plain((menuView, event) -> {
							event.setCancelled(true);
							//we want nothing to happen here
						}
				));
		Button transformingFruit = Button.transformerItem(fruits[0], (menuView, event)-> {
			event.setCancelled(true);
			index++;
			if(index >= fruits.length) {
				index = 0;
			}
			return fruits[index];
		});

		return Content.builder(capacity)
				  .apply(content -> {
					  content.fill(transformingFruit);
					  content.fillBorder(borderPane);
				  })
				  .build();
	}
}
