
package com.tiger.quicknews.initview;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;

import com.tiger.quicknews.R;
import com.tiger.quicknews.wedget.slidingmenu.SlidingMenu;
import com.tiger.quicknews.wedget.slidingmenu.SlidingMenu.CanvasTransformer;

public class SlidingMenuView {
    private static SlidingMenuView slidingMenuView;

    public SlidingMenu slidingMenu;

    private CanvasTransformer mTransformer;

    public static SlidingMenuView instance() {
        if (slidingMenuView == null) {
            slidingMenuView = new SlidingMenuView();
        }
        return slidingMenuView;
    }

    public SlidingMenu initSlidingMenuView(Activity activity, View view) {
        // �˵���ʱ�Ķ���
        mTransformer = new CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, canvas.getWidth() / 2, canvas.getHeight() / 2);
            }
        };
        slidingMenu = new SlidingMenu(activity);
        slidingMenu.setMode(SlidingMenu.LEFT);// �������һ��˵�
        slidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// ����Ҫʹ�˵�������������Ļ�ķ�Χ
        // slidingMenuView.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);//������������ȡ�����˵�����Ľ��㣬������ע�͵�
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);// ������ӰͼƬ�Ŀ��
        slidingMenu.setShadowDrawable(R.drawable.shadow);// ������ӰͼƬ
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu����ʱ��ҳ����ʾ��ʣ����
        slidingMenu.setFadeDegree(0.35F);// SlidingMenu����ʱ�Ľ���̶�
        slidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);// ʹSlidingMenu������Activity�ұ�
        // slidingMenuView.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//����SlidingMenu�˵��Ŀ��
        slidingMenu.setMenu(view);// ����menu�Ĳ����ļ�
        // localSlidingMenu.toggle();//��̬�ж��Զ��رջ���SlidingMenu
        // slidingMenu.setSecondaryMenu(R.layout.activity_main);
        // slidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
        // ���ò˵��򿪶���
        // slidingMenu.setBehindCanvasTransformer(mTransformer);
        return slidingMenu;
    }

    public void setWeatherImage(ImageView mWeatherImage, String weather) {
        if (weather.equals("����") || weather.equals("����ת��") || weather.equals("����ת��")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        } else if (weather.equals("����") || weather.equals("�е�����")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        } else if (weather.equals("������")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        } else if (weather.equals("����") || weather.equals("����ת����")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        } else if (weather.equals("��ѩ")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        } else if (weather.equals("����")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        } else if (weather.equals("����")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        } else if (weather.equals("��ѩ")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_daxue);
        } else if (weather.equals("����") || weather.equals("����ת����")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_dayu);
        } else if (weather.equals("���������")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        } else if (weather.equals("��")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_qing);
        } else if (weather.equals("ɳ����")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        } else if (weather.equals("�ش���")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        } else if (weather.equals("��") || weather.equals("����")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_wu);
        } else if (weather.equals("Сѩ")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        } else if (weather.equals("С��")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        } else if (weather.equals("��")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_yin);
        } else if (weather.equals("���ѩ")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        } else if (weather.equals("��ѩ")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        } else if (weather.equals("��ѩ")) {
            mWeatherImage.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        }
    }

}
