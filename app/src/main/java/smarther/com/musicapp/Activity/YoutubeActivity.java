package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import smarther.com.musicapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

public class YoutubeActivity extends AppCompatActivity {
    LinearLayout navigate_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        navigate_back=findViewById(R.id.navigate_back);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        navigate_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        assert getSupportActionBar() != null;
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setTitle("Youtube");
        WebView webView=(WebView)findViewById(R.id.webview_terms);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);


        final Activity activity = this;

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }


        });
        webView.loadUrl(url);

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
