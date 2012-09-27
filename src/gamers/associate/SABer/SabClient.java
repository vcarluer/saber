package gamers.associate.SABer;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class SabClient extends WebChromeClient {
    private SABerActivity webactivity;

    private RelativeLayout mContentView;
    private FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    
    public SabClient(SABerActivity wv) {
        super();
        this.webactivity = wv;
    }
    
    
    public void onShowCustomView(View view, CustomViewCallback callback) {
        // super.onShowCustomView(view, callback);
        if (view instanceof FrameLayout) {
        	webactivity.hideWebControls();
        	mCustomViewContainer = (FrameLayout) view;
            
            mCustomViewCallback = callback;
            mContentView = (RelativeLayout) webactivity.findViewById(R.id.main);
        	// Add the custom view to the content view
	        mContentView.addView(mCustomViewContainer, new FrameLayout.LayoutParams( 
	                                ViewGroup.LayoutParams.FILL_PARENT, 
	                                ViewGroup.LayoutParams.FILL_PARENT, 
	                                Gravity.CENTER));
        }
    }
    
    
    
    @Override
	public View getVideoLoadingProgressView() {
		return new ProgressBar(webactivity, null, android.R.attr.progressBarStyleLarge);
	}

	public void stopCustomView() {
    	this.onHideCustomView();
    }

    public void onHideCustomView() {
    	// Remove the custom view from its container.
    	if (mCustomViewContainer != null) {
        	mContentView.removeView(mCustomViewContainer);
            mCustomViewContainer = null;
        }
        
        if (mCustomViewCallback != null) {
        	mCustomViewCallback.onCustomViewHidden();
        	mCustomViewCallback = null;
        }
        
        // Show the content view.
        webactivity.showWebControls();
    }


	public void onProgressChanged(WebView view, int progress) {
	     // Activities and WebViews measure progress with different scales.
	     // The progress meter will automatically disappear when we reach 100%
    	webactivity.setProgress(progress * 100);
    	ProgressBar progressBar = (ProgressBar) webactivity.findViewById(R.id.progressBar);
	     progressBar.setProgress(progress);
	     if (progressBar.getProgress() == progressBar.getMax()) {
	    	 progressBar.setVisibility(View.GONE);
	    	 progressBar.setProgress(0);
	     } else {
	    	 progressBar.setVisibility(View.VISIBLE);
	     }
	   }
    
    public boolean isCustomViewPlaying() {
    	return this.mCustomViewContainer != null;
    }
}
