package com.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class UrlRequestJSON extends AsyncTask<String, String, String>{

	private OnTaskCompleted delegate = null;
	private Context context = null;
	private String taskId = "";
	
	public UrlRequestJSON(String taskId, OnTaskCompleted delegate, Context context){
		this.delegate = (OnTaskCompleted) delegate;
		this.context = (Context) context;
		this.taskId = taskId;
	}
	public UrlRequestJSON(OnTaskCompleted delegate, Context context){
		this.delegate = (OnTaskCompleted) delegate;
		this.context = (Context) context;
	}
	public UrlRequestJSON(Object delegate){
		this.delegate = (OnTaskCompleted) delegate;
		this.context = (Context) delegate;
	}
	
    @Override
    protected String doInBackground(String... uri) {
    	if(!isNetworkAvailable()){
    		return "";
    	}
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost();
        HttpResponse response;
       
        String responseString = null;
        try {
        	httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        	httppost.setURI(new URI(uri[0]));
        	
        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        	for(int i = 1; i < uri.length; i=i+2){
        		if(uri.length > (i+1)){
        			nameValuePairs.add(new BasicNameValuePair(uri[i], uri[i+1]));
        		}
        	}
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            
            response = httpclient.execute(httppost);
            StatusLine statusLine = response.getStatusLine();
            
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        
        JSONObject error = null;
        try {
			error = new JSONObject("{\"status\":\"error\",\"response\":{},\"error\":\"parsing\",\"warning\":null}");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
        try {
        	if(result==null){
        		error = new JSONObject("{\"status\":\"error\",\"response\":{},\"error\":\"no connection\",\"warning\":null}");
            	if(delegate!=null){ 
        			delegate.onTaskCompleted(error);
    				delegate.onTaskCompleted(taskId, error);
            	}
                return;
            }
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
        try {
        	JSONObject respuesta = new JSONObject(result);
        	
			if(delegate!=null){
				delegate.onTaskCompleted(respuesta);
				delegate.onTaskCompleted(taskId, respuesta);
				return;
			}
		} catch (JSONException e) {}
        try {
        	JSONArray respuesta = new JSONArray(result);
        	
			if(delegate!=null){
				delegate.onTaskCompleted(respuesta);
				delegate.onTaskCompleted(taskId, respuesta);
				return;
			}
		} catch (JSONException e) {}
        if(delegate!=null) {
			delegate.onTaskCompleted(error);
			delegate.onTaskCompleted(taskId, error);
        }
        return;
    }
    
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
