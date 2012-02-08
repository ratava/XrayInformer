package me.sourcemaker.XrayInformer;

import org.bukkit.command.CommandSender;

public class CustomRunnable implements Runnable {
	CommandSender sender;
	String world;
	int oreid;
	String bantype;
	float maxrate;

	public CustomRunnable(CommandSender sender, String world, int oreid,
			String bantype, float maxrate) {
		this.sender = sender;
		this.world = world;
		this.oreid = oreid;
		this.bantype = bantype;
		this.maxrate = maxrate;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}