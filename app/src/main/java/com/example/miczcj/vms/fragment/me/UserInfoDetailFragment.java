package com.example.miczcj.vms.fragment.me;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.model.User;
import com.example.miczcj.vms.okhttp.BaseHttp;
import com.example.miczcj.vms.okhttp.ResMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;


import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by MicZcj on 2018/5/30.
 */

public class UserInfoDetailFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.uid)
    QMUIAlphaTextView uid;
    @BindView(R.id.name)
    QMUIAlphaTextView name;
    @BindView(R.id.dept)
    QMUIAlphaTextView dept;
    @BindView(R.id.authority)
    QMUIAlphaTextView authority;

    private User user;
    private ResMessage resMessage;
    private Handler handler = new Handler();
    private OkHttpClient okHttpClient = new OkHttpClient();
    private BaseHttp baseHttp = new BaseHttp();

    @Override
    public View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.user_info_detail, null);
        ButterKnife.bind(this, view);
        initTopBar();
        initContent();
        return view;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        //设置标题
        mTopBar.setTitle("用户详情");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheetList();
                    }
                });

    }

    private void initContent(){
        new Thread(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = getArguments();
                        user = (User)bundle.getSerializable("user");
                        uid.setText(user.getUid()+"");
                        name.setText(user.getUsername());
                        dept.setText(user.getDept());
                        String authorityStr = "";
                        String temp = user.getAuthority();
                        if (temp.contains("1")) {
                            authorityStr += "活动招募\n";
                        }
                        if (temp.contains("2")) {
                            authorityStr += "活动管理\n";
                        }
                        if (temp.contains("3")) {
                            authorityStr += "工时查询\n";
                        }
                        if (temp.contains("4")) {
                            authorityStr += "维护管理\n";
                        }
                        if (temp.contains("5")) {
                            authorityStr += "红黑名单管理\n";
                        }
                        if (temp.contains("9")) {
                            authorityStr += "超级管理\n";
                        }
                        authority.setText(authorityStr);
                    }
                });
            }
        }.start();
    }

    private void showBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("删除用户")
                .addItem("重置密码")
                .addItem("修改用户信息")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                doPost("APIAdminUserDelete");
                                popBackStack();
                                popBackStack();
                                break;
                            case 1:
                                doPost("APIAdminUserResetPassword");
                                break;
                            case 2:
                                QMUIFragment fragment = newInstance(user);
                                startFragment(fragment);
                                break;
                        }
                    }
                })
                .build()
                .show();
    }

    private static UserAddFragment newInstance(User u){
        //数据传入
        Bundle args = new Bundle();
        args.putSerializable("user",u);
        UserAddFragment fragment = new UserAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void doPost(String path){
        FormBody formBody = new FormBody.Builder()
                .add("uid",user.getUid()+"")
                .build();
        Request request = new Request.Builder()
                .url(baseHttp.getUrl()+path)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("result",result);
                Gson gson = new Gson();
                resMessage = gson.fromJson(result, ResMessage.class);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showResult();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void showResult(){
        final QMUITipDialog tipDialog;
        if(resMessage.getCode()==0) {
            tipDialog = new QMUITipDialog.Builder(getContext())
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                    .setTipWord(resMessage.getMessage())
                    .create();
            tipDialog.show();
        }else{
            tipDialog = new QMUITipDialog.Builder(getContext())
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                    .setTipWord(resMessage.getMessage())
                    .create();
            tipDialog.show();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 1500);


    }

}
