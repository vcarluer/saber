package gamers.associate.SABer;

import gamers.associate.SABer.R;
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
            mCustomViewContainer = (FrameLayout) view;
            mCustomViewCallback = callback;
            mContentView = (RelativeLayout) webactivity.findViewById(R.id.main);
            if (view instanceof FrameLayout) { 
            	// Add the custom view to the content view
		        mContentView.addView(mCustomViewContainer, new FrameLayout.LayoutParams( 
		                                ViewGroup.LayoutParams.FILL_PARENT, 
		                                ViewGroup.LayoutParams.FILL_PARENT, 
		                                Gravity.CENTER)); 
        		
		        // Switch visibility
		        mContentView.setVisibility(View.VISIBLE);         		
        		webactivity.hideWebControls();
	        }
        }
    }

    public void onHideCustomView() {
        if (mCustomViewContainer == null)
            return;
        // Hide the custom view.
        mCustomViewContainer.setVisibility(View.GONE);
        // Remove the custom view from its container.
        mContentView.removeView(mCustomViewContainer);
        mCustomViewContainer = null;
        mCustomViewCallback.onCustomViewHidden();
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
}
