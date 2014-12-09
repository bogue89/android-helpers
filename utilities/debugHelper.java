package com.utilities;

import android.util.Log;

public class debugHelper {
	public static void NSLog(Object... objects){
		for (int i = 0; i < objects.length; ++i) {
			if(objects[i] != null){
				Log.d("dx",objects[i].toString());
			} else {
				Log.d("dx","null");
			}
		}
	}
}
