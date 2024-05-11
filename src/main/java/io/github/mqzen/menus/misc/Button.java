package io.github.mqzen.menus.misc;

import io.github.mqzen.menus.base.PlayerMenu;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Button {
	
	private @Nullable ItemStack item;
	private @Nullable ButtonAction action = null;
	
	protected Button(@Nullable ItemStack item) {
		this.item = item;
	}
	
	//TODO add animation
	
	public static Button empty(ItemStack item) {
		return new Button(item);
	}
	
	public static Button clickable(ItemStack item, @Nullable ButtonAction action) {
		return new Button(item, action);
	}
	
	public static Button transformerButton(ItemStack item, BiFunction<PlayerMenu<?>, InventoryClickEvent, Button> transformer) {
		return new Button(item, (menu, click)-> menu.setClickedButton(click, transformer.apply(menu, click)));
	}
	
	public static Button transformerItem(ItemStack item,
	                                     BiFunction<PlayerMenu<?>, InventoryClickEvent, ItemStack> transformer) {
		return new Button(item, (menu, click)->
			menu.setClickedItem(click, transformer.apply(menu, click)));
	}
	
	public boolean isClickable() {
		return action != null;
	}
	
	public void executeOnClick(PlayerMenu<?> menu, InventoryClickEvent event) {
		if(action != null) action.execute(menu, event);
	}
	
	
}
