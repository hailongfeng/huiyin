package com.wizarpos.netpayrouter.utils;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.wizarpos.netpayrouter.R;

/**
 * @author xuchuanren
 * @date 2015年7月31日 下午4:03:57
 * @modify 修改人： 修改时间：
 */
public class KeyBoardOperate {

	private List<EditText> keyboardTextList = null;
	private List<MyKeyListener> myKeyListenerList = null;

	public KeyBoardOperate() {
		// TODO Auto-generated constructor stub
		keyboardTextList = new ArrayList<EditText>();
		myKeyListenerList = new ArrayList<MyKeyListener>();
	}

	/**
	 * money
	 * 
	 * @param view
	 * @param inputAmountEt
	 */
	public void initClickListener(Window view, final EditText inputAmountEt) {
		setKeyboard(view, new MyKeyListener() {
			public void onClick(String value) {
				if ("del".equals(value)) {
					deleteMoney(inputAmountEt);
				} else {
					inputMoney(inputAmountEt, value);
				}
			}
		}, inputAmountEt);
	}

	/**
	 * 
	 * @param view
	 * @param inputAmountEt
	 */
	public void initNumberClickListener(Window view,
			final EditText inputAmountEt) {
		setKeyboard(view, new MyKeyListener() {
			public void onClick(String value) {
				if ("del".equals(value)) {
					deleteNumber(inputAmountEt);
				} else {
					inputNumber(inputAmountEt, value);
				}
			}
		}, inputAmountEt);
	}

	public void initClickListener(Window view, final EditText etOperator,
			final EditText etPassword) {
		// 账户输入框
		if (etOperator != null) {
			setKeyboard(view, new MyKeyListener() {
				public void onClick(String value) {
					if ("del".equals(value)) {
						deleteNumber(etOperator);
					} else {
						inputNumber(etOperator, value);
					}
				}
			}, etOperator);
		}
		// 密码输入框
		if (etPassword != null) {
			setKeyboard(view, new MyKeyListener() {
				public void onClick(String value) {
					if ("del".equals(value)) {
						deletePassword(etPassword);
					} else {
						inputPassword(etPassword, value, 8);
					}
				}
			}, etPassword);
		}
	}

	public void canlKeyBoardListener(Window view, final EditText etOperator,
			final EditText etPassword) {
		// 账户输入框
		if (etOperator != null) {
			setKeyboard(view, null, etOperator);
		}
		// 密码输入框
		if (etPassword != null) {
			setKeyboard(view, null, etPassword);
		}
	}

	// 设置数字键盘
	public void setKeyboard(Window window, MyKeyListener keyListener,
			EditText editText) {

		if (editText == null || keyListener == null) {
			return;
		}
		if (keyboardTextList.contains(editText)) {
			return;
		}
		myKeyListenerList.add(keyListener);
		keyboardTextList.add(editText);
		Button key0 = (Button) window.findViewById(R.id.key_0);

		key0.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int index = getFocusedIndex();
				if (index == -1) {
					return;
				} else {
					myKeyListenerList.get(index).onClick("0");
				}
			}
		});
		Button key1 = (Button) window.findViewById(R.id.key_1);
		key1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int index = getFocusedIndex();
				if (index == -1) {
					return;
				} else {
					myKeyListenerList.get(index).onClick("1");
				}
			}
		});
		Button key2 = (Button) window.findViewById(R.id.key_2);
		key2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int index = getFocusedIndex();
				if (index == -1) {
					return;
				} else {
					myKeyListenerList.get(index).onClick("2");
				}
			}
		});
		Button key3 = (Button) window.findViewById(R.id.key_3);
		key3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int index = getFocusedIndex();
				if (index == -1) {
					return;
				} else {
					myKeyListenerList.get(index).onClick("3");
				}
			}
		});
		Button key4 = (Button) window.findViewById(R.id.key_4);
		key4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int index = getFocusedIndex();
				if (index == -1) {
					return;
				} else {
					myKeyListenerList.get(index).onClick("4");
				}
			}
		});
		Button key5 = (Button) window.findViewById(R.id.key_5);
		key5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int index = getFocusedIndex();
				if (index == -1) {
					return;
				} else {
					myKeyListenerList.get(index).onClick("5");
				}
			}
		});
		Button key6 = (Button) window.findViewById(R.id.key_6);
		key6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int index = getFocusedIndex();
				if (index == -1) {
					return;
				} else {
					myKeyListenerList.get(index).onClick("6");
				}
			}
		});
		Button key7 = (Button) window.findViewById(R.id.key_7);
		key7.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int index = getFocusedIndex();
				if (index == -1) {
					return;
				} else {
					myKeyListenerList.get(index).onClick("7");
				}
			}
		});
		Button key8 = (Button) window.findViewById(R.id.key_8);
		key8.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int index = getFocusedIndex();
				if (index == -1) {
					return;
				} else {
					myKeyListenerList.get(index).onClick("8");
				}
			}
		});
		Button key9 = (Button) window.findViewById(R.id.key_9);
		key9.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int index = getFocusedIndex();
				if (index == -1) {
					return;
				} else {
					myKeyListenerList.get(index).onClick("9");
				}
			}
		});

		// Button keyDot = (Button) window.findViewById(R.id.key_dot);
		// if (keyDot != null) {
		// keyDot.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// int index = getFocusedIndex();
		// if (index == -1) {
		// return;
		// } else {
		// myKeyListenerList.get(index).onClick(".");
		// }
		// }
		// });
		// }

		Button keyDel = (Button) window.findViewById(R.id.key_del);
		keyDel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int index = getFocusedIndex();
				if (index == -1) {
					return;
				} else {
					myKeyListenerList.get(index).onClick("del");
				}
			}
		});
	}

	private int getFocusedIndex() {
		for (int i = 0; i < keyboardTextList.size(); i++) {
			if (isInputable(keyboardTextList.get(i))) {
				return i;
			}
		}
		return -1;
	}

	/** 控件是否可键盘输入 */
	public boolean isInputable(View view) {
		return view.isEnabled() && view.isFocused();
	}

	public static abstract class MyKeyListener {
		public abstract void onClick(String value);
	}

	/** 删除数字 */
	public void deleteNumber(EditText editText) {
		String text = Tools.deleteNumber(editText.getText().toString());
		editText.setText(text);
	}

	/** 删除金额 */
	protected void deleteMoney(EditText editText) {
		String text = Tools.deleteMoney(editText.getText().toString());
		editText.setText(text);
	}

	/** 输入金额 */
	protected void inputMoney(EditText editText, String appendValue) {
		String text = Tools.inputMoney(editText.getText().toString(),
				appendValue);
		editText.setText(text);
	}

	/** 输入数字 */
	public void inputNumber(EditText editText, String appendValue) {
		String text = Tools.inputNumber(editText.getText().toString(),
				appendValue);
		editText.setText(text);
	}

	/** 删除密码 */
	public void deletePassword(EditText editText) {
		Object o = editText.getTag();
		String s = "";
		if (o != null) {
			s = o.toString();
		}
		String text = Tools.deleteNumber(s);
		editText.setTag(text);
		editText.setText(Tools.replace(text, '*', 0, text.length()));
	}

	/** 输入密码 */
	public void inputPassword(EditText editText, String appendValue, int length) {
		Object o = editText.getTag();
		String s = "";
		if (o != null) {
			s = o.toString();
		}
		if (length != -1 && s.length() >= length) {
			return;
		}
		String text = Tools.inputNumber(s, appendValue);
		editText.setTag(text);
		editText.setText(Tools.replace(text, '*', 0, text.length()));
	}

}
