package io.github.mqzen.menus.misc.serializers;

import io.github.mqzen.menus.Lotus;
import io.github.mqzen.menus.base.BaseMenuView;
import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import io.github.mqzen.menus.misc.button.actions.ButtonClickActions;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class MenuSerializer {

    public static void serializeMenu(DataRegistry dataRegistry, Menu menu, FileConfiguration configuration) {
        //DataRegistry dataRegistry = menu.getExtraData();
        var capacity = menu.getCapacity(dataRegistry, null);
        configuration.set("properties.capacity", capacity.getRows());
        configuration.set("properties.title", menu.getTitle(dataRegistry, null).asString());
        YamlConfiguration buttonsSection = new YamlConfiguration();
        var buttonMap = menu.getContent(dataRegistry, null, capacity).getButtonMap();
        for (Slot slot : buttonMap.keySet()) {
            //since there's no input, the default button name will be lowercase ("slot_{num}");
            String key = "slot_" + slot.getSlot();
            buttonsSection.set(key + ".slot",slot.getSlot());
            buttonsSection.set(key + ".item", buttonMap.get(slot).getItem());
            buttonsSection.set(key + ".actions", dataRegistry.getData("BTN:" + slot.getSlot()));
        }
        configuration.set("buttons", buttonsSection);
    }

    public static BaseMenuView<Menu> deserializeMenu(Lotus lotus,String name, FileConfiguration configuration) {
        Capacity capacity = Capacity.ofRows(configuration.getInt("properties.capacity"));
        MenuTitle title = MenuTitles.createLegacy(configuration.getString("properties.title"));
        var buttonsSection = configuration.getConfigurationSection("buttons");
        DataRegistry dataRegistry = DataRegistry.empty();
        Map<Slot, Button> loadedButtons = new HashMap<>();
        if (buttonsSection != null) {
            for (String button : buttonsSection.getKeys(false)) {
                int slot = buttonsSection.getInt(button + ".slot");
                ItemStack item = buttonsSection.getItemStack(button + ".item"); //TODO custom item deSerializer
                List<String> buttonActions = buttonsSection.getStringList(button + ".actions");
                dataRegistry.setData("BTN:" + slot, buttonActions);
                loadedButtons.put(Slot.of(slot), Button.clickable(item, new ButtonClickAction() {
                    @Override
                    public String tag() {
                        return "";
                    }

                    @Override
                    public void execute(MenuView<?> menu, InventoryClickEvent event, DataRegistry data) {
                        //get every custom action and execute them
                        for (String buttonAction : buttonActions) {
                            int index = buttonAction.indexOf("(");
                            String tag = buttonAction.substring(0, index);
                            ButtonClickAction action = ButtonClickActions.getByTag(tag);
                            action.execute(menu,event,data);
                        }
                    }
                }));
            }
        }
        Menu menu = new Menu() {
            @Override
            public String getName() {
                return name;
            }
            @Override
            public @NotNull MenuTitle getTitle(DataRegistry extraData, Player opener) {
                return title;
            }

            @Override
            public @NotNull Capacity getCapacity(DataRegistry extraData, Player opener) {
                return capacity;
            }

            @Override
            public @NotNull Content getContent(DataRegistry extraData, Player opener, Capacity capacity) {
                return Content.builder(capacity).buttons(loadedButtons).build();
            }
        };
        return new BaseMenuView<>(lotus, menu,dataRegistry);
    }

}
