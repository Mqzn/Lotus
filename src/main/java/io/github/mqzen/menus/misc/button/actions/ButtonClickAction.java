package io.github.mqzen.menus.misc.button.actions;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.DataRegistry;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 * @author Mqzen (modified after Cobeine)
 */
public interface ButtonClickAction {
    
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
    
    Pattern PATTERN = Pattern.compile("\\((.*?)\\)");

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

    default String getData(String action) {
        return PATTERN.matcher(action).group();
    }
    static ButtonClickAction plain(ActionExecutor executor) {
        return ButtonClickAction.of("", executor);
    }

    interface ActionExecutor {
        void execute(MenuView<?> menu, InventoryClickEvent event);
    }


}
