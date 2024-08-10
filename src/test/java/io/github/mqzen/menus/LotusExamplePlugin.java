package io.github.mqzen.menus;

import io.github.mqzen.menus.base.pagination.Pagination;
import io.github.mqzen.menus.base.pagination.exception.PageDoesntExistException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class LotusExamplePlugin extends JavaPlugin implements CommandExecutor {

	private Lotus lotus;
	@Override
	public void onEnable() {
		lotus = new Lotus(this, EventPriority.LOW);
		this.getCommand("test").setExecutor(this);
	}


	@Override
	public boolean onCommand(CommandSender sender, Command command,
	                         String label, String[] args) {

		Pagination pagination = Pagination.auto(lotus)
				  .creator(new IslandsMenu())
				  .componentProvider(()->
						    Arrays.asList(new IslandMenuComponent("a"),
								      new IslandMenuComponent("b"),
								      new IslandMenuComponent("c"),
								      new IslandMenuComponent("d")))
				  .build();

		try {
			pagination.open((Player) sender);
		} catch (PageDoesntExistException e) {
			throw new RuntimeException(e);
		}
		return true;
	}
}
