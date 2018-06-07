package com.example.miczcj.vms.fragment.index;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.lib.annotation.annotation.Widget;
import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.manager.QDDataManager;
import com.example.miczcj.vms.model.List;
import com.example.miczcj.vms.model.QDItemDescription;
import com.example.miczcj.vms.okhttp.BaseHttp;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//原本是 QMUIButton
@Widget(name = "红名单", iconRes = R.mipmap.list_red)
public class ListRedFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.listview)
    ListView listView;


    private SimpleAdapter adapter;
    private ArrayList<List> list = new ArrayList<List>();

    private Handler handler = new Handler();
    private Bundle bundle;
    private BaseHttp baseHttp = new BaseHttp();
    private OkHttpClient okHttpClient = new OkHttpClient();

    private QDItemDescription mQDItemDescription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doPost("1");
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_listview, null);
        ButterKnife.bind(this, view);
        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());

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
        QMUIAlphaImageButton temp=mTopBar.addRightImageButton(R.mipmap.list_add, R.id.topbar_right_add_button);

        temp.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QMUIFragment fragment = new ListAddFragment();
                startFragment(fragment);
            }
        });
        mTopBar.setTitle(mQDItemDescription.getName());
    }

    private void initContent() {
        adapter = new SimpleAdapter(getContext(), getData(), R.layout.list_rby_item,
                new String[]{"name", "stuNum", "description", "image"},
                new int[]{R.id.name, R.id.stuNum, R.id.description, R.id.imageView});
        new Thread(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                        //添加监听事件
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                                //跳出对话框 确认是否删除
                            }
                        });
                    }
                });
            }
        }.start();
    }

    public ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> returnList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;

        for (int i = 0; i < list.size(); i++) {
            map = new HashMap<String, Object>();
            map.put("name", list.get(i).getName());
            map.put("stuNum", list.get(i).getNum());
            map.put("description", list.get(i).getDcb());
            map.put("image", R.color.app_color_theme_1);
            returnList.add(map);
        }

        return returnList;
    }

    private void doPost(String type){
        FormBody formBody = new FormBody.Builder()
                .add("type",type)
                .build();
        Request request = new Request.Builder()
                .url(baseHttp.getUrl()+"APIListShow")
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
                JsonArray ja = jo.getAsJsonArray("list");
                Gson gson = new Gson();
                for(JsonElement l : ja){
                    List temp = gson.fromJson(l,List.class);
                    list.add(temp);
                }
                initContent();
            }
        });
    }
}
