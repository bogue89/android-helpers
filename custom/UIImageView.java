package com.custom;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import com.utilities.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class UIImageView extends ImageView implements OnTaskCompleted {
	
	private Bitmap bitmap;
	private String image_url;
	private String cache_dir;
	private Context context;
	private Activity activity;
	private UIImageView self;
	private UrlRequest urlRequest;
	private HashMap <String, Bitmap> bitmaps = new HashMap <String, Bitmap>();
	
	public boolean working = false;
	public BitmapFactory.Options options;
	public boolean onBackground = true;
	public int round = -1;
	
	public UIImageView(Context context) {
		super(context);
        this.init(context);
	}
    public UIImageView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.init(context);
    }
    public UIImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context);
    }
    private void init(Context context){
    	this.activity = (Activity) context;
        this.context = context;
        this.bitmap = null;
        bitmap = null;
		this.cache_dir = this.context.getCacheDir().getAbsolutePath() + File.separator + "images" + File.separator;
		this.options = new BitmapFactory.Options();
		this.image_url = "";
		self = this;
    }
	/*
	 * Custom Methods
	 * */
    public String makeFilenameFrendly(String name){
    	return name.replaceAll("[^\\p{Alpha}\\p{Digit}]+","");
    }
	public void setImageFromUrl(final String url){
		System.gc();
		if(!url.equals("")){
			self.working = true;
			self.image_url = url;
			Bitmap cache_bitmap = bitmaps.get(url);
			if(cache_bitmap != null){
				self.setBitmap(url, cache_bitmap);
				return;
			}
			if(urlRequest!=null){
				urlRequest.cancel(true);
			}
			if(onBackground){
		    	Runnable runnable = new Runnable() {
		    		@Override
		    		public void run() {
		    			final Bitmap bitmap = self.loadImage(url);
		    			bitmaps.put(url, bitmap);
		    			activity.runOnUiThread(new Runnable() {
		    			    public void run() {
		    			    	if(bitmap!=null){
			        				self.setBitmap(url, bitmap);
								} else {
									downloadImage(url);
								}
		    			    }
		    			});
		    		}
		    	};
		    	new Thread(runnable).start();
			} else {
				Bitmap bitmap = loadImage(url);
				if(bitmap != null){
					bitmaps.put(url, bitmap);
					self.setBitmap(url, bitmap);
				} else {
					downloadImage(url);
				}
			}
		}
	}
	public void downloadImage(String image_url){
		if(urlRequest!=null){
			urlRequest.cancel(true);
		}
		urlRequest = new UrlRequest(image_url, self, self.context);
		urlRequest.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, image_url,"ByteArray");
	}
	@Override
	public void onTaskCompleted(Object object) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onTaskCompleted(final String url, final Object object) {
		if(urlRequest!=null){
			urlRequest=null;
		}
		if(onBackground){
	    	Runnable runnable = new Runnable() {
	    		@Override
	    		public void run() {
	    			final Bitmap bitmap = decodeBitmap(object);
	    			if(bitmap != null){
	    				bitmaps.put(url, bitmap);
	    				self.saveImage(url, bitmap);
		    			activity.runOnUiThread(new Runnable() {
		    			    public void run() {
		    			    	self.setBitmap(url, bitmap);
		    			    }
		    			});
	    			}
	    			self.working = false;
	    		}
	    	};
	    	new Thread(runnable).start();
		} else {
			Bitmap bitmap = decodeBitmap(object);
			if(bitmap != null){
				bitmaps.put(url, bitmap);
				self.saveImage(url, bitmap);
				self.setBitmap(url, bitmap);
			}
			self.working = false;
		}
	}
	private Bitmap decodeBitmap(Object object){
		byte[] data = object!=null ? (byte[])object:null;
		if(data!=null && data.length > 0){
			// inPurgeable is used to free up memory while required
            options.inPurgeable = true; 
            // Decode image			
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
			if(bitmap != null){
				return bitmap;
			}
		}
		return null;
	}
	private Bitmap loadImage(String url){
		File file = null;
		try{
			String image_name = makeFilenameFrendly(url);
			//create the file on the data directory of the app
			file = new File(cache_dir + image_name + ".png");

			FileInputStream streamIn = new FileInputStream(file);
			//This gets the image from stream
			Bitmap bitmap = BitmapFactory.decodeStream(streamIn);
			streamIn.close();
			if(bitmap != null){
				self.working = false;
				return bitmap;
			}
		} catch (Exception e) { e.printStackTrace(); if(file!=null)file.delete();}
		self.working = false;
		return null;
	}
	private boolean saveImage(String url, Bitmap bitmap){
		if(bitmap!=null){
			try {
				File dir = new File(cache_dir);
				if(!dir.exists()){
					dir.mkdirs();
				}
				String image_name = makeFilenameFrendly(url);
				FileOutputStream out = new FileOutputStream(cache_dir + image_name + ".png");
				bitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
		        out.flush();
		        out.close();
		        return true;
			} catch (Exception e) { Log.d("dx",e.toString()); e.printStackTrace(); }
		}
		return false;
	}
	private void setBitmap(String url, Bitmap bitmap){
		if(self.image_url.equals(url)){
			self.bitmap = bitmap;
			self.setImageBitmap(bitmap);
		}
	}
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
