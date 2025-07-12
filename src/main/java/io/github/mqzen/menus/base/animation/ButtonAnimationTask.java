package io.github.mqzen.menus.base.animation;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.Slot;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class ButtonAnimationTask extends BukkitRunnable {
    
    private final @NotNull Slot slot;
    private final @NotNull AnimatedButton button;
    private final @NotNull MenuView<?> view;
    
    private ButtonAnimationTask (
            @NotNull Slot slot,
            @NotNull AnimatedButton button,
            @NotNull MenuView<?> view
    ) {
        this.slot = slot;
        this.button = button;
        this.view = view;
    }
    
    @Override
    public void run() {
        if(!view.isOpen()) {
            this.cancel();
            return;
        }
        
        button.animate(slot, view);
    }
    
    public static ButtonAnimationTask of(
            Slot slot,
            AnimatedButton button,
            MenuView<?> view
    ) {
        return new ButtonAnimationTask(slot, button, view);
    }
    
    
    public void start(Plugin plugin) {
        
        AnimationTaskData taskData = button.getAnimationTaskData();
        if (taskData.isAsync()) {
            runTaskTimerAsynchronously(plugin, taskData.getDelay(), taskData.getTicks());
        } else {
            runTaskTimer(plugin, taskData.getDelay(), taskData.getTicks());
        }
        
    }
}
