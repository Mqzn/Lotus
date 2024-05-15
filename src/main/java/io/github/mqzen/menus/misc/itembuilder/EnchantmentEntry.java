package io.github.mqzen.menus.misc.itembuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.enchantments.Enchantment;

/**
 * @Author <a href="https://github.com/Cobeine">Cobeine</a>
 */
@Getter
public class EnchantmentEntry {
    private final Enchantment enchantment;
    private final int level;
    private final boolean ignore;

     EnchantmentEntry(Enchantment enchantment, int level, boolean ignore) {
        this.enchantment = enchantment;
        this.level = level;
        this.ignore = ignore;
    }

    public static EnchantmentEntry of(Enchantment enchantment, int level, boolean ignore) {
        return new EnchantmentEntry(enchantment, level, ignore);
    }
    public static EnchantmentEntry of(Enchantment enchantment, int level) {
        return new EnchantmentEntry(enchantment, level,true);
    }
    public static EnchantmentEntry of(Enchantment enchantment) {
        return new EnchantmentEntry(enchantment, 1,true);
    }
    public static EnchantmentEntry of(Enchantment enchantment, boolean ignore) {
        return new EnchantmentEntry(enchantment, 1, ignore);
    }
}
