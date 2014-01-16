package com.jtorn.bot.core;

import java.text.SimpleDateFormat;

public final class TornConstants
{
	public static String captcha = "please validate";
	public static String onThePlane = "you are currently on the plane flying";
	public static String youAreTraveling = "you are travelling...";
	public static String atStoreAbroad = "travel home";
	public static String atHome = "nerve";
	public static String mexico = "2";
	public static String hawaii = "3";
	public static String united_kingdom = "10";
	public static String argentina = "7";
	public static String japan = "5";
	public static String china = "6";
	public static String cayman_islands = "12";
	public static String canada = "9";
	public static String dubai = "11";
	public static String south_africa = "4";
	public static String switzerland = "8";
	public static String dahlia = "260";
	public static String cherry_blossom = "277";
	public static String ceibo_flower = "271";
	public static String african_violet = "282";
	public static String edelweiss = "272";
	public static String tribulus_omanese = "385";
	public static String peony = "276";
	public static String orchid = "264";
	public static String heather = "267";
	public static String banana_orchid = "617";
	public static String crocus = "263";
	public static int time_mexico = 15;
	public static int time_hawaii = 86;
	public static int time_united_kingdom = 108;
	public static int time_argentina = 134;
	public static int time_japan = 143;
	public static int time_china = 154;
	public static int time_cayman_islands = 41;
	public static int time_canada = 27;
	public static int time_dubai = 182;
	public static int time_south_africa = 218;
	public static int time_switzerland = 119;
	public static String timeDateFormat = "HH-mm-ss";
	public static String dayDateFormat = "yy-MM-dd";
	public static String fullDateFormat = "yy-MM-dd-HH-mm-ss";

	public static SimpleDateFormat getTimeDateFormat()
	{
		return new SimpleDateFormat(timeDateFormat);
	}

	public static SimpleDateFormat getDayDateFormat()
	{
		return new SimpleDateFormat(dayDateFormat);
	}

	public static SimpleDateFormat getFullDateFormat()
	{
		return new SimpleDateFormat(fullDateFormat);
	}

}
