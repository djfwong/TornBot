package com.jtorn.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Utility
{
	public void writeFile(String filename, String contents)
	{
		try
		{
			// Create file
			File file = new File(filename);
			File dir = file.getParentFile();
			// Create the directory if it doesnt already exist
			dir.mkdirs();
			file.createNewFile();
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(contents);
			//Close the output stream
			out.close();
		}
		catch (Exception e)
		{
			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public void writeFile(String filename, String contents, boolean append)
	{
		try
		{
			// Create file
			File file = new File(filename);
			File dir = file.getParentFile();
			// Create the directory if it doesnt already exist
			dir.mkdirs();
			file.createNewFile();
			FileWriter fstream = new FileWriter(file,append);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(contents);
			//Close the output stream
			out.close();
		}
		catch (Exception e)
		{
			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
