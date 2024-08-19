package io.github.mqzen.menus.misc.button.actions;

import io.github.mqzen.menus.misc.button.actions.impl.CloseMenuAction;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 * @author Mqzen (modifed after Cobeine)
 */

public final class ButtonClickActions {

    private ButtonClickActions() {
        throw new IllegalAccessError();
    }
    
    public final static ButtonClickAction CLOSE_INVENTORY = new CloseMenuAction();
}
