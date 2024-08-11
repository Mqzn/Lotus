package io.github.mqzen.menus.misc.button;

import io.github.mqzen.menus.base.MenuView;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Button {
	
	private @Nullable ItemStack item;
	private @Nullable ButtonClickAction action = null;
	
	protected Button(@Nullable ItemStack item) {
		this.item = item;
	}
	
	//TODO add animation
	
	public static Button empty(ItemStack item) {
		return new Button(item);
	}
	
	public static Button clickable(ItemStack item, @Nullable ButtonClickAction action) {
		return new Button(item, action);
	}
	
	public static Button transformerButton(ItemStack item, BiFunction<MenuView<?>, InventoryClickEvent, Button> transformer) {
		return new Button(item, (menu, click) -> menu.replaceClickedButton(click, transformer.apply(menu, click)));
	}
	
	public static Button transformerItem(ItemStack item,
	                                     BiFunction<MenuView<?>, InventoryClickEvent, ItemStack> transformer) {
		return new Button(item, (view, click) ->
			view.replaceClickedItemStack(click, transformer.apply(view, click), false));
	}
	
	public boolean isClickable() {
		return action != null;
	}
	
	
	public Button setAction(@Nullable ButtonClickAction action) {
		this.action = action;
		return this;
	}
	
	public Button setItem(@Nullable ItemStack item) {
		this.item = item;
		return this;
	}
	
	public void executeOnClick(MenuView<?> menu, InventoryClickEvent event) {
		if (action != null) action.execute(menu, event);
	}
	
	
}
