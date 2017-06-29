package com.jarvis.mytaobao.cart;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvis.mytaobao.Data.Data;
import com.jarvis.mytaobao.home.BabyActivity;
import com.jarvis.mytaobaotest.R;
import com.javis.Adapter.Adapter_ListView_cart;
import com.javis.Adapter.Adapter_ListView_cart.onCheckedChanged;
import com.javis.mytools.IBtnCallListener;

/**
 * 购物车界面中的全部宝贝界面
 * @author http://yecaoly.taobao.com
 * 
 */
@SuppressLint("ValidFragment")
public class AllBaby_F extends Fragment implements IBtnCallListener, onCheckedChanged, OnClickListener {
	IBtnCallListener btnCallListener;
	private TextView tv_goShop, tv_cart_Allprice, tv_cart_buy_Ordel;
	private LinearLayout ll_cart;
	private ListView listView_cart;
	private CheckBox cb_cart_all;
	private Adapter_ListView_cart adapter;
	private String str_del = "结算(0)";
	private boolean[] is_choice;

	public AllBaby_F(String del) {
		str_del = del;
	}

	public AllBaby_F() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		is_choice=new boolean[Data.arrayList_cart.size()];
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.cart_all_f, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		tv_goShop = (TextView) view.findViewById(R.id.tv_goShop);
		tv_cart_Allprice = (TextView) view.findViewById(R.id.tv_cart_Allprice);
		tv_cart_buy_Ordel = (TextView) view.findViewById(R.id.tv_cart_buy_or_del);
		tv_cart_buy_Ordel.setText(str_del);
		cb_cart_all = (CheckBox) view.findViewById(R.id.cb_cart_all);

		cb_cart_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				/*
				 * 判断一：（全选按钮选中）全选按钮是否选择，如果选择，那么列表每一行都选中
				 * 判断二：（全选按钮取消）当取消全选按钮时，会有两种情况
				 * ，第一：主动点击全选按钮，此时直接取消列表所有的选中状态，第二：取消列表某一行，导致全选取消，此时列表其他行仍然是选中
				 * 
				 * 判断二的分析：（主动取消）判断列表每一行的选中状态，如果全部都是选中状态，那么（列表选中数=列表总数），此时属于主动取消，
				 * 则取消所有行的选中状态，反之（被动取消）则不响应
				 */

