package io.github.mqzen.menus;

import dev.velix.imperat.BukkitImperat;
import io.github.mqzen.menus.base.pagination.Pagination;
import io.github.mqzen.menus.base.pagination.exception.InvalidPageException;
import io.github.mqzen.menus.base.serialization.SerializableMenu;
import io.github.mqzen.menus.base.serialization.impl.SerializedMenuYaml;
import io.github.mqzen.menus.misc.DataRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class LotusExamplePlugin extends JavaPlugin implements CommandExecutor {

	private Lotus lotus;
	private BukkitImperat bukkitImperat;

	@Override
	public void onEnable() {
		lotus = Lotus.load(this);
		lotus.enableDebugger();

		this.bukkitImperat = BukkitImperat.builder(this)
				.build();

		this.getCommand("test").setExecutor(this);
		getDataFolder().mkdirs();
		getCommand("save").setExecutor((commandSender, command, s, strings) -> {
			File exampleFile = new File(getDataFolder(), "example.yml");
			if (!exampleFile.exists()) {
				try {
					exampleFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			ExampleMenu exampleMenu = new ExampleMenu();
			DataRegistry registry = DataRegistry.empty();
			SerializableMenu menu = new SerializableMenu(exampleMenu.getName(), exampleMenu.getTitle(registry, null).asString(),
					exampleMenu.getCapacity(registry, null), exampleMenu.getContent(registry, null, exampleMenu.getCapacity(registry, null)));

			YamlConfiguration configuration = YamlConfiguration.loadConfiguration(exampleFile);
			SerializedMenuYaml yaml = (SerializedMenuYaml) lotus.getMenuIO();
			yaml.write(lotus.getMenuSerializer().serialize(menu), configuration);
			try {
				configuration.save(exampleFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			commandSender.sendMessage("saved");
			return true;
		});
		getCommand("load").setExecutor((commandSender, command, s, strings) -> {
			File exampleFile = new File(getDataFolder(), "example.yml");
			if (!exampleFile.exists()) {
			return true;
			}
			YamlConfiguration configuration = YamlConfiguration.loadConfiguration(exampleFile);
			SerializedMenuYaml yaml = (SerializedMenuYaml) lotus.getMenuIO();

			DataRegistry registry = yaml.read(configuration);
			var  menu = lotus.getMenuSerializer().deserialize(registry);
			lotus.openMenu((Player) commandSender,menu);
			commandSender.sendMessage("loaded");
			return true;
		});
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
