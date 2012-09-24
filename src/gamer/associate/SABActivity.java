package gamer.associate;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SABActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);
        
        WebView webview = (WebView) this.findViewById(R.id.webViewMain);
        
        webview.getSettings().setJavaScriptEnabled(true);
        
        final Activity activity = this;
        webview.setWebChromeClient(new WebChromeClient() {
        	   public void onProgressChanged(WebView view, int progress) {
        	     // Activities and WebViews measure progress with different scales.
        	     // The progress meter will automatically disappear when we reach 100%
        	     activity.setProgress(progress * 1000);
        	   }
        	 });
        	 webview.setWebViewClient(new WebViewClient() {
        	   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        	     Toast.makeText(activity, "d'oh! " + description, Toast.LENGTH_SHORT).show();
        	   }
        	 });

        	 webview.loadUrl("http://www.google.com");
    }
}