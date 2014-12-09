package com.custom;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class UIActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	@Override
    protected void onDestroy() { 
        super.onDestroy();
    }

    @Override
    protected void onPause() { 
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    public void pr(Object object){
    	Log.d("dx",object.toString());
    }
    public void alert(Object object){
    	Toast.makeText(this, object.toString(), Toast.LENGTH_SHORT).show();
    }
}
