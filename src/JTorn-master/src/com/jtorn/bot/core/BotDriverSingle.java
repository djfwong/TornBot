package com.jtorn.bot.core;

import java.util.logging.Level;

import org.apache.log4j.Logger;

import com.jtorn.bot.core.TornRoutine.RoutineType;


public class BotDriverSingle
{
	
	private static Logger logger = Logger.getLogger(BotDriverSingle.class);
	
	public static void main(String[] args) 
	{
		
		logger.info("Creating the bot threads...");
		TornProxy proxy = null;
		TornUser user = new TornUser("tempaccount", "temp123", "fastslip@gmail.com", proxy);
		String[] botArgs = {TornConstants.united_kingdom, 
						 TornConstants.heather, 
						 Integer.toString(TornConstants.time_united_kingdom)};
		Thread botThread =  new TornBot(user, RoutineType.LEVELUP, null);
	while (true)
	{
		try 
		{
			botThread.start();
			botThread.join();
			//System.out.println("Sleeping 60 minutes...");
			//Thread.sleep(1000*60*60);
		} catch (Exception e) 
		{
			e.printStackTrace();
			botThread =  new TornBot(user, RoutineType.LEVELUP, botArgs);}
			logger.info("Building new bot thread...");
		}
	}
}
