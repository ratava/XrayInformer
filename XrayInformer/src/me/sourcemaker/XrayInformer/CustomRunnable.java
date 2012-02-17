/**
 * XrayInformer - A warning solution especially against xray-using players
 *
 * The XRI informs about farmrates and their possibilities. It's useful for every serveradmin
 *
 * @package    me.sourcemaker.XrayInformer
 * @author     sourcemaker
 * @copyright  widecraft.de
 * @license    Attribution-NonCommercial-NoDerivs 3.0 Unported (CC BY-NC-ND 3.0) (http://creativecommons.org/licenses/by-nc-nd/3.0/)
 * @version    Release: 2.1.6
 * @link       http://dev.bukkit.org/server-mods/xray-informer
 *
 */

package me.sourcemaker.XrayInformer;

import org.bukkit.command.CommandSender;

public class CustomRunnable implements Runnable {
	CommandSender sender;
	String world;
	int oreid;
	String bantype;
	float maxrate;
	boolean banned;

	public CustomRunnable(CommandSender sender, String world, int oreid,
			String bantype, float maxrate, boolean banned) {
		this.sender = sender;
		this.world = world;
		this.oreid = oreid;
		this.bantype = bantype;
		this.maxrate = maxrate;
		this.banned = banned;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}