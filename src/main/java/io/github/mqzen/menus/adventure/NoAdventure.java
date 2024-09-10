package io.github.mqzen.menus.adventure;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.ComponentLike;

public final class NoAdventure<S> implements AdventureProvider<S> {

    @Override
    public Audience audience(final S sender) {
        throw new UnsupportedOperationException("No Audience provider has been supplied, Check if kyori has been loaded correctly on your java runtime");
    }
    
    @Override
    public void send(final S sender, final ComponentLike component) {
        throw new UnsupportedOperationException("No Audience provider has been supplied, Check if kyori has been loaded correctly on your java runtime");
    }
    

}
