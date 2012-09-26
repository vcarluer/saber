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
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SABerActivity extends Activity {
    private static final String D_OH = "D'oh! ";
	private static final String HOME_SEARCH = "http://www.google.com/m?source=mobileproducts&dc=gorganic";
	private WebView webview;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        // Lets groove
        this.webview = (WebView) this.findViewById(R.id.webViewMain);
        this.clearAll();
        
        // Web sttings        
        WebSettings settings = webview.getSettings();
        // Enabled
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setAppCacheEnabled(true);
        // Disabled
        settings.setSavePassword(false);
        settings.setAllowFileAccess(false);
        settings.setDatabaseEnabled(false);
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(false);
        settings.setGeolocationEnabled(false);
        settings.setNavDump(false);
        settings.setPluginState(PluginState.OFF);
        
        // Back
        Button btBack = (Button) this.findViewById(R.id.btBack);        
        btBack.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				goBack();
			}
		});
        
        // Search
        Button btSearch = (Button) this.findViewById(R.id.btSearch);
        btSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				webview.loadUrl(HOME_SEARCH);
			}
		});
        
        // Progress bar
        final Activity activity = this;
        final ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        
        // Web client
        webview.setWebChromeClient(new SabClient(this));
        
    	 webview.setWebViewClient(new WebViewClient() {
    	   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    	     Toast.makeText(activity, D_OH + description, Toast.LENGTH_SHORT).show();
    	   }
    	 });

    	 // Go!
    	 webview.loadUrl(HOME_SEARCH);
    }
	@Override
	protected void onDestroy() {
		this.clearAll();
		super.onDestroy();
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
    
    private void clearAll() {
    	if (webview != null) {
    		webview.clearHistory();
            webview.clearCache(true); // really...
    	}
    	
        CookieManager cookieManager = CookieManager.getInstance();
        if (cookieManager != null) {
        	cookieManager.removeAllCookie();
        }        
    }
}