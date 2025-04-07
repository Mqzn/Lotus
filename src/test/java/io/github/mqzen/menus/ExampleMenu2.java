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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class ExampleMenu2 implements Menu {
    @Override
    public String getName() {
        return "menu2";
    }

    @Override
    public @NotNull MenuTitle getTitle(DataRegistry extraData, Player opener) {
        return MenuTitles.createLegacy("Hii");
    }

    @Override
    public @NotNull Capacity getCapacity(DataRegistry extraData, Player opener) {
        return Capacity.ofRows(3);
    }

    @Override
    public @NotNull Content getContent(DataRegistry extraData, Player opener, Capacity capacity) {
        Content.Builder b = Content.builder(capacity);
        b.apply(content -> {
            for(ChatColor color : ChatColor.values()) {
                content.addButton(Button.clickable(ItemBuilder.legacy(Material.NAME_TAG).setDisplay(color.name()).build(), ButtonClickAction.plain((menu, event)-> {
                    event.setCancelled(true);
                    opener.closeInventory();
                    opener.sendMessage("Color clicked= " + color + color.name());
                })));
            }
        });
        return b.build();
    }
}
