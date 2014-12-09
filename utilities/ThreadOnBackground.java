package com.utilities;

import android.os.AsyncTask;
import android.util.Log;


public class ThreadOnBackground extends AsyncTask<Runnable, Void, Void> {
	
	private Runnable runnable;
	private Runnable runnableFinally;
	private OnTaskCompleted delegate = null;
	
	public ThreadOnBackground(OnTaskCompleted delegate, Runnable... run){
		this.delegate = delegate;
		runnable = run.length>0 ? run[0] : null;
		runnableFinally = run.length>1 ? run[1] : null;
	}
	public void start(){
		if(runnable != null){
			final Thread thread = new Thread() {
		        @Override
		        public void run() {
		            try {
		            	runnable.run();
		            } finally {
		            	if(runnableFinally != null){
		            		runnableFinally.run();
		            	}
		            }
		        }
		    };
		    thread.start();
		}
	}
	@Override
	protected Void doInBackground(Runnable... run) {
		
		runnable = runnable == null && run.length>0 ? run[0]:runnable;
		runnableFinally = runnableFinally == null && run.length>1 ? run[1]:runnableFinally;
		
		try{
			if(runnable!=null)
				runnable.run();
		} catch (Exception e) {
			Log.d("dx",e.toString());
		}
		
		return null;
	}
	@Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if(runnableFinally!=null)
			runnableFinally.run();
        delegate.onTaskCompleted(null);
    }
	
}
