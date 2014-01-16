package com.jtorn.bot.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jtorn.bot.core.TornAction;
import com.jtorn.util.Utility;

public class PtcInvestigator
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		//scrape();
		parse();
	}
		
	public static void format()
	{
		File file = new File("C:/file.txt");
	    int ch;
	    StringBuffer strContent = new StringBuffer("");
	    FileInputStream fin = null;
	    try {
	      fin = new FileInputStream(file);
	      while ((ch = fin.read()) != -1)
	        strContent.append((char) ch);
	      fin.close();
	    } catch (Exception e) {
	      System.out.println(e);
	    }
	    System.out.println(strContent.toString());
	}
	
	public static void valueMapper(ArrayList<String> values, String site)
	{
		String filename = "D:/workspace/JTorn/files/formatted-table.txt";
		Utility util = new Utility();
		ArrayList<String> filteredList = new ArrayList<String>();
		String contents = "";
		
		if (values.size() == 12)
		{
			contents = "12|"+site;
			for (String s: values)
				contents += "|"+s;
			System.out.format("Country: %s%n",values.get(0));
			System.out.format("PPC: %s%n",values.get(1));
			System.out.format("Payout: %s%n",values.get(2));
			System.out.format("Delivery: %s%n",values.get(3));
			System.out.format("Ads: %s%n",values.get(4));
			System.out.format("Daily $: %s%n",values.get(5));
			System.out.format("Referrals: %s%n",values.get(6));
			System.out.format("Inactivity: %s%n",values.get(7));
			System.out.format("Timer: %s%n",values.get(8));
			System.out.format("Days/Cashout: %s%n",values.get(9));
			System.out.format("PPR: %s%n",values.get(10));
			System.out.format("Script: %s%n",values.get(11));
			
		}
		else if (values.size() == 15)
		{
			for (String v: values)
			{
				if (v.equals("Who Can Join?") ||
					v.equals("Cashout") ||
					v.equals("Wait Time") ||
					v.equals("Methods") ||
					v.equals("Per Ad") ||
					v.equals("# of Ads") ||
					v.equals("Daily Avg") ||
					v.equals("Referrals"))
				;
				else
					filteredList.add(v);
			}
			valueMapper(filteredList, site);
		}
		else if (values.size() == 7)
		{
			contents = "7|"+site;
			for (String s: values)
				contents += "|"+s;
			System.out.format("Country: %s%n",values.get(0));
			System.out.format("Payout: %s%n",values.get(1));
			System.out.format("Delivery: %s%n",values.get(2));
			System.out.format("PPC: %s%n",values.get(3));
			System.out.format("Ads: %s%n",values.get(4));
			System.out.format("Daily: %s%n",values.get(5));
			System.out.format("Referrals: %s%n",values.get(6));
		}
		else
		{
			System.out.println("ERROR MAPPING VALUES");
			contents = "?|"+site;
			for (String s: values)
				contents += "|"+s;
			for (String v: values)
				System.out.println(v);
		}
		util.writeFile(filename, "@@"+contents, true);
	}
	
	public static void parse() throws Exception
	{
		WebClient wc = new WebClient(BrowserVersion.FIREFOX_3_6);
		wc.setJavaScriptEnabled(false);
		
		File dir = new File("ptc");

    	File[] files = dir.listFiles();
		
		
		for (File f: files)
		{
			ArrayList<String> values = new ArrayList<String>();
			System.out.println("file://"+f.getAbsolutePath());
			HtmlPage page = wc.getPage("file://"+f.getAbsolutePath());
			List<HtmlElement> rowList = page.getElementsByTagName("tr");
			for (int i = 0; i < rowList.size(); i++)
			{
				//if ((i % 2) == 1)
				{
					HtmlElement row = rowList.get(i);
					List<HtmlElement> colList = row.getElementsByTagName("td");
					for (int j = 0; j < colList.size(); j++)
					{
						String cell = colList.get(j).asText().trim();
						if (!cell.isEmpty())
							values.add(cell);
						//System.out.println(i+","+j+": "+cell);
					}
				}
			}
			valueMapper(values,f.getName().replace(".html", ""));
			System.out.println("---------------------------------------------");
		}
	}
	
	public static void scrape() throws Exception
	{
		WebClient wc = new WebClient(BrowserVersion.FIREFOX_3_6);
		wc.setJavaScriptEnabled(false);
		
		HtmlPage page = wc.getPage("http://www.ptc-investigation.com/legit.aspx");
		HtmlElement e = (HtmlElement) page.getByXPath("/html/body/div/div/div/div[6]/div/div/div/div/table").get(0);

		for (HtmlElement e0: e.getElementsByTagName("a"))
		{
			String name = "ptc/"+e0.asText()+".html";
			File f = new File(name);
			if (!f.exists()) 
			{
				String link = e0.getAttribute("href");
				if (!link.contains("http://ptc-investigation.com"))
				{
					if (link.startsWith("/"))
						link = "http://ptc-investigation.com/pages" + link;
					else
						link = "http://ptc-investigation.com/pages/" + link;
				}
				link = link.replace("pages/pages/", "pages/");
				System.out.println(name);
				System.out.println(link);
				HtmlPage page2;
				try
				{
				page2 = wc.getPage(link);
				} catch (Exception ex)
				{
					page2 = wc.getPage(link.replace("pages/",""));
				}
				//System.out.println(page2.asXml());
				List<HtmlElement> eles;
				eles = (List<HtmlElement>) page2.getByXPath("//table[@class='iws_table']");
				if (eles == null || eles.isEmpty())
					eles = (List<HtmlElement>) page2.getByXPath("//table[@class='style7']");
				HtmlElement e2 = eles.get(0);
				//System.out.println(e2.asText());
				Utility util = new Utility();
				util.writeFile("ptc/"+name+".html", e2.asXml());
			}
			else
				System.out.println(name+" already exists.");
		}
	}

}
