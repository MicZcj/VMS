package com.example.miczcj.vms.fragment.index;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lib.annotation.annotation.Widget;
import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.manager.QDDataManager;
import com.example.miczcj.vms.model.QDItemDescription;
import com.example.miczcj.vms.model.RecruitActivity;
import com.example.miczcj.vms.okhttp.BaseHttp;
import com.example.miczcj.vms.okhttp.ResMessage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.arch.QMUIFragment;
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

//原本是 QMUIButton
@Widget(name = "活动招募", iconRes = R.mipmap.recruit_add)
public class RecruitActivityNewFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.recruit_submit)
    Button mSubmit;
    @BindView(R.id.name)
    EditText nameEdt;
    @BindView(R.id.address)
    EditText addressEdt;
    @BindView(R.id.time)
    EditText timeEdt;
    @BindView(R.id.num)
    EditText numEdt;
    @BindView(R.id.content)
    EditText contentEdt;
    @BindView(R.id.selecttime)
    EditText selecttimeEdt;
    @BindView(R.id.phone)
    EditText phoneEdt;


    private String operation;
    private RecruitActivity ac;

    private QDItemDescription mQDItemDescription;
    private String name;
    private String address;
    private String time;
    private String num;
    private String content;
    private String phone;
    private String selecttime;
    private ResMessage resMessage;
    private String id;

    private Handler handler = new Handler();
    private Bundle bundle;

    @SuppressLint("HandlerLeak")

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recruit_activity_new, null);
        ButterKnife.bind(this, view);
        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());
        bundle = getArguments();
        try{
            operation = (String)bundle.get("operation");
            id = (String)bundle.get("id");
        }catch(NullPointerException e){
            operation= "new";
        }
        Log.i("opertaion的值是",operation);

        initTopBar();
        initContent();
        return view;
    }

    private void initContent() {
        if(operation.equals("edit")){
            ac = (RecruitActivity)bundle.getSerializable("recruitActivity");
            new Thread(){
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            nameEdt.setText(ac.getName());
                            addressEdt.setText(ac.getAddress());
                            timeEdt.setText(ac.getTime());
                            numEdt.setText(ac.getNum());
                            contentEdt.setText(ac.getContent());
                            selecttimeEdt.setText(ac.getSelecttime());
                            phoneEdt.setText(ac.getPhone());
                            mTopBar.setTitle("修改招募信息");
                        }
                    });
                }
            }.start();

        }
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    recruitPost();
            }
        });
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle("志愿者活动招募");
    }

    private void recruitPost() {
        BaseHttp baseHttp = new BaseHttp();
        name = nameEdt.getText().toString();
        address = addressEdt.getText().toString();
        time = timeEdt.getText().toString();
        num = numEdt.getText().toString();
        content = contentEdt.getText().toString();
        phone = phoneEdt.getText().toString();
        selecttime = selecttimeEdt.getText().toString();
        if (!valid()) {
            return;
        }
        SharedPreferences preference = getContext().getSharedPreferences("login_info", MODE_PRIVATE);
        String dept = preference.getString("dept", "");
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fromBody;
        Request request;
        if(operation.equals("new")){
            fromBody = new FormBody.Builder()
                    .add("name", name)
                    .add("address", address)
                    .add("time", time)
                    .add("num", num)
                    .add("content", content)
                    .add("phone", phone)
                    .add("selecttime", selecttime)
                    .add("dept", dept)
                    .build();
            request = new Request.Builder()
                    .url(baseHttp.getUrl() + "APIRecruitActivityNew")
                    .post(fromBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("新建志愿者活动招募失败", "未知原因！");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //TODO
                    String result = response.body().string();
                    Log.i("返回结果", result);
                    resMessage = new Gson().fromJson(result, ResMessage.class);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    thread.start();

                }
            });

        }else{
            fromBody = new FormBody.Builder()
                    .add("id",id)
                    .add("name", name)
                    .add("address", address)
                    .add("time", time)
                    .add("num", num)
                    .add("content", content)
                    .add("phone", phone)
                    .add("selecttime", selecttime)
                    .add("dept", dept)
                    .build();
            request = new Request.Builder()
                    .url(baseHttp.getUrl() + "APIRecruitAcitivityUpdate")
                    .post(fromBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    resMessage = new Gson().fromJson(result, ResMessage.class);
                    Log.i("修改招募信息后返回的结果",result);
                    new Thread(){
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"修改招募信息成功！",Toast.LENGTH_SHORT).show();
                                    popBackStack();
                                    popBackStack();
                                    popBackStack();
                                    BaseFragment fragment = new ActivityAllFragment();
                                    startFragment(fragment);
                                }
                            });
                        }
                    }.start();
                }
            });
        }


    }

    private boolean valid() {
        if (name.equals("")) {
            Toast.makeText(getContext(), "活动名称不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (name.length() > 15) {
            Toast.makeText(getContext(), "活动名称不能超过15个字", Toast.LENGTH_SHORT).show();
            return false;
        } else if (address.equals("")) {
            Toast.makeText(getContext(), "活动地址不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (time.equals("")) {
            Toast.makeText(getContext(), "活动时间不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (num.equals("")) {
            Toast.makeText(getContext(), "需求人数不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!num.matches("^[0-9]*$")) {
            Toast.makeText(getContext(), "需求人数必须为数字", Toast.LENGTH_SHORT).show();
            return false;
        } else if (content.equals("")) {
            Toast.makeText(getContext(), "活动详情不能为空", Toast.LENGTH_SHORT).show();
            return false;
//        }else if(content.length()<50){
//            Toast.makeText(getContext(),"活动详情至少50字",Toast.LENGTH_SHORT).show();
//            return false;
        } else if (selecttime.equals("")) {
            Toast.makeText(getContext(), "可报名时间不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone.equals("")) {
            Toast.makeText(getContext(), "联系人不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
