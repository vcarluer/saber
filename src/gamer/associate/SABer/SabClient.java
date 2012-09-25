package gamer.associate.SABer;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class SabClient extends WebChromeClient {
    private SABerActivity webactivity; // Use Your WebView instance instead

    private RelativeLayout mContentView;
    private FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    static final FrameLayout.LayoutParams COVER_SCREEN_GRAVITY_CENTER = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT, Gravity.CENTER);

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
		        mContentView.addView(mCustomViewContainer, new FrameLayout.LayoutParams( 
		                                ViewGroup.LayoutParams.FILL_PARENT, 
		                                ViewGroup.LayoutParams.FILL_PARENT, 
		                                Gravity.CENTER)); 
        		mContentView.setVisibility(View.VISIBLE); 
        		WebView webview = (WebView) mContentView.findViewById(R.id.webViewMain);
        		webview.setVisibility(View.GONE);
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
        WebView webview = (WebView) mContentView.findViewById(R.id.webViewMain);
        webview.setVisibility(View.VISIBLE);
    }
    
    public void onProgressChanged(WebView view, int progress) {
	     // Activities and WebViews measure progress with different scales.
	     // The progress meter will automatically disappear when we reach 100%
    	webactivity.setProgress(progress * 100);
    	ProgressBar progressBar = (ProgressBar) webactivity.findViewById(R.id.progressBar);
	     progressBar.setProgress(progress);
	     if (progressBar.getProgress() == progressBar.getMax()) {
	    	 progressBar.setVisibility(View.INVISIBLE);
	    	 progressBar.setProgress(0);
	     } else {
	    	 progressBar.setVisibility(View.VISIBLE);
	     }
	   }
}
