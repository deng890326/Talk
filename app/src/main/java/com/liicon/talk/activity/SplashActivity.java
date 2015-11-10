package com.liicon.talk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.liicon.talk.CustomApplcation;
import com.liicon.talk.R;
import com.liicon.talk.config.Config;
import com.liicon.talk.utils.CollectionUtils;
import com.liicon.talk.utils.LogUtils;

import java.util.List;

import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.FindListener;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";

    private static final int GO_HOME = 100;
    private static final int GO_LOGIN = 200;

    private BmobUserManager userManager;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    break;
                case GO_LOGIN:
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //可设置调试模式，当为true的时候，会在logcat的BmobChat下输出一些日志，包括推送服务是否正常运行，如果服务端返回错误，也会一并打印出来。方便开发者调试
        BmobChat.DEBUG_MODE = true;
        //BmobIM SDK初始化--只需要这一段代码即可完成初始化
        //请到Bmob官网(http://www.bmob.cn/)申请ApplicationId,具体地址:http://docs.bmob.cn/android/faststart/index.html?menukey=fast_start&key=start_android
        BmobChat.getInstance(this).init(Config.applicationId);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userManager.getCurrentUser() != null) {
            // 每次自动登陆的时候就需要更新下当前位置和好友的资料，因为好友的头像，昵称啥的是经常变动的
            updateUserInfos();
            mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
        }
    }

}
