package com.jtorn.scraper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FlowerParser
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			String path = "files/torn-flower-thread"; 
			 
		    File folder = new File(path);
		    File[] files = folder.listFiles(); 
			int count = 0;
		    for (int i = 0; i < files.length; i++)
		    {
				//String filename = "files/torn-flower-thread/page-9900.html";
				File htmlFile = files[i];
				Document doc = Jsoup.parse(htmlFile, "UTF-8");
				Elements elements = doc.getElementsByTag("table").get(2).getElementsByTag("table").first().getElementsByTag("tr");
				for (Element e: elements)
				{
					if (e.text().toLowerCase().matches("(.*(flower|heather|ceibo|tribulus|omanese|cherry|blossom|dahlia|orchid|peony|banana|african|violet|crocus|edelweiss|mexico|canada|hawaii|united|kingdom|uk|argentina|switzerland|japan|china|south|africa|dubai|uae|cayman|island|stock).*)") &&
						!e.text().toLowerCase().contains(">>general discussion>>"))
					{
						count++;
						System.out.println(count + " " + e.text());
						//System.out.println("------------------------------------------------------------------");
					}
				}
		    }
				
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
