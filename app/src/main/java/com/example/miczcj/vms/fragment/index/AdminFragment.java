package com.example.miczcj.vms.fragment.index;

import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lib.annotation.annotation.Widget;
import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.manager.QDDataManager;
import com.example.miczcj.vms.model.QDItemDescription;
import com.example.miczcj.vms.okhttp.BaseHttp;
import com.example.miczcj.vms.okhttp.ResMessage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//原本是 QMUIButton
@Widget(name = "综合维护", iconRes = R.mipmap.admin_wrench)
public class AdminFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.listview)
    ListView mListView;

    private QDItemDescription mQDItemDescription;
    private int mCurrentDialogStyle = R.style.DialogTheme2;

    private CharSequence text="";
    private ResMessage resMessage;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private BaseHttp baseHttp = new BaseHttp();
    private Handler handler = new Handler();

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_listview, null);
        ButterKnife.bind(this, view);
        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());

        initTopBar();
        initListView();
        return view;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle(mQDItemDescription.getName());
    }

    private void initListView() {
        String[] listItems = new String[]{
                "开启上传通道",
                "删除工时表",
                "显示活动",
                "隐藏活动",
                "同步志愿中国数据"
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        mListView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_list_item, data));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        showEditTextDialog("开启通道", 0);
                        break;
                    case 1:
                        showEditTextDialog("删除工时表", 1);
                        break;
                    case 2:
                        showEditTextDialog("显示活动", 2);
                        break;
                    case 3:
                        showEditTextDialog("隐藏活动", 3);
                        break;
                    case 4:
                        showMessagePositiveDialog();
                        break;
                }
            }
        });
    }

    private void showEditTextDialog(String title, final int i) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle(title)
                .setPlaceholder("在此输入活动号")
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                });
        if (i == 1) {
            builder.addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    text = builder.getEditText().getText();
                    if (text != null && text.length() > 0) {
                        doPost("APIAdminDeleteFile", "");
                        dialog.dismiss();
                    }else {
                        Toast.makeText(getActivity(), "请输入活动号", Toast.LENGTH_SHORT).show();
                    }
                }
            }).create(mCurrentDialogStyle).show();
        } else {
            builder.addAction("确定", new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    text = builder.getEditText().getText();
                    if (text != null && text.length() > 0) {
                        switch(i){
                            case 0:
                                doPost("APIAdminOpenUpload","");
                                break;
                            case 2:
                                doPost("APIAdminShowHide","show");
                                break;
                            case 3:
                                doPost("APIAdminShowHide","hide");;
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), "请输入活动号", Toast.LENGTH_SHORT).show();
                    }
                }
            }).create(mCurrentDialogStyle).show();
        }
    }

    private void showMessagePositiveDialog() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("同步志愿中国数据")
                .setMessage("确定要同步数据吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        doPost("APIAdminFlushActivity","");;
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void doPost(String path, String type){
        FormBody formBody = new FormBody.Builder()
                .add("num",text.toString())
                .add("num1",text.toString())
                .add("type",type)
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
                Gson gson = new Gson();
                resMessage = gson.fromJson(result,ResMessage.class);
                showResult();
            }
        });
    }

    private void showResult(){
        new Thread(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),resMessage.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();
    }
}
