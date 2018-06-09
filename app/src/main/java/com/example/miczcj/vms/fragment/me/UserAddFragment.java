package com.example.miczcj.vms.fragment.me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.model.User;
import com.example.miczcj.vms.okhttp.BaseHttp;
import com.example.miczcj.vms.okhttp.ResMessage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.QMUITopBar;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by MicZcj on 2018/5/30.
 */

public class UserAddFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView((R.id.list_submit))
    Button mSubmit;
    @BindView(R.id.name)
    EditText nameEdt;
    @BindView(R.id.dept)
    EditText deptEdt;
    @BindView(R.id.cb1)
    CheckBox cb1;
    @BindView(R.id.cb2)
    CheckBox cb2;
    @BindView(R.id.cb3)
    CheckBox cb3;
    @BindView(R.id.cb4)
    CheckBox cb4;
    @BindView(R.id.cb5)
    CheckBox cb5;
    @BindView(R.id.cb9)
    CheckBox cb9;

    private String name;
    private String dept;
    private String auth1="1";
    private String auth2="2";
    private String auth3="3";
    private String auth4="4";
    private String auth5="5";
    private String auth9="9";

    private String operation;
    private User user;
    private String path = "APIAdminUserNew";


    private ResMessage resMessage;
    private BaseHttp baseHttp = new BaseHttp();
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler =new Handler();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        try{
            user =(User)bundle.getSerializable("user");
            path = "APIAdminUserUpdateSave";
        }catch (NullPointerException e){
            user = null;
        }

    }

    @Override
    protected View onCreateView(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user_add,null);
        ButterKnife.bind(this, view);
        initTopBar();
        initContent();
        return view;
    }

    private void initTopBar(){
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle("添加用户");
    }

    private void initContent() {
        if(user!=null){
            nameEdt.setText(user.getUsername());
            deptEdt.setText(user.getDept());
            if(user.getAuthority().contains("1")){
                cb1.setChecked(true);
            }
            if(user.getAuthority().contains("2")){
                cb2.setChecked(true);
            }
            if(user.getAuthority().contains("3")){
                cb3.setChecked(true);
            }
            if(user.getAuthority().contains("4")){
                cb4.setChecked(true);
            }
            if(user.getAuthority().contains("5")){
                cb5.setChecked(true);
            }
            if(user.getAuthority().contains("9")){
                cb9.setChecked(true);
            }
        }
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPost();
            }
        });
    }
    FormBody.Builder formBodyBuild = new FormBody.Builder();
    private void doPost(){
        name = nameEdt.getText().toString();
        dept = deptEdt.getText().toString();
        if(cb1.isChecked()){
            formBodyBuild.add("auth1",auth1);
        }
        if(cb2.isChecked()){
            formBodyBuild.add("auth2",auth2);
        }
        if(cb3.isChecked()){
            formBodyBuild.add("auth3",auth3);
        }
        if(cb4.isChecked()){
            formBodyBuild.add("auth4",auth4);
        }
        if(cb5.isChecked()){
            formBodyBuild.add("auth5",auth5);
        }
        if(cb9.isChecked()){
            formBodyBuild.add("auth9",auth9);
        }
        if(user!=null){
            formBodyBuild.add("uid",user.getUid()+"");
        }
        formBodyBuild.add("name",name)
                .add("dept",dept);

        FormBody formBody = formBodyBuild
                .build();
        Request reuqest = new Request.Builder()
                .url(baseHttp.getUrl()+path)
                .post(formBody)
                .build();
        okHttpClient.newCall(reuqest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                resMessage = new Gson().fromJson(result, ResMessage.class);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showResult();
                                popBackStack();
                                popBackStack();
                            }
                        });
                    }
                });
                thread.start();
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
        mSubmit.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 1500);
    }
}
