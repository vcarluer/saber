package gamers.associate.SABer;

import gamers.associate.SABer.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SABerActivity extends Activity {
    private static final String NO_TABS = "No tab in SABer to preserve your brain.";
	private static final String NO_DOWNLOAD = "No download feature available in SABer.";
	private static final String UA_SABER = " SABer/1.0";
	private static final String D_OH = "D'oh! ";
	private static final String HOME_SEARCH = "http://www.google.com/m?source=mobileproducts&dc=gorganic";
	private WebView webview;
	private Button btBack;
	private Button btSearch;
	private ProgressBar progressBar;
	
	private RelativeLayout mContentView;
	
	private SabClient sabClient;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        // main container
        this.mContentView = (RelativeLayout) this.findViewById(R.id.main);
        
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
        
        this.webview.setOnLongClickListener(new OnLongClickListener() {
			
			public boolean onLongClick(View v) {
				final WebView webview = (WebView) v;
	            final HitTestResult result = webview.getHitTestResult();
	            if(result.getType() == HitTestResult.SRC_ANCHOR_TYPE) {
	            	Toast.makeText(activity, D_OH + NO_TABS, Toast.LENGTH_SHORT).show();
	            	return true;
	            }
				
				return false;
			}
		});
                
        // Back
        this.btBack = (Button) this.findViewById(R.id.btBack);        
        this.btBack.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				goBack();
			}
		});
        
        // Search
        this.btSearch = (Button) this.findViewById(R.id.btSearch);
        this.btSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				webview.loadUrl(HOME_SEARCH);
			}
		});
        
        // Progress bar        
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        
        // Web client
        sabClient = new SabClient(this);        
        webview.setWebChromeClient(sabClient);
        
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
    	 
    	 // Url
    	 String url = HOME_SEARCH;
    	 Intent intent = getIntent();
    	 if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
    		 Uri uri = intent.getData();
    		 url = uri.toString();
    	 }
    	 
    	 // Go!
    	 this.showWebControls();
    	 webview.loadUrl(url);    	 
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
    	if (this.sabClient != null && this.sabClient.isCustomViewPlaying()) {
//    		this.sabClient.stopCustomView();
//    		return;
    	}
    	
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
    
    public void showWebControls() {
    	this.setControlsVisibility(View.VISIBLE);
    }
    
    public void hideWebControls() {
    	this.setControlsVisibility(View.GONE);
    }
    
    private void setControlsVisibility(int visible) {
    	if (this.webview != null) {
    		this.webview.setVisibility(visible);
    	}
    	
    	if (this.btBack != null) {
    		this.btBack.setVisibility(visible);
    	}
    	
    	if (this.btSearch != null) {
    		this.btSearch.setVisibility(visible);
    	}
    	
    	if (this.progressBar != null) {
    		this.progressBar.setVisibility(visible);
    	}
    }
	@Override
	protected void onPause() {
		if (this.sabClient != null && this.sabClient.isCustomViewPlaying()) {
//			this.sabClient.stopCustomView();
		}
		
		super.onPause();
	}
    
    
}