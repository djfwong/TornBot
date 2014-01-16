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

import org.cyberneko.html.HTMLElements;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

public class ThreadStock 
{
	private static Stack<String> stockSymbolStack;
	private static int count = 0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		stockSymbolStack = new Stack<String>();
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		
		try{
			  // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream("stocksymbols.txt");
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null)   
			  {
				  String strStockSymbol = strLine;
				  stockSymbolStack.push(strStockSymbol);
				  System.out.println("Stock Symbol: "+strStockSymbol);
			  }
			  //Close the input stream
			  in.close();
			    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		
		for (int i = 0; i < 10; i++)
		{
			new Thread() 
			{
				public void run() 
				{
					try
					{
						WebClient wc = new WebClient(BrowserVersion.FIREFOX_3_6);
						wc.setJavaScriptEnabled(false);
						HtmlPage page;
						String strStockSymbol = stockSymbolStack.pop();
						String url = "http://ca.finance.yahoo.com/q?s="+strStockSymbol;
						//String url = "http://ca.search.yahoo.com/search?p="+stockSymbolStack.pop()+"&fr=sfp&fr2=&iscqry=";
						System.out.println("Fetching... " + url);
						page = wc.getPage(url);
						//HtmlElement element = page.getHtmlElementById("yfi_rt_quote_summary_rt_top");
						while(true)
						{
							count++;
							HtmlElement element = (HtmlElement)page.getByXPath("/html/body/div[3]/div/div[3]/div[2]/div/div/div[2]").get(0);
													
							DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							Date date = new Date();
							
							System.out.println(count + " - " + strStockSymbol + " - " + dateFormat.format(date) + " - " + element.asText());
							//Thread.sleep(100);
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
