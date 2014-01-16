package com.jtorn.bot.core;

import com.jtorn.bot.core.TornRoutine.RoutineType;


public class BotDriverSinglePublicSimple
{
	public static void main(String[] args) 
	{
		System.out.println("Creating the bot threads...");
		TornProxy proxy;
		if (args[3] == null || args[4] == null)
			proxy = null;
		else
			proxy = new TornProxy(args[3], args[4], "754d539bc6ade1ad", "454585f105747a76");
		TornUser user = new TornUser(args[0], args[1], args[2], proxy);
		Thread botThread =  new TornBot(user, RoutineType.SIMPLE, null);
		botThread.start();
	}
}
