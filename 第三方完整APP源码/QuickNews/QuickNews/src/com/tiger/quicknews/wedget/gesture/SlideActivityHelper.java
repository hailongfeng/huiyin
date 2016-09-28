
package com.tiger.quicknews.wedget.gesture;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.FrameLayout;

import com.tiger.quicknews.R;
import com.tiger.quicknews.wedget.gesture.GestureViewGroup.GestureViewGroupGoneListener;

/**
 * 鐢ㄤ簬瀹炵幇婊戝姩鍏抽棴鐣岄潰鏁堟灉鐨勮緟鍔╃被 from SlideBaseActivity
 * 
 * @author xuewenchao
 */
public class SlideActivityHelper {
    private final Activity activity;
    private GestureViewGroup gesturellView;

    public SlideActivityHelper(Activity activity) {
        this.activity = activity;
    }

    protected void onCreate() {
        activity.overridePendingTransition(R.anim.slide_right_in, 0);
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gesturellView = new GestureViewGroup(activity);
        gesturellView.setGestureViewGroupGoneListener(new GestureViewGroupGoneListener() {
            @Override
            public void onFinish() {
                activity.finish(); // 鐣岄潰婊戝姩娑堝け鍚庯紝閿?瘉 Activity锛?
            }
        });

    }

    void onResume() {
        FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
        View decorView_child = decorView.getChildAt(0);

        // 浣跨敤 GestureViewGroup 灏佽 DecorView 涓殑鍐呭锛?
        if (!(decorView_child instanceof GestureViewGroup)) {
            decorView.removeAllViews();
            decorView_child.setBackgroundResource(R.drawable.window_background);
            gesturellView.addView(decorView_child);

            decorView.addView(gesturellView);
        }
    }

    void finish() {
        activity.overridePendingTransition(0, R.anim.slide_right_out);
    }

}
