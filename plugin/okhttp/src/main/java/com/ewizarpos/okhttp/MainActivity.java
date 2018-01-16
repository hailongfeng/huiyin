package com.ewizarpos.okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.txt_content)
    TextView txtContent;
    @BindView(R.id.wv1)
    WebView wv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OKHttpUtil.getInstance().get("http://blog.csdn.net/fhl13017599952/article/details/79062722", null, new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        final String result = e.getMessage();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtContent.setText(result);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String result = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtContent.setText(result);
                            }
                        });
                    }
                });
            }
        });
    }

}
