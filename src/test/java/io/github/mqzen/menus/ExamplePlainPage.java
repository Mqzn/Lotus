package io.github.mqzen.menus;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.pagination.Page;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ExamplePlainPage extends Page {

	private final int amountOfItems;


	public ExamplePlainPage(int amountOfItems) {
		this.amountOfItems = amountOfItems;
	}

	@Override
	public int getPageButtonsCount(@Nullable PageView pageView, Player opener) {
		return amountOfItems;
	}


	@Override
	public ItemStack nextPageItem(Player player) {
		return ItemBuilder.legacy(Material.PAPER)
				  .setDisplay("&aNext page")
				  .build();
	}

	@Override
	public ItemStack previousPageItem(Player player) {
		return ItemBuilder.legacy(Material.PAPER)
				  .setDisplay("&aPrevious page")
				  .build();
	}

	@Override
	public String getName() {
		return "Islands";
	}

	@Override
	public @NotNull MenuTitle getTitle(DataRegistry dataRegistry, Player player) {
		return MenuTitles.createLegacy("Islands");
	}

	@Override
	public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
		return Capacity.ofRows(6);
	}


	@Override
	public @NotNull Content getContent(DataRegistry extraData, Player opener, Capacity capacity) {
		var builder = Content.builder(capacity);
		for(int i = 0; i < amountOfItems; i++) {
			builder.setButton(i, new ExampleMenuComponent("" + i).toButton());
		}
		return builder.build();
	}
}
