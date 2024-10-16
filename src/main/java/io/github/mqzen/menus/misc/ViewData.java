package io.github.mqzen.menus.misc;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.titles.MenuTitle;

/**
 * ViewData is a record that encapsulates information about a menu view. It includes
 * a menu title, capacity, and content. This record serves as a container to hold
 * all necessary details for rendering a menu view in a user interface.
 */
public record ViewData(MenuTitle title, Capacity capacity, Content content) {
}
