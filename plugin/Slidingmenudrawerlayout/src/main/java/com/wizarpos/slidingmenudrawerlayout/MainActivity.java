package com.wizarpos.slidingmenudrawerlayout;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
   DrawerLayout drawer_layout;
    ListView lv_drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolBar);
        drawer_layout= (DrawerLayout) findViewById(R.id.drawer_layout);
        lv_drawer= (ListView) findViewById(R.id.lv_drawer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
       Button toggleButton= (Button) findViewById(R.id.tbMenu);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(Gravity.START);
            }
        });
        List<Map<String,String>> data=new ArrayList<>();
        for (int i = 0; i <15 ; i++) {
            HashMap map1=new HashMap<String, String>();
            map1.put("title","ttt"+i);
            data.add(map1);
        }

        lv_drawer.setAdapter(new SimpleAdapter(this,data,android.R.layout.simple_list_item_1, new String[] { "title"},
                new int[] {android.R.id.text1}));
        lv_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawer_layout.closeDrawer(Gravity.START);
            }
        });
    }

}
