package com.example.miczcj.vms.fragment.index;

import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
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
 * Created by MicZcj on 2018/5/28.
 */
public class ListAddFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.name)
    EditText nameEdt;
    @BindView(R.id.num)
    EditText numEdt;
    @BindView(R.id.dcb)
    EditText dcbEdt;
    @BindView(R.id.dept)
    EditText deptEdt;
    @BindView(R.id.list_submit)
    Button mSubmit;
    @BindView(R.id.type)
    RadioGroup typeRg;
    @BindView(R.id.red)
    RadioButton red;
    @BindView(R.id.yellow)
    RadioButton yellow;
    @BindView(R.id.black)
    RadioButton black;

    private String name;
    private String num;
    private String dcb;
    private String dept;
    private String type;
    private ResMessage resMessage;
    private RadioButton rb;

    private BaseHttp baseHttp = new BaseHttp();
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler =new Handler();


    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list_add, null);
        ButterKnife.bind(this, view);
        initTopBar();
        initContent();

        return view;
    }

    private void initContent() {
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPost();
            }
        });
        typeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                red.setTextColor(getResources().getColor(R.color.app_color_blue));
                yellow.setTextColor(getResources().getColor(R.color.app_color_blue));
                black.setTextColor(getResources().getColor(R.color.app_color_blue));
                red.setBackgroundColor(getResources().getColor(R.color.qmui_config_color_pressed));
                yellow.setBackgroundColor(getResources().getColor(R.color.qmui_config_color_pressed));
                black.setBackgroundColor(getResources().getColor(R.color.qmui_config_color_pressed));
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.red:
                        red.setTextColor(getResources().getColor(R.color.qmui_config_color_75_white));
                        red.setBackgroundColor(getResources().getColor(R.color.app_color_theme_6));
                        break;
                    case R.id.yellow:
                        yellow.setTextColor(getResources().getColor(R.color.qmui_config_color_75_white));
                        yellow.setBackgroundColor(getResources().getColor(R.color.app_color_theme_6));
                        break;
                    case R.id.black:
                        black.setTextColor(getResources().getColor(R.color.qmui_config_color_75_white));
                        black.setBackgroundColor(getResources().getColor(R.color.app_color_theme_6));
                        break;
                }
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

        mTopBar.setTitle("添加红黑名单");
    }

    private void doPost(){
        name = nameEdt.getText().toString();
        num = numEdt.getText().toString();
        dcb = dcbEdt.getText().toString();
        rb = (RadioButton)getActivity().findViewById(typeRg.getCheckedRadioButtonId());
        type = rb.getText().toString();
        if(type.equals("红名单")) {
            type="1";
        }else if(type.equals("黑名单")){
            type="3";
        }else {
            type="2";
        }
        if (!valid()) {
            return;
        }
        SharedPreferences preference = getContext().getSharedPreferences("login_info", MODE_PRIVATE);
        dept = preference.getString("dept", "");

        FormBody formBody = new FormBody.Builder()
                .add("name",name)
                .add("num",num)
                .add("dept",dept)
                .add("type",type)
                .add("dcb",dcb)
                .build();
        Request reuqest = new Request.Builder()
                .url(baseHttp.getUrl()+"APIListNew")
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

    private boolean valid() {
        if (name.equals("")) {
            Toast.makeText(getContext(), "志愿者姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (name.equals("")) {
            Toast.makeText(getContext(), "志愿者学号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dcb.equals("")) {
            Toast.makeText(getContext(), "主要事迹不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showResult(){
        final QMUITipDialog tipDialog;
        if(resMessage.getCode()==0) {
            tipDialog = new QMUITipDialog.Builder(getContext())
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                    .setTipWord("提交成功")
                    .create();
            tipDialog.show();
        }else{
            tipDialog = new QMUITipDialog.Builder(getContext())
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                    .setTipWord("提交失败")
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

