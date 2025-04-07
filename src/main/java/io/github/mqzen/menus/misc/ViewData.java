package io.github.mqzen.menus.misc;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.titles.MenuTitle;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * ViewData is a record that encapsulates information about a menu view. It includes
 * a menu title, capacity, and content. This record serves as a container to hold
 * all necessary details for rendering a menu view in a user interface.
 */
@Data @RequiredArgsConstructor
public final class ViewData {

    final MenuTitle title;
    final Capacity capacity;
    final Content content;

}
