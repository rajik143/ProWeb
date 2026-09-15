package com.github.warren_bank.webmonkey.settings;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewSettingsMgr {

  private static Context context = null;
  private static WebView webView = null;

  public static void initStaticResources(Context context, WebView webView) {
    WebViewSettingsMgr.context = context;
    WebViewSettingsMgr.webView = webView;
  }

  public static void initWebView() {
    if ((webView == null) || (context == null)) return;

    updateRemoteDebugger();

    WebSettings webSettings = webView.getSettings();
    webSettings.setLoadWithOverviewMode(true);
    webSettings.setSupportZoom(true);
    webSettings.setBuiltInZoomControls(true);
    webSettings.setDisplayZoomControls(true);
    webSettings.setUseWideViewPort(false);
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    if (Build.VERSION.SDK_INT >= 17) {
      webSettings.setMediaPlaybackRequiresUserGesture(false);
    }
    if (Build.VERSION.SDK_INT >= 21) {
      webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }
    updateUserAgent(webSettings);

    // Deep Black Mode for WebView
    webView.setBackgroundColor(0xFF000000);
    try {
      if (androidx.webkit.WebViewFeature.isFeatureSupported(androidx.webkit.WebViewFeature.FORCE_DARK)) {
        androidx.webkit.WebSettingsCompat.setForceDark(webSettings, androidx.webkit.WebSettingsCompat.FORCE_DARK_ON);
      } else if (Build.VERSION.SDK_INT >= 29) {
        webSettings.setForceDark(WebSettings.FORCE_DARK_ON);
      }
      if (androidx.webkit.WebViewFeature.isFeatureSupported(androidx.webkit.WebViewFeature.FORCE_DARK_STRATEGY)) {
        androidx.webkit.WebSettingsCompat.setForceDarkStrategy(webSettings, androidx.webkit.WebSettingsCompat.DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING);
      }
    } catch (Throwable t) {
    }

    webView.setInitialScale(0);
    webView.setHorizontalScrollBarEnabled(false);
    webView.setVerticalScrollBarEnabled(false);
    webView.clearCache(true);
    webView.clearHistory();
  }

  public static String getDefaultUserAgent() {
    if ((webView == null) || (context == null)) return null;

    if (Build.VERSION.SDK_INT >= 17)
      return WebSettings.getDefaultUserAgent(context);

    // https://stackoverflow.com/a/10248817
    try {
      return System.getProperty("http.agent");
    }
    catch(Exception e) {}

    return null;
  }

  public static void updateUserAgent() {
    if ((webView == null) || (context == null)) return;

    WebSettings webSettings = webView.getSettings();
    updateUserAgent(webSettings);
  }

  private static void updateUserAgent(WebSettings webSettings) {
    if ((webView == null) || (context == null) || (webSettings == null)) return;

    String agent = SettingsUtils.getUserAgent(/* Context */ context, false);
    webSettings.setUserAgentString(agent);
  }

  public static void updateRemoteDebugger() {
    if ((webView == null) || (context == null)) return;

    boolean enabled = (Build.VERSION.SDK_INT >= 19)
      ? SettingsUtils.getEnableRemoteDebuggerPreference(/* Context */ context)
      : false;

    WebView.setWebContentsDebuggingEnabled(enabled);
  }

  public static void removeAllCookies() {
    CookieManager cookieMgr = CookieManager.getInstance();

    cookieMgr.removeSessionCookie();
    cookieMgr.removeAllCookie();
  }

  public static void toggleDarkMode() {
    if (webView == null || context == null) return;
    WebSettings webSettings = webView.getSettings();
    try {
      if (androidx.webkit.WebViewFeature.isFeatureSupported(androidx.webkit.WebViewFeature.FORCE_DARK)) {
        int current = androidx.webkit.WebSettingsCompat.getForceDark(webSettings);
        int target = (current == androidx.webkit.WebSettingsCompat.FORCE_DARK_ON)
            ? androidx.webkit.WebSettingsCompat.FORCE_DARK_OFF
            : androidx.webkit.WebSettingsCompat.FORCE_DARK_ON;
        androidx.webkit.WebSettingsCompat.setForceDark(webSettings, target);
        android.widget.Toast.makeText(context, (target == androidx.webkit.WebSettingsCompat.FORCE_DARK_ON) ? "Deep Black Mode: ON" : "Deep Black Mode: OFF", android.widget.Toast.LENGTH_SHORT).show();
        webView.reload();
      } else if (Build.VERSION.SDK_INT >= 29) {
        int current = webSettings.getForceDark();
        int target = (current == WebSettings.FORCE_DARK_ON)
            ? WebSettings.FORCE_DARK_OFF
            : WebSettings.FORCE_DARK_ON;
        webSettings.setForceDark(target);
        android.widget.Toast.makeText(context, (target == WebSettings.FORCE_DARK_ON) ? "Deep Black Mode: ON" : "Deep Black Mode: OFF", android.widget.Toast.LENGTH_SHORT).show();
        webView.reload();
      }
    } catch (Throwable t) {}
  }

}
