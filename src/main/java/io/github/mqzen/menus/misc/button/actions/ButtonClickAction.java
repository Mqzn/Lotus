package io.github.mqzen.menus.misc.button.actions;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.DataRegistry;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 * @author Mqzen (modified after Cobeine)
 */
public interface ButtonClickAction {

    Pattern PATTERN = Pattern.compile("\\((.*?)\\)");

    static ButtonClickAction of(String tag, ActionExecutor executor) {
        return new ButtonClickAction() {
            @Override
            public String tag() {
                return tag;
            }

            @Override
            public void execute(MenuView<?> menu, InventoryClickEvent event) {
                executor.execute(menu, event);
            }
        };
    }

    String tag();
    void execute(MenuView<?> menu, InventoryClickEvent event);

    default boolean isTargetedAction(String e) {
        return e.startsWith(tag());
    }

    default List<String> getPreferableActions(InventoryClickEvent menu, DataRegistry data) {

        List<String> actions = new ArrayList<>(data.getData("BTN:" + menu.getSlot()));
        actions.removeIf(e  -> !isTargetedAction(e));
        return actions.stream().map(e-> {
            Matcher matcher = PATTERN.matcher(e);
            if (!matcher.find()) return  null;
            return matcher.group(1);
        }).toList();

    }

    default @Nullable String getData(String action) {
        Matcher matcher = PATTERN.matcher(action);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    static ButtonClickAction plain(ActionExecutor executor) {
        return ButtonClickAction.of("", executor);
    }

    interface ActionExecutor {
        void execute(MenuView<?> menu, InventoryClickEvent event);
    }


}
