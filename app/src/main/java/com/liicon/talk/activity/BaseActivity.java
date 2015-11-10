package com.liicon.talk.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.liicon.talk.CustomApplcation;
import com.liicon.talk.utils.CollectionUtils;
import com.liicon.talk.utils.LogUtils;

import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wei on 15-11-10.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    BmobUserManager userManager;
    BmobChatManager manager;

    CustomApplcation mApplication;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        userManager = BmobUserManager.getInstance(this);
        manager = BmobChatManager.getInstance(this);
        mApplication = CustomApplcation.getSingleton();
    }

    Toast mToast;

    public void ShowToast(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(BaseActivity.this.getApplicationContext(), msg,
                            Toast.LENGTH_LONG);
                } else {
                    mToast.setText(msg);
                }
                mToast.show();
            }
        });
    }

    public void ShowToast(int resId) {
        ShowToast(getString(resId));
    }

    public void showOfflineDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CustomApplcation.getSingleton().logout();
                        startActivity(new Intent(context, LoginActivity.class));
                        finish();
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    /** 用于登陆或者自动登陆情况下的用户资料及好友资料的检测更新
     * @Title: updateUserInfos
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    public void updateUserInfos(){
        //更新地理位置信息
        //查询该用户的好友列表(这个好友列表是去除黑名单用户的哦),目前支持的查询好友个数为100，如需修改请在调用这个方法前设置BmobConfig.LIMIT_CONTACTS即可。
        //这里默认采取的是登陆成功之后即将好于列表存储到数据库中，并更新到当前内存中,
        userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub
                if (arg0 == BmobConfig.CODE_COMMON_NONE) {
                    LogUtils.i(TAG, arg1);
                } else {
                    LogUtils.e(TAG, "查询好友列表失败：" + arg1);
                }
            }

            @Override
            public void onSuccess(List<BmobChatUser> arg0) {
                // TODO Auto-generated method stub
                // 保存到application中方便比较
                CustomApplcation.getSingleton().setContactList(CollectionUtils.list2map(arg0));
            }
        });
    }
}
