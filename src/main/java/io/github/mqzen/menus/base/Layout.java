package io.github.mqzen.menus.base;

import io.github.mqzen.menus.misc.Button;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Layout {
    @NotNull
    private final Map<Character, Button> mappedButtons;

    @NotNull
    private final String[] patterns;

    Layout(@NotNull Map<Character, Button> mappedButtons, final String[] patterns) {
        this.mappedButtons = mappedButtons;
        this.patterns = patterns;
    }

    public Button getMappedButton(int row, int column) {
        Objects.checkFromToIndex(1, 6, row);
        Objects.checkFromToIndex(1, 9, column);

        String pattern = patterns[row];
        return this.mappedButtons.get(pattern.charAt(column));
    }

    @NotNull
    @Contract(pure = true)
    public static LayoutBuilder builder() {
        return new LayoutBuilder();
    }

    public static class LayoutBuilder {
        private final Map<Character, Button> mappedButtons;

        LayoutBuilder() {
            this.mappedButtons = new HashMap<>();
        }

        public LayoutBuilder set(char mapper, Button button) {
            this.mappedButtons.put(mapper, button);
            return this;
        }

        public Layout create(String... patterns) {
            Objects.requireNonNull(patterns, "Pattern array must NOT be null.");
            Objects.checkFromToIndex(1, 6, patterns.length);

            for (String pattern : patterns)
                Objects.checkFromToIndex(1, 9, pattern.length());

            return new Layout(mappedButtons, patterns);
        }
    }
}
