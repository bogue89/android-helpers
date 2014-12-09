package com.custom;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class UILabel  extends TextView {
    
	public Context context;
	public AttributeSet attrs;
	public int defStyle;
	
    public UILabel(Context context) {
        super(context);
        this.context = context;
    }
    public UILabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
    }
    public UILabel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.attrs = attrs;
        this.defStyle = defStyle;
    }
    
    /*
     * Custom Methods
     * */
    public void setFont(String font) {
    	super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/"+font+".ttf"));
    }
    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),    "fonts/HelveticaNeue-UltraLight.ttf"));
        } else if(style == Typeface.ITALIC) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/HelveticaNeueLight.ttf"));
        }
    }
}