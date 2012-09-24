package gamer.associate;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SABActivity extends Activity {
    private static final String HOME_SEARCH = "http://www.google.com";
	private WebView webview;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);
        
        this.webview = (WebView) this.findViewById(R.id.webViewMain);
        
        ImageView btBack = (ImageView) this.findViewById(R.id.btBack);
        
        btBack.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				goBack();
			}
		});
        
        ImageView btSearch = (ImageView) this.findViewById(R.id.btSearch);
        btSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				webview.loadUrl(HOME_SEARCH);
			}
		});
        
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setBuiltInZoomControls(true);
        
        final Activity activity = this;
        final ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        webview.setWebChromeClient(new WebChromeClient() {
        	   public void onProgressChanged(WebView view, int progress) {
        	     // Activities and WebViews measure progress with different scales.
        	     // The progress meter will automatically disappear when we reach 100%
        	     activity.setProgress(progress * 100);
        	     progressBar.setProgress(progress);
        	     if (progressBar.getProgress() == progressBar.getMax()) {
        	    	 progressBar.setVisibility(View.INVISIBLE);
        	    	 progressBar.setProgress(0);
        	     } else {
        	    	 progressBar.setVisibility(View.VISIBLE);
        	     }
        	   }
        	 });
        
    	 webview.setWebViewClient(new WebViewClient() {
    	   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    	     Toast.makeText(activity, "d'oh! " + description, Toast.LENGTH_SHORT).show();
    	   }
    	 });

    	 webview.loadUrl(HOME_SEARCH);
    }
	@Override
	public void onBackPressed() {
		goBack();
	}
    
    private void goBack() {
    	if (webview.canGoBack()) {
			webview.goBack();
		}
    }
}