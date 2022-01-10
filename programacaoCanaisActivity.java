package com.guiadeprogramacao.guiatv.eliezer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;


public class programacaoCanaisActivity extends AppCompatActivity implements programacaoCanaisActivityInterface {

    private String url = "";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacao_canais);

        getSupportActionBar().hide();

        primeiraVerificacaoParaStatusBarTransparent();

        webView = (WebView) findViewById(R.id.webView);

        configurandoWebView();

        url = "https://meuguia.tv/";

        if (savedInstanceState == null) webView.loadUrl(url);

    }

    private void configurandoWebView() {
        webView.setWebViewClient(new MyBrowser());
        //webView.setWebViewClient(new WebViewClient());//evita pausar ao tocar a tela, porem tem anuncios
        webView.setWebChromeClient(new MyChrome());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void primeiraVerificacaoParaStatusBarTransparent() {
        //step 1
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    private void setWindowFlag(programacaoCanaisActivity mainActivity, int flagTranslucentStatus, boolean b) {
        Window win = mainActivity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (b) {
            winParams.flags |= flagTranslucentStatus;
        } else {
            winParams.flags &= ~flagTranslucentStatus;
        }
        win.setAttributes(winParams);
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;
        }
    }

    private class MyChrome extends WebChromeClient {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChrome() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    //eu nao quero efchar meu app ao pressionar a tecla de retorno do celular

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && this.webView.canGoBack())//if i press key back i keep going navigating in website
        {
            this.webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event); //otherwise i exciting from the aplication
    }

//se o usuario clicar em algum link eu no quero que ele va para um browser externo

    class CustomWebViewClient extends WebViewClient
    {
        private Activity activity; // i need to pass my currently instance

        //create constructor

        public CustomWebViewClient(Activity activity)
        {
            this.activity = activity;
        }

    }

    // API less than 24
    //nao abrir links em outros brownsers por isso o retorno vai ser false alem de ocultar a url
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }

    //API >= 24
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request)
    {
        return false;
    }

}
