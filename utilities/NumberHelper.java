package com.utilities;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberHelper {
	public static String timeFormat(double seconds){
		
		int hours = (int) (seconds)/3600;
		int minutes = (int) (seconds - (hours*3600))/60;
		seconds = (int) (seconds - (hours*3600) - (minutes*60));
		
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		
		nf.setMinimumIntegerDigits(2);
		nf.setMaximumIntegerDigits(2);
		nf.setMaximumFractionDigits(0);
		
		return ""+nf.format(hours)+":"+nf.format(minutes)+":"+nf.format(seconds);
	}
}
