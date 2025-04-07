package io.github.mqzen.menus;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.base.animation.AnimatedButton;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class MenuUpdateTask extends BukkitRunnable {
    private final Lotus lotus;
    private MenuUpdateTask(Lotus lotus) {
        this.lotus = lotus;
    }
    
    static MenuUpdateTask newTask(Lotus lotus) {
        return new MenuUpdateTask(lotus);
    }
    
    @Override
    public void run() {
        try {
            for (MenuView<?> menuView : lotus.getOpenViews()) {
                Set<AnimatedButtonEntry> buttons = getAnimatedButtons(menuView);
                buttons.parallelStream().forEach((animatedButton) ->
                        menuView.updateButton(animatedButton.slot, (b) -> ((AnimatedButton) b).animate(animatedButton.slot, menuView)));
            }
        }catch (Throwable ex) {
            lotus.debugger.error("Failed to continue the menu update task", ex);
            this.cancel();
        }
    }
    private Set<AnimatedButtonEntry> getAnimatedButtons(MenuView<?> view) {
        Set<AnimatedButtonEntry> buttons = new HashSet<>();
        for(Map.Entry<Slot, Button> entry : view.getContent().getButtonMap().entrySet()) {
            if(entry.getValue() instanceof AnimatedButton) {
                AnimatedButton animatedButton = (AnimatedButton)entry.getValue();
                buttons.add(new AnimatedButtonEntry(entry.getKey(),  animatedButton));
            }
        }
        return buttons;
    }

    @Data @AllArgsConstructor
    static final class AnimatedButtonEntry {
        final Slot slot;
        final AnimatedButton button;
    }
}
