package com.jtorn.bot.misc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

public class ThreadProxyDriver 
{
	private static Stack<String> proxyStack;
	private static int count = 0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		proxyStack = new Stack<String>();
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		
		try{
			  // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream("proxyports.txt");
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null)   
			  {
				  String proxy = strLine;
				  proxyStack.push(proxy);
				  System.out.println("proxy: "+proxy);
			  }
			  //Close the input stream
			  in.close();
			    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		
		for (int i = 0; i < 8; i++)
		{
			new Thread() 
			{
				public void run() 
				{
					try
					{
						String port = proxyStack.pop();
						WebClient wc = new WebClient(BrowserVersion.FIREFOX_3_6);
						ProxyConfig proxyConfig = new ProxyConfig("127.0.0.1", Integer.parseInt(port),true);
						wc.setProxyConfig(proxyConfig);
						wc.setJavaScriptEnabled(false);
						HtmlPage page;
						String url = "http://automation.whatismyip.com/n09230945.asp";
						System.out.println("Fetching... " + url);
						while(true)
						{
							count++;
							page = wc.getPage(url);
													
							DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							Date date = new Date();
							
							System.out.println(count + " - " + port + " - " + dateFormat.format(date) + " - " + page.asText());
							Thread.sleep(5*1000 * 60);
						}
					}
					catch (Exception e)
					{
						//System.out.println("THREAD FAILED!");
						//System.out.println(e.toString());
					}
				}
			}.start();

		}
	}

}
