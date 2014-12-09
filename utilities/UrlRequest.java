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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class UrlRequest extends AsyncTask<String, String, String>{
	/*
	 * Properties
	 * */
	private OnTaskCompleted delegate = null;
	private Context context = null;
	private String taskId = "";
	String returnType = null;
	String responseString = null;
    private byte[] responseData = null;
    /*
	 * Methods
	 * */
    public UrlRequest(String taskId, OnTaskCompleted delegate, Context context){
		this.delegate = (OnTaskCompleted) delegate;
		this.context = (Context) context;
		this.taskId = taskId;
	}
    public UrlRequest(OnTaskCompleted delegate, Context context){
		this.delegate = (OnTaskCompleted) delegate;
		this.context = (Context) context;
	}
	public UrlRequest(Object delegate){
		this.delegate = (OnTaskCompleted) delegate;
		this.context = (Context) delegate;
	}
    @Override
    protected String doInBackground(String... uri) {
    	if(uri.length > 1){
        	returnType = uri[1];
        }
    	if(!isNetworkAvailable()){
    		return "";
    	}
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost();
        HttpResponse response;
        
        try {
        	httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        	httppost.setURI(new URI(uri[0]));
        	
        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        	for(int i = 0; i < uri.length; i=i+2){
        		if(uri.length<(i+1)){
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
                responseData = out.toByteArray();
            } else if(statusLine.getStatusCode() == HttpStatus.SC_BAD_REQUEST){
            	Log.d("dx","BAD REQUEST");
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            } else {
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
        if(delegate!=null){
        	if(returnType!=null && returnType.equals("ByteArray")){
        		delegate.onTaskCompleted(taskId, responseData);
        		delegate.onTaskCompleted(responseData);
        	} else {
        		delegate.onTaskCompleted(taskId, result);
        		delegate.onTaskCompleted(result);
        	}
        } else {
        	delegate.onTaskCompleted(taskId, null);
        	delegate.onTaskCompleted(null);
        }
    }
    @Override
    protected void onCancelled() {
    	responseData = null;
    	responseString=null;
    }
    private boolean isNetworkAvailable() {
    	if(this.context == null){
    		return false;
    	}
        ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}