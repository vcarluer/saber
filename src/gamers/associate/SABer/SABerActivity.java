package gamers.associate.SABer;

import gamers.associate.SABer.R;
import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SABerActivity extends Activity {
    private static final String NO_DOWNLOAD = " No download feature available in SABer.";
	private static final String UA_SABER = " SABer/1.0";
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
        final Activity activity = this;
        this.clearAll();        
        
        // Web sttings        
        WebSettings settings = webview.getSettings();
        // User Agent
        String ua = settings.getUserAgentString() + UA_SABER;        
        settings.setUserAgentString(ua);
        // Enabled
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true); // No database path so no persistent
        // Disabled
        settings.setSavePassword(false);
        settings.setAllowFileAccess(false);
        settings.setDatabaseEnabled(false);
        settings.setDisplayZoomControls(false);        
        settings.setGeolocationEnabled(false);
        settings.setNavDump(false);
        settings.setPluginState(PluginState.OFF);
        // Downloads
        this.webview.setDownloadListener(new DownloadListener() {
			
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {
				Toast.makeText(activity, D_OH + NO_DOWNLOAD, Toast.LENGTH_SHORT).show();
			}
		});
                
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
        final ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        
        // Web client
        webview.setWebChromeClient(new SabClient(this));
        
    	 webview.setWebViewClient(new WebViewClient() {
    	   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    	     Toast.makeText(activity, D_OH + description, Toast.LENGTH_SHORT).show();
    	   }

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler,
					SslError error) {
				handler.proceed();
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
    		webview.clearFormData();
            webview.clearCache(true); // really...
    	}
    	
        CookieManager cookieManager = CookieManager.getInstance();
        if (cookieManager != null) {
        	cookieManager.removeAllCookie();
        }
        
        WebStorage storage = WebStorage.getInstance();
        if (storage != null) {
        	storage.deleteAllData();
        }
    }
}