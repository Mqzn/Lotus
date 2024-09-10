package io.github.mqzen.menus;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.base.animation.AnimatedButton;
import io.github.mqzen.menus.misc.Slot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public final class MenuUpdateTask extends BukkitRunnable {
    private final Lotus lotus;
    private MenuUpdateTask(Lotus lotus) {
        this.lotus = lotus;
    }
    
    static MenuUpdateTask newTask(Lotus lotus) {
        return new MenuUpdateTask(lotus);
    }
    
    @Override
    public void run() {
        for(MenuView<?> menuView : lotus.getOpenViews()) {
            var buttons = getAnimatedButtons(menuView);
            buttons.parallelStream().forEach((animatedButton)->
                    menuView.updateButton(animatedButton.slot, (b)-> ((AnimatedButton)b).animate(animatedButton.slot, menuView)));
        }
    }
    private Set<AnimatedButtonEntry> getAnimatedButtons(MenuView<?> view) {
        Set<AnimatedButtonEntry> buttons = new HashSet<>();
        for(var entry : view.getContent().getButtonMap().entrySet()) {
            if(entry.getValue() instanceof AnimatedButton animatedButton) {
                buttons.add(new AnimatedButtonEntry(entry.getKey(), animatedButton));
            }
        }
        return buttons;
    }
    
    private record AnimatedButtonEntry(Slot slot, AnimatedButton button) {
    
    }
}
