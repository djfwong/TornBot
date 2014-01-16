package com.jtorn.bot.misc;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.Stack;
import java.util.concurrent.Executor;

import com.jtorn.bot.core.TornBot;
import com.jtorn.bot.core.TornProxy;
import com.jtorn.bot.core.TornUser;

public class BotDriver2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println("Creating the bot threads...");
		
		// login routine not robust enough
		
		Properties userProperties = new Properties();
		Properties proxyProperties = new Properties();
		
		try 
		{
			userProperties.load(new FileInputStream("files/java-properties/users.properties"));
			proxyProperties.load(new FileInputStream("files/java-properties/proxies.properties"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		Stack<TornBot> botStack = new Stack<TornBot>();
		int size = botStack.size();
		Collections.shuffle(botStack);
		
		System.out.println("Starting the bot threads...");
		
		try 
		{
			for (int i = 0; i < size; i++)
			{
				TornBot botThread = botStack.pop();
				botThread.start();
				System.out.println((i+1)+"# Bot thread started. Waiting 1/2 minute until next startup...");
				Thread.sleep(1000*30);
			}
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
	}

}
