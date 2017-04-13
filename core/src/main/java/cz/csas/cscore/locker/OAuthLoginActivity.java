package cz.csas.cscore.locker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import cs.cz.cscore.R;
import cz.csas.cscore.error.CsCoreError;
import cz.csas.cscore.utils.csjson.CsJson;

/**
 * The type O auth login activity.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 06 /12/15.
 */
public class OAuthLoginActivity extends Activity {

    private final List<String> TESTING_JS_ALLOWED_BASE_URLS = new ArrayList<String>() {{
        add("https://bezpecnost.csast.csas.cz/mep/fs/fl/oauth2");
        add("https://www.csast.csas.cz/widp/oauth2");
    }};

    private final int EXPIRATION_TIME = 5 * 60 * 1000;
    private WebView mLoginWebView;
    private Toolbar mToolbar;
    private String mUrlPath;
    private String mRedirectUrl;
    private String mJavascript;
    private OAuthLoginActivityOptions mOAuthLoginActivityOptions;
    private boolean mAllowUntrustedCertificates;
    private Handler mHandler;
    @SuppressLint("StaticFieldLeak")
    private static Activity mActivity;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauth_login_activity);
        mActivity = this;

        Bundle extras = getIntent().getExtras();
        mUrlPath = extras.getString(Constants.OAUTH_URL_EXTRA);
        mRedirectUrl = extras.getString(Constants.REDIRECT_URL_EXTRA);
        mJavascript = extras.getString(Constants.TESTING_JS_EXTRA);
        mOAuthLoginActivityOptions = new CsJson().fromJson(extras.getString(Constants.OAUTH_LOGIN_ACTIVITY_OPTIONS_EXTRA), OAuthLoginActivityOptions.class);
        mAllowUntrustedCertificates = extras.getBoolean(Constants.ALLOW_UNTRUSTED_CERTIFICATES_EXTRA);
        mHandler = new Handler();
        finishAfterExpiration();

        mLoginWebView = (WebView) findViewById(R.id.vw_oauth_login_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main_activity);
        setNavBar();

        mLoginWebView.getSettings().setDomStorageEnabled(true);
        mLoginWebView.getSettings().setJavaScriptEnabled(true);
        mLoginWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(mRedirectUrl)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.CODE_EXTRA, url);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtras(bundle);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (mAllowUntrustedCertificates)
                    handler.proceed();
                else
                    super.onReceivedSslError(view, handler, error);
            }
        });
        mLoginWebView.getSettings().setSupportMultipleWindows(true);
        mLoginWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView.HitTestResult result = view.getHitTestResult();
                String data = result.getExtra();
                Context context = view.getContext();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                context.startActivity(browserIntent);
                return false;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    if (mJavascript != null && shouldExecuteTestingJavascript(mUrlPath)) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                            mLoginWebView.evaluateJavascript(mJavascript, null);
                        else
                            mLoginWebView.loadUrl("javascript:" + mJavascript);
                    }
                }
            }


        });
        mLoginWebView.loadUrl(mUrlPath);
    }

    @Override
    public void onBackPressed() {
        if (mLoginWebView.canGoBack())
            mLoginWebView.goBack();
        else {
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    /**
     * Finish o auth login activity.
     *
     * @return the boolean
     */
    public static boolean finishOAuthLoginActivity() {
        if (mActivity != null) {
            mActivity.finish();
            return true;
        }
        return false;
    }

    private void finishAfterExpiration() {
        mHandler.postDelayed(mRunnable, EXPIRATION_TIME);
    }


    private boolean shouldExecuteTestingJavascript(String url) {
        for (String allowedUrl : TESTING_JS_ALLOWED_BASE_URLS) {
            if (url.contains(allowedUrl))
                return true;
        }
        throw new CsCoreError(CsCoreError.Kind.BAD_ENVIRONMENT_FOR_TESTING_JS);
    }

    private void setNavBar() {
        CsNavBarColor csNavBarColor = mOAuthLoginActivityOptions.getNavBarColor();
        if (csNavBarColor == CsNavBarColor.WHITE) {
            setBackground(mToolbar, new ColorDrawable(ContextCompat.getColor(this, R.color.csasColorWhite)));
        } else {
            setBackground(mToolbar, new ColorDrawable(ContextCompat.getColor(this, R.color.csasColorNavBar)));
        }

        mToolbar.setLogo(ContextCompat.getDrawable(this, R.drawable.logo_csas));
        if (mOAuthLoginActivityOptions.getShowLogo())
            mToolbar.setVisibility(View.VISIBLE);
        else
            mToolbar.setVisibility(View.GONE);
    }

    private void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }
}


