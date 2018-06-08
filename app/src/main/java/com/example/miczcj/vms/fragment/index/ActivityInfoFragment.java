package com.example.miczcj.vms.fragment.index;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.model.VolunteerActivity;
import com.example.miczcj.vms.okhttp.BaseHttp;
import com.example.miczcj.vms.okhttp.ResMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.widget.QMUIFontFitTextView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;


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
 * Created by MicZcj on 2018/5/27.
 */

public class ActivityInfoFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.status)
    QMUIFontFitTextView statusTxt;
    @BindView(R.id.num)
    QMUIAlphaTextView numTxt;
    @BindView(R.id.id)
    QMUIAlphaTextView idTxt;
    @BindView(R.id.title)
    QMUIAlphaTextView titleTxt;
    @BindView(R.id.deptname)
    QMUIAlphaTextView deptnameTxt;
    @BindView(R.id.actstarttime)
    QMUIAlphaTextView actstarttimeTxt;
    @BindView(R.id.actendtime)
    QMUIAlphaTextView actendtimeTxt;
    @BindView(R.id.content)
    QMUIAlphaTextView contentTxt;
    @BindView(R.id.worksheet)
    QMUIAlphaTextView worksheetTxt;


    private int mCurrentDialogStyle = R.style.DialogTheme2;

    private String dept;
    private String num;
    private String approval;
    private String name;
    private ResMessage resMessage;
    private CharSequence text = "";
    private VolunteerActivity va;
    private String workSheet = "";
    private Handler handler = new Handler();
    private OkHttpClient okHttpClient = new OkHttpClient();
    private BaseHttp baseHttp = new BaseHttp();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        num = (String) bundle.get("num");
        dept = (String) bundle.get("dept");
        approval = (String) bundle.get("approval");
        if (num == null) {
            num = "not";
        }
        if (dept == null) {
            dept = "not";
        }
        if (approval == null) {
            approval = "not";
        }
        doPost();

    }

    @Override
    public View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_info_detail, null);
        ButterKnife.bind(this, view);
        initTopBar();
//        initContent();
        return view;
    }

    private void initContent() {
        new Thread() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (va.getStauts().equals("审核未通过") || va.getStauts().equals("工时表超时未交")) {
                            statusTxt.setTextColor(getResources().getColor(R.color.app_color_theme_1));
                        } else if (va.getStauts().equals("工时表等待提交") || va.getStauts().equals("待审核")) {
                            statusTxt.setTextColor(getResources().getColor(R.color.app_color_theme_2));
                        }
                        statusTxt.setText(va.getStauts());
                        numTxt.setText(va.getNum());
                        idTxt.setText(va.getId());
                        titleTxt.setText(va.getTitle());
                        deptnameTxt.setText(va.getDeptname());
                        actstarttimeTxt.setText(va.getActstarttime());
                        actendtimeTxt.setText(va.getActendtime());
                        contentTxt.setText(va.getActendtime());
                        worksheetTxt.setText(workSheet);
                        mTopBar.setTitle(va.getTitle());
                    }
                });
            }
        }.start();
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        if (approval.equals("approval")) {
            QMUIAlphaImageButton temp = mTopBar.addRightImageButton(R.mipmap.activity_approval, R.id.topbar_right_approval_button);
            temp.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditTextDialog();
                }
            });
        }
        //设置标题

    }

    private void showEditTextDialog() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle("活动审批")
                .setPlaceholder("在此输入审批意见")
                .setInputType(InputType.TYPE_CLASS_TEXT);
        builder.addAction(0, "不通过", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            doPostApproval("12");
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "请输入审批意见", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addAction("通过", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            doPostApproval("11");
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "请输入审批意见", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create(mCurrentDialogStyle).show();
    }

    private void doPost() {
        FormBody formBody = new FormBody.Builder()
                .add("num", num)
                .add("dept", dept)
                .build();
        Request request = new Request.Builder()
                .url(baseHttp.getUrl() + "APIAcitivityShowSingle")
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JsonElement je = new JsonParser().parse(result);
                JsonObject jo = je.getAsJsonObject();
                Gson gson = new Gson();
                va = gson.fromJson(jo.getAsJsonObject("activity"), VolunteerActivity.class);
                workSheet = jo.get("workSheet").toString();
                initContent();
            }
        });
    }

    private void doPostApproval(String status) {
        SharedPreferences preferences = getContext().getSharedPreferences("login_info", MODE_PRIVATE);
        name = preferences.getString("name", "");
        FormBody formBody = new FormBody.Builder()
                .add("stauts", status)
                .add("name", name)
                .add("num", num)
                .add("content", text.toString())
                .build();
        Request request = new Request.Builder()
                .url(baseHttp.getUrl() + "APIAdminActivitySave")
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                resMessage = gson.fromJson(result, ResMessage.class);
                showResult();
            }
        });
    }

    private void showResult() {
        new Thread() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), resMessage.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();
    }

}
