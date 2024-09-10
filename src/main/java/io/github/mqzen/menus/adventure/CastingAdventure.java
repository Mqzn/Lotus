package io.github.mqzen.menus.adventure;

import net.kyori.adventure.audience.Audience;

public final class CastingAdventure<S> implements AdventureProvider<S> {

    @Override
    public Audience audience(final S sender) {
        return (Audience) sender;
    }

}
