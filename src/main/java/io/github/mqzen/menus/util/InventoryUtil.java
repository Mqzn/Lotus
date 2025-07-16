package io.github.mqzen.menus.util;

import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Method;

public final class InventoryUtil {

    private static final Method GET_INVENTORY_VIEW, GET_TOP_INVENTORY;

    static {
        try {
            GET_INVENTORY_VIEW = InventoryEvent.class.getMethod("getView");

            Class<?> inventoryViewClass = Class.forName("org.bukkit.inventory.InventoryView");
            GET_TOP_INVENTORY = inventoryViewClass.getMethod("getTopInventory");

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Inventory getTopInventory(final InventoryEvent event) {
        try {
            Object view = GET_INVENTORY_VIEW.invoke(event);
            return (Inventory) GET_TOP_INVENTORY.invoke(view);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get top inventory from event", e);
        }
    }

}
