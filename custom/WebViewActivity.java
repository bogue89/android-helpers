package com.utilities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class WebViewActivity extends Activity implements OnTaskCompleted {
	
	int apiVersion = 0;
	Activity activity;
	public int mainView = 0;
	public int layout = 0;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainView = this.getIntent().getExtras().getInt("view");
        apiVersion = android.os.Build.VERSION.SDK_INT;
        
        layout = this.getIntent().getExtras().getInt("layout");
        title = this.getIntent().getExtras().getString("title");
        content = this.getIntent().getExtras().getString("content");
        setContentView(layout);
        activity = this;
        apiVersion = android.os.Build.VERSION.SDK_INT;
        setContent();
    }
    @Override
    public void onResume(){
    	super.onResume();
    }
    /*Helpers*/
    public int getStatusBarHeight() {
    	int result = 0;
    	int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
    	if (resourceId > 0) {
    		result = activity.getResources().getDimensionPixelSize(resourceId);
    	}
    	
    	return result;
	}
    public int getMainViewHeight() {
    	int result = 0;
    	int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
    	if (resourceId > 0) {
    		result = activity.getResources().getDimensionPixelSize(resourceId);
    	}
    	return result;
	}
    public void pr(){
		Log.d("dx","");
	}
    public void pr(String str){
		Log.d("dx",str);
	}
	public void pr(int str){
		Log.d("dx",""+str);
	}
	public void pr(float str){
		Log.d("dx",""+str);
	}
	public void pr(boolean str){
		Log.d("dx",""+str);
	}
	public void pr(double str){
		Log.d("dx",""+str);
	}
	public void navigationBarSetStyle(boolean primary) {
    	/*
    	if (apiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
    		if(primary){
    			Drawable background = activity.getResources().getDrawable(R.drawable.navbarbackground);
            	Drawable title = activity.getResources().getDrawable(R.color.color_black);
            	activity.getActionBar().setBackgroundDrawable(background);
            	activity.getActionBar().setIcon(title);
            	activity.getActionBar().setTitle("");
    		} else {
    			Drawable background = activity.getResources().getDrawable(R.drawable.navbarbackground);
            	Drawable none = activity.getResources().getDrawable(R.drawable.none);
            	activity.getActionBar().setBackgroundDrawable(background);
            	activity.getActionBar().setIcon(none);
    		}
    	} else{
    	}
    	*/
    }
	
    /*Methods*/
	
	public String title;
	public String content;
	public String url;
	
    public void setContent(){
    	System.gc();
        this.navigationBarSetStyle(false);
        load();
        System.gc();
    }
    
	public void load() {
		setTitle(title);
		
		WebView web = (WebView) findViewById(mainView);
		
		String summary = "<html><body>"+content+"</body></html>";
		web.loadData(summary, "text/html; charset=UTF-8", null);
    }
	public void onTaskCompleted(Object object) {
		
	}
	@Override
	public void onTaskCompleted(String id, Object object) {
		// TODO Auto-generated method stub
		
	}
}