				// 记录列表每一行的选中状态数量
				int isChoice_all = 0;
				if (arg1) {
					// 设置全选
					for (int i = 0; i < Data.arrayList_cart.size(); i++) {
						// 如果选中了全选，那么就将列表的每一行都选中
						((CheckBox) (listView_cart.getChildAt(i)).findViewById(R.id.cb_choice)).setChecked(true);
					}
				} else {
					// 设置全部取消
					for (int i = 0; i < Data.arrayList_cart.size(); i++) {
						// 判断列表每一行是否处于选中状态，如果处于选中状态，则计数+1
						if (((CheckBox) (listView_cart.getChildAt(i)).findViewById(R.id.cb_choice)).isChecked()) {
							// 计算出列表选中状态的数量
							isChoice_all += 1;
						}
					}
					// 判断列表选中数是否等于列表的总数，如果等于，那么就需要执行全部取消操作
					if (isChoice_all == Data.arrayList_cart.size()) {
						// 如果没有选中了全选，那么就将列表的每一行都不选
						for (int i = 0; i < Data.arrayList_cart.size(); i++) {
							// 列表每一行都取消
							((CheckBox) (listView_cart.getChildAt(i)).findViewById(R.id.cb_choice)).setChecked(false);
						}
					}
				}
			}
		});
		
		

		ll_cart = (LinearLayout) view.findViewById(R.id.ll_cart);
		listView_cart = (ListView) view.findViewById(R.id.listView_cart);
		// 如果购物车中有数据，那么就显示数据，否则显示默认界面
		if (Data.arrayList_cart != null && Data.arrayList_cart.size() != 0) {
			adapter = new Adapter_ListView_cart(getActivity(), Data.arrayList_cart);
			adapter.setOnCheckedChanged(this);
			listView_cart.setAdapter(adapter);
			ll_cart.setVisibility(View.GONE);
		} else {
			ll_cart.setVisibility(View.VISIBLE);
		}

		listView_cart.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(getActivity(), BabyActivity.class);
				startActivity(intent);
			}
		});
		
		
		
		tv_cart_buy_Ordel.setOnClickListener(this);
		tv_goShop.setOnClickListener(this);
	}


	@Override
	public void onAttach(Activity activity) {
		btnCallListener = (IBtnCallListener) activity;

		super.onAttach(activity);
	}

	@Override
	public void transferMsg() {
		// 这里响应在FragmentActivity中的控件交互
		System.out.println("由Activity中传送来的消息");
	}

	/** adapter的回调函数，当点击CheckBox的时候传递点击位置和checkBox的状态 */
	@Override
	public void getChoiceData(int position, boolean isChoice) {
		//得到点击的哪一行
		if (isChoice) {
			if (Data.arrayList_cart.size()!=0) {
				//49表示商品的价格，这里偷了下懒，没有去动态获取商品价格
				Data.Allprice_cart += Float.valueOf(Data.arrayList_cart.get(position).get("num").toString())*49;
			}
		} else {
			if (Data.arrayList_cart.size()!=0) {
				Data.Allprice_cart -= Float.valueOf(Data.arrayList_cart.get(position).get("num").toString())*49;
			}
		}
		// 记录列表处于选中状态的数量
		int num_choice = 0;
		for (int i = 0; i < Data.arrayList_cart.size(); i++) {
			// 判断列表中每一行的选中状态，如果是选中，计数加1
			if (null!=listView_cart.getChildAt(i)&&((CheckBox) (listView_cart.getChildAt(i)).findViewById(R.id.cb_choice)).isChecked()) {
				// 列表处于选中状态的数量+1
				num_choice += 1;
				is_choice[i]=true;
			}
		}
		// 判断列表中的CheckBox是否全部选择
		if (num_choice == Data.arrayList_cart.size()) {
			// 如果选中的状态数量=列表的总数量，那么就将全选设置为选中
			cb_cart_all.setChecked(true);
		} else {
			// 如果选中的状态数量！=列表的总数量，那么就将全选设置为取消
			cb_cart_all.setChecked(false);
		}

		tv_cart_Allprice.setText("合计：￥"+Data.Allprice_cart + "");

		System.out.println("选择的位置--->"+position);
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_goShop://点击去逛逛
			btnCallListener.transferMsg();
			break;
		case R.id.tv_cart_buy_or_del://点击结算/删除
			boolean[] is_choice_copy=is_choice;
			if (tv_cart_buy_Ordel.getText().toString().equals("删除")) {
				//执行删除操作
				if (Data.arrayList_cart.size()!=0) {
					for (int i = is_choice_copy.length-1; i >=0; i--) {
						if (is_choice_copy[i]) {
							((CheckBox) (listView_cart.getChildAt(i)).findViewById(R.id.cb_choice)).setChecked(false);
							Data.arrayList_cart.remove(i);
							is_choice_copy=deleteByIndex(is_choice, i);
						}
					}
				}
				
				
				if (Data.arrayList_cart.size()==0) {
					ll_cart.setVisibility(View.VISIBLE);
				}
				
				adapter.notifyDataSetChanged();
				is_choice=new boolean[Data.arrayList_cart.size()];
				System.out.println("此时的长度---->"+is_choice.length);
			}else {
				//执行结算操作
				Toast.makeText(getActivity(), "暂时无法结算", Toast.LENGTH_SHORT).show();
			}
			
			break;
		default:
			break;
		}
		
	}
	
	
	/**删除数组中的一个元素*/
    public static boolean[] deleteByIndex(boolean[] array, int index) {
    	boolean[] newArray = new boolean[array.length - 1];
        for (int i = 0; i < newArray.length; i++) {
            if (i < index) {
                newArray[i] = array[i];
            } else {
                newArray[i] = array[i + 1];
            }
        }
        return newArray;
    }

}
