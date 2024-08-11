package io.github.mqzen.menus;

import io.github.mqzen.menus.base.pagination.Pagination;
import io.github.mqzen.menus.base.pagination.exception.InvalidPageException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class LotusExamplePlugin extends JavaPlugin implements CommandExecutor {

	private Lotus lotus;
	@Override
	public void onEnable() {
		lotus = new Lotus(this, EventPriority.LOW);
		this.getCommand("test").setExecutor(this);
	}


	public final static List<ExampleMenuComponent> islands = new ArrayList<>();
	static {
		for (int i = 0; i < 50; i++) {
			islands.add(new ExampleMenuComponent(i + ""));
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
	                         String label, String[] args) {

		if(args.length == 0) {
			lotus.openMenu((Player) sender, new ExampleMenu());
			return true;
		}
		if(args.length != 1) {
			return false;
		}


		String sub = args[0];
		Pagination pagination;
		if(sub.equalsIgnoreCase("auto")) {
			pagination = Pagination.auto(lotus)
					  .creator(new ExampleAutoPage())
					  .componentProvider(()-> islands)
					  .build();
		}else {
			pagination = Pagination.plain(lotus)
					  .page(0, new ExamplePlainPage(10))
					  .page(1, new ExamplePlainPage(20))
					  .build();
		}


		try {
			pagination.open((Player) sender);
		} catch (InvalidPageException e) {
			throw new RuntimeException(e);
		}
		return true;
	}
}
