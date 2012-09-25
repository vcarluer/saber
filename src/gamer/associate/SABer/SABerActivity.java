package gamer.associate.SABer;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SABerActivity extends Activity {
    private static final String HOME_SEARCH = "http://www.google.com/m?source=mobileproducts&dc=gorganic";
	private WebView webview;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);
        
        this.webview = (WebView) this.findViewById(R.id.webViewMain);
        
        Button btBack = (Button) this.findViewById(R.id.btBack);
        
        btBack.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				goBack();
			}
		});
        
        Button btSearch = (Button) this.findViewById(R.id.btSearch);
        btSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				webview.loadUrl(HOME_SEARCH);
			}
		});
        
        webview.clearHistory();
        webview.clearCache(true); // really...
        
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setAppCacheEnabled(true);
        
        settings.setSavePassword(false);
        settings.setAllowFileAccess(false);
        settings.setDatabaseEnabled(false);
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(false);
        settings.setGeolocationEnabled(false);
        settings.setNavDump(false);
        settings.setPluginsEnabled(false);
        
        CookieManager cookieManager = CookieManager.getInstance(); 
        cookieManager.removeAllCookie();
        
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