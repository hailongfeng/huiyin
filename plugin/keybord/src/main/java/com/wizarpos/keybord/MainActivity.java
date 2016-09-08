package com.wizarpos.keybord;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.wizarpos.keybord.view.KeyboardView;

public class MainActivity extends AppCompatActivity {
    EditText test1,test2;
    KeyboardView kbv;
    int currentSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kbv=(KeyboardView) findViewById(R.id.kb);
        test1=(EditText) findViewById(R.id.edt_test);
        test2=(EditText) findViewById(R.id.edt_test2);
        test1.setOnFocusChangeListener(onFocusChangeListener);
        test2.setOnFocusChangeListener(onFocusChangeListener);
        kbv.setOnKeyBoardInput(new KeyboardView.OnKeyBoardInput() {
            @Override
            public void onKeyClick(String value) {
                if(currentSelect==1){
                    test1.setText(value);
                }else {
                    test2.setText(value);
                }
            }
        });
    }
    View.OnFocusChangeListener onFocusChangeListener=  new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (view.getId()==R.id.edt_test&&b){
                currentSelect=1;
                kbv.setDefaultValue(test1.getText().toString());
            }else {
                currentSelect=2;
                kbv.setDefaultValue(test2.getText().toString());
            }
        }
    };
}
