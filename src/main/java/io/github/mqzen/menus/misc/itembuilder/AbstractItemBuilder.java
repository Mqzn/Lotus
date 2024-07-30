package io.github.mqzen.menus.misc.itembuilder;

import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.ButtonClickAction;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

@Getter
public abstract class AbstractItemBuilder {
	private final ItemStack itemStack;

	public AbstractItemBuilder(Material material) {
		this(material, 1);
	}
	
	public AbstractItemBuilder(Material material, int amount) {
		if (amount > 64)
			throw new IllegalArgumentException("amount cannot be bigger than 64");
		if (amount <= 0)
			throw new IllegalArgumentException("amount cannot be smaller than 1");
		
		this.itemStack = new ItemStack(material, amount);

	}
	
	
	public ItemStack build() {
		return itemStack;
	}
	
	public void modify(Consumer<ItemMeta> metaConsumer) {
		if (!itemStack.hasItemMeta())
			return;
		ItemMeta meta = itemStack.getItemMeta();
		metaConsumer.accept(meta);
		itemStack.setItemMeta(meta);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ItemMeta> void modify(Class<T> typeMeta, Consumer<T> metaConsumer) {
		if (!itemStack.hasItemMeta())
			return;
		if (!typeMeta.isInstance(itemStack.getItemMeta())) {
			return;
		}
		T customMeta = (T) itemStack.getItemMeta();
		metaConsumer.accept(customMeta);
		itemStack.setItemMeta(customMeta);
	}
	
	/**
	 * Build the item into a new MenuItem.
	 *
	 * @return the new MenuItem
	 */
	public Button asEmptyButton() {
		return Button.empty(build());
	}
	
	/**
	 * Build the item into a new MenuItem with the provided Click Event.
	 *
	 * @param event the event
	 * @return the new MenuItem
	 */
	public Button asClickable(ButtonClickAction event) {
		return Button.clickable(build(), event);
	}
	
}
