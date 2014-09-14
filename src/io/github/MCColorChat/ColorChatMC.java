package io.github.MCColorChat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class ColorChatMC extends JavaPlugin implements Listener
{
	public static final Logger cc = Logger.getLogger("CC");
	public static Map<String, Object> colorcodes = new HashMap<String, Object>();
	
	@Override
	public void onEnable()
	{
		PluginDescriptionFile file = this.getDescription();
		this.cc.info("[" + "CCC" + "]" + " Custom Color Chat" + " V" + file.getVersion() + " Enabled");
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		try
		{
			colorcodes = this.getConfig().getConfigurationSection("ColorChat.data").getValues(false);
		}catch (NullPointerException e){}
	}
	
	@Override
	public void onDisable()
	{
		PluginDescriptionFile file = this.getDescription();
		this.cc.info("[" + "CCC" + "]" + " Custom Color Chat" + " V" + file.getVersion() + " Disabled");
		this.getConfig().createSection("ColorChat.data", colorcodes);
		this.saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args)
	{
		Player player = (Player) sender;
		if(cmdLabel.equalsIgnoreCase("cc") || cmdLabel.equalsIgnoreCase("colorchat"))
		{
			if(args.length > 0)
			{
				if(args.length > 1)
				{
					if(player.hasPermission("colorchat.others") || player.hasPermission("colorchat.*"))
					{
						if(Bukkit.getServer().getPlayer(args[1]) != null)
						{
							colorcodes.put(Bukkit.getServer().getPlayer(args[1]).getName(), ChatColor.translateAlternateColorCodes('&', args[0]));
							player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[ColorChat] " + Bukkit.getServer().getPlayer(args[1]).getName() + "'s color has been set to " + ChatColor.translateAlternateColorCodes('&', args[0]) + "this.");
						}else
						{
							player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[ColorChat] Player not found");
						}
					}
				}else
				{
					if(player.hasPermission("colorchat.self") || player.hasPermission("colorchat.*"))
					{
						System.out.println(ChatColor.translateAlternateColorCodes('&', args[0]));
						colorcodes.put(player.getName(), ChatColor.translateAlternateColorCodes('&', args[0]));
						player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[ColorChat] " + player.getName() + "'s color has been set to " + ChatColor.translateAlternateColorCodes('&', args[0]) + "this.");
					}
				}
			}else
			{
				player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[ColorChat] Usage: /chatcolor <color> <player>");
			}
		}
		return false;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		if(colorcodes.get(e.getPlayer().getName()) != null)
		{
			e.setMessage(colorcodes.get(e.getPlayer().getName()) + e.getMessage());
		}
	}
}

