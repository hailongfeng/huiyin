package com.residemenu.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.residemenu.main.lib.DragLayout;
import com.residemenu.main.lib.DragLayout.DragListener;

public class MainActivity extends Activity {

	/** 左边侧滑菜单 */
	private DragLayout mDragLayout;
	private ListView menuListView;// 菜单列表
	private ImageButton menuSettingBtn;// 菜单呼出按钮
	private LinearLayout menu_header;
	private TextView menu_setting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qqredide_activity_main);
		
		menu_setting=(TextView) this.findViewById(R.id.menu_setting);
		menu_header = (LinearLayout) this.findViewById(R.id.menu_header);
		/**
		 * 如果需要在别的Activity界面中也实现侧滑菜单效果，需要在布局中引入DragLayout（同本Activity方式），
		 * 然后在onCreate中声明使用; Activity界面部分，需要包裹在MyRelativeLayout中.
		 */
		mDragLayout = (DragLayout) findViewById(R.id.dl);
		mDragLayout.setDragListener(new DragListener() {// 动作监听
					@Override
					public void onOpen() {
					}

					@Override
					public void onClose() {
					}

					@Override
					public void onDrag(float percent) {

					}
				});

		// 生成测试菜单选项数据
		List<Map<String, Object>> data = getMenuData();

		menuListView = (ListView) findViewById(R.id.menu_listview);
		menuListView.setAdapter(new SimpleAdapter(this, data,
				R.layout.qqredide_menulist_item_text, new String[] { "item", "image" },
				new int[] { R.id.menu_text, R.id.menu_imageView1 }));

		// 添加监听，可点击呼出菜单
		menuSettingBtn = (ImageButton) findViewById(R.id.menu_imgbtn);
		menuSettingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDragLayout.open();
			}
		});
		initResideListener();// 自定义添加的东东

	}

	private void initResideListener() {
		// TODO Auto-generated method stub
		// 点击个人中心
		menu_header.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "进入个人中心界面", Toast.LENGTH_LONG).show();
			}
		});
		// 点击子菜单选项
		menuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					Toast.makeText(MainActivity.this, "0", Toast.LENGTH_LONG).show();
					break;
				case 1:
					Toast.makeText(MainActivity.this, "1", Toast.LENGTH_LONG).show();
					break;
				case 2:
					Toast.makeText(MainActivity.this, "2", Toast.LENGTH_LONG).show();
					break;
				case 3:
					Toast.makeText(MainActivity.this, "3", Toast.LENGTH_LONG).show();
					break;
				case 4:
					Toast.makeText(MainActivity.this, "4", Toast.LENGTH_LONG).show();
					break;
				case 5:
					Toast.makeText(MainActivity.this, "5", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
			}
		});
		//进入设置界面
		menu_setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "进入设置界面", Toast.LENGTH_LONG).show();
			}
		});

	}

	private List<Map<String, Object>> getMenuData() {
		// TODO Auto-generated method stub
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();		
		Map<String, Object> item;

		item = new HashMap<String, Object>();
		item.put("item", "菜单0");
		item.put("image", R.drawable.ic_launcher);
		data.add(item);

		item = new HashMap<String, Object>();
		item.put("item", "菜单1");
		item.put("image", R.drawable.ic_launcher);
		data.add(item);

		item = new HashMap<String, Object>();
		item.put("item", "菜单2");
		item.put("image", R.drawable.ic_launcher);
		data.add(item);

		item = new HashMap<String, Object>();
		item.put("item", "菜单3");
		item.put("image", R.drawable.ic_launcher);
		data.add(item);

		item = new HashMap<String, Object>();
		item.put("item", "菜单4");
		item.put("image", R.drawable.ic_launcher);
		data.add(item);

		item = new HashMap<String, Object>();
		item.put("item", "菜单5");
		item.put("image", R.drawable.ic_launcher);
		data.add(item);

		return data;
	}
}