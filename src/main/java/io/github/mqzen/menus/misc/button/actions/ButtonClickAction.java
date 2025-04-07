package io.github.mqzen.menus.misc.button.actions;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.DataRegistry;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Interface defining actions triggered by button clicks in a menu.
 * <p>
 * A ButtonClickAction is identified by a distinct tag and executed by providing
 * a specific implementation of the {@link ActionExecutor}.
 * <p>
 * The interface offers utilities to:
 * - Check if a given action string is targeted to the current action
 * - Retrieve preferable actions based on click events and data registry
 * - Extract action-specific data from a given action string
 *
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 * @author Mqzen (modified after Cobeine)
 */
public interface ButtonClickAction {

    /**
     * Pattern for capturing content inside parentheses.
     * Used to extract action-specific data from a given action string.
     */
    Pattern PATTERN = Pattern.compile("\\((.*?)\\)");

    /**
     * Creates a ButtonClickAction with the specified tag and executor.
     *
     * @param tag the tag identifying the button click action
     * @param executor the ActionExecutor responsible for executing the action
     * @return a new ButtonClickAction with the specified tag and executor
     */
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

    /**
     * Retrieves the tag that identifies this ButtonClickAction.
     *
     * @return The tag associated with this action.
     */
    String tag();
    /**
     * Executes an action triggered by a button click in a menu.
     *
     * @param menu  the MenuView instance where the click event occurred
     * @param event the InventoryClickEvent instance representing the click event
     */
    void execute(MenuView<?> menu, InventoryClickEvent event);

    /**
     * Checks if a given action string matches the current action based on its tag.
     *
     * @param e the action string to be checked
     * @return true if the action string starts with the tag of the current action, false otherwise
     */
    default boolean isTargetedAction(String e) {
        return e.startsWith(tag());
    }

    /**
     * Retrieves a list of preferable actions based on the given click event and data registry.
     *
     * @param menu The inventory click event, which provides context such as the slot clicked.
     * @param data The data registry containing potential actions associated with the inventory.
     * @return A list of preferable action strings extracted and processed from the registry.
     */
    default List<String> getPreferableActions(InventoryClickEvent menu, DataRegistry data) {

        List<String> actions = new ArrayList<>(data.getData("BTN:" + menu.getSlot()));
        actions.removeIf(e  -> !isTargetedAction(e));
        return actions.stream().map(e-> {
            Matcher matcher = PATTERN.matcher(e);
            if (!matcher.find()) return  null;
            return matcher.group(1);
        }).collect(Collectors.toList());

    }

    /**
     * Extracts specific data from the given action string based on a predefined pattern.
     *
     * @param action the action string from which data is to be extracted
     * @return the extracted data if the pattern matches, or null otherwise
     */
    default @Nullable String getData(String action) {
        Matcher matcher = PATTERN.matcher(action);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    /**
     * Creates a ButtonClickAction with an empty tag and the specified ActionExecutor.
     *
     * @param executor The ActionExecutor to be executed when the button is clicked.
     * @return A new ButtonClickAction instance with an empty tag and the provided ActionExecutor.
     */
    static ButtonClickAction plain(ActionExecutor executor) {
        return ButtonClickAction.of("", executor);
    }

    /**
     * Represents an executor that defines the action to be performed when a button
     * click event occurs within a menu interface.
     */
    interface ActionExecutor {
        /**
         * Executes an action associated with a button click event within a menu interface.
         *
         * @param menu the menu interface where the button click event occurred
         * @param event the event representing the button click
         */
        void execute(MenuView<?> menu, InventoryClickEvent event);
    }


}
