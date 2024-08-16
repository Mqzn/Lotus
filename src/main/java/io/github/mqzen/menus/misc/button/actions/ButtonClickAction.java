package io.github.mqzen.menus.misc.button.actions;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.DataRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Author <a href="https://github.com/Cobeine">Cobeine</a>
 */
public interface ButtonClickAction {
    Pattern pattern = Pattern.compile("\\((.*?)\\)");

    String tag();
    void execute(MenuView<?> menu, InventoryClickEvent event, DataRegistry data);

    default boolean isTargetedAction(String e) {
        return e.startsWith(tag());
    }

    default List<String> getPreferableActions(InventoryClickEvent menu, DataRegistry data) {

        List<String> actions = new ArrayList<>(data.getData("BTN:" + menu.getSlot()));
        actions.removeIf(e  -> !isTargetedAction(e));
        return actions.stream().map(e-> {
            Matcher matcher = pattern.matcher(e);
            if (!matcher.find()) return  null;
            return matcher.group(1);
        }).toList();

    }

    default String getData(String action) {
        return pattern.matcher(action).group();
    }
    static ButtonClickAction plain(ActionExecutor executor) {
        return new ButtonClickAction() {
            @Override
            public String tag() {
                return "";
            }

            @Override
            public void execute(MenuView<?> menu, InventoryClickEvent event, DataRegistry data) {
                executor.execute(menu, event);
            }
        };
    }

    interface ActionExecutor {
        void execute(MenuView<?> menu, InventoryClickEvent event);
    }


}
