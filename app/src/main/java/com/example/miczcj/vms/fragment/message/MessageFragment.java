package com.example.miczcj.vms.fragment.message;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.lib.annotation.annotation.Widget;
import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.manager.QDDataManager;
import com.example.miczcj.vms.model.Message;
import com.example.miczcj.vms.model.QDItemDescription;
import com.example.miczcj.vms.model.User;
import com.example.miczcj.vms.okhttp.BaseHttp;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

/**
 * Created by MicZcj on 2018/6/9.
 */
@Widget(name = "系统消息", iconRes = R.mipmap.vms_logo)
public class MessageFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.listview)
    ListView listview;

    private SimpleAdapter adapter;
    private ArrayList<Message> list = new ArrayList<Message>();

    private Handler handler = new Handler();
    private BaseHttp baseHttp = new BaseHttp();
    private OkHttpClient okHttpClient = new OkHttpClient();

    private QDItemDescription mQDItemDescription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doPost();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_listview, null);
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
        mTopBar.setTitle(mQDItemDescription.getName());
    }
    private void initContent() {
        adapter = new SimpleAdapter(getContext(),getData(),R.layout.list_message_item,
                new String[]{"title","content","time"},
                new int[]{R.id.title,R.id.content,R.id.time});
        new Thread(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                });
            }
        }.start();
    }

    private ArrayList<HashMap<String, Object>> getData(){
        ArrayList<HashMap<String, Object>> returnlist = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        for (Message m : list) {
            map = new HashMap<String, Object>();
            map.put("title", m.getTitle());
            map.put("content", m.getContent());
            map.put("time", m.getTime());
            returnlist.add(map);
        }
        return returnlist;
    }

    public void doPost(){
        FormBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(baseHttp.getUrl()+"APIMessage")
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
                for(JsonElement m :ja){
                    Message message = gson.fromJson(m,Message.class);
                    list.add(message);
                }
                Log.i("list的大小",list.size()+"");
                initContent();
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Message message = list.get(position);
//                        Toast.makeText()
                    }
                });
            }
        });
    }
}
