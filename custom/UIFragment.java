package com.custom;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.widget.Toast;

public class UIFragment extends Fragment {
	public String title = null;
	public int layout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroy() { 
        super.onDestroy();
    }

    @Override
	public void onPause() { 
        super.onPause();
    }

    @Override
	public void onResume() {
        super.onResume();
    }

    @Override
	public void onStart() {
        super.onStart();
    }
    @Override
	public void onStop() {
        super.onStop();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
    public void pr(Object object){
    	Log.d("dx",object.toString());
    }
    public void alert(Object object){
    	Toast.makeText(getActivity(), object.toString(), Toast.LENGTH_SHORT).show();
    }
}
