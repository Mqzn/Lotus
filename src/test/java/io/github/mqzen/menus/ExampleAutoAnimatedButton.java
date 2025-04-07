package io.github.mqzen.menus;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.base.animation.AnimatedButton;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ExampleAutoAnimatedButton extends Button implements AnimatedButton {

    public ExampleAutoAnimatedButton() {
        super(ItemBuilder.legacy(Material.EMERALD).setDisplay(frames[0]).setLore(Collections.singletonList(loreFrames[0])).build());
        this.data.setData("frame", 0);
    }

    public ExampleAutoAnimatedButton(@Nullable ItemStack item, @Nullable ButtonClickAction action, DataRegistry data) {
        super(item, action, data);
    }

    private final static String[] frames = {
            "§a❈ §b§lMAGIC §a❈",
            "§b❈ §a§lMAGIC §b❈",
            "§e❈ §6§lMAGIC §e❈",
            "§6❈ §e§lMAGIC §6❈"
    };

    private final static String[] loreFrames = {
            "§7✧ Click to activate",
            "§7✧ Click to enchant",
            "§7✧ Click to cast",
            "§7✧ Click to conjure"
    };


    public static ItemStack newItem(String[] frames, int frame) {
        return ItemBuilder.legacy(Material.EMERALD)
                .setDisplay(frames[frame])
                .setLore(Collections.singletonList(loreFrames[frame])).build();
    }

    @Override
    public ItemStack getCurrentItem() {
        return this.getItem();
    }

    @Override
    public void animate(Slot slot, @NotNull MenuView<?> view) {
        Integer frame = data.getData("frame");
        frame = (frame + 1) % frames.length;

        System.out.println("Frame= " + frame);
        data.setData("frame", frame);
        this.setItem(newItem(frames, frame));
    }

    @Override
    public Button copy() {
        return new ExampleAutoAnimatedButton(this.getItem(), this.getAction(), this.data);
    }
}
