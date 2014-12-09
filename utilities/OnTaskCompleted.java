package com.utilities;

public interface OnTaskCompleted {
	void onTaskCompleted(Object object);
	void onTaskCompleted(String id, Object object);
}