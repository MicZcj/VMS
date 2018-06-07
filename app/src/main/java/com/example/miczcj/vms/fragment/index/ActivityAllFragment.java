package com.example.miczcj.vms.fragment.index;

import android.content.SharedPreferences;
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
import com.example.miczcj.vms.model.QDItemDescription;
import com.example.miczcj.vms.model.RecruitActivity;
import com.example.miczcj.vms.model.VolunteerActivity;
import com.example.miczcj.vms.okhttp.BaseHttp;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


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

import static android.content.Context.MODE_PRIVATE;

//原本是 QMUIButton
@Widget(name = "全部活动", iconRes = R.mipmap.activity_all)
public class ActivityAllFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.listview)
    ListView listview;

    private SimpleAdapter adapter;
    private ArrayList<VolunteerActivity> list = new ArrayList<VolunteerActivity>();


    private Handler handler = new Handler();
    private Bundle bundle;
    private BaseHttp baseHttp = new BaseHttp();
    private OkHttpClient okHttpClient = new OkHttpClient();

    private QDItemDescription mQDItemDescription;
    private String name;
    private String dept;
    private String status;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        try{
            name = (String) bundle.get("name");
        }catch(NullPointerException e){
            name = "all";
        }
        doPost();
//        Bundle bundle = getArguments();
//        name = (String)bundle.get("name");
//        dept = preference.getString("dept","");
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_activity, null);
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
        adapter = new SimpleAdapter(getContext(),getData(),R.layout.list_activity_item_status,
                new String[]{"name","status"},
                new int[]{R.id.name,R.id.status});
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
        for (VolunteerActivity va : list) {
            map = new HashMap<String, Object>();
            map.put("name", va.getTitle());
            map.put("status", va.getStauts());
            returnlist.add(map);
        }
        return returnlist;
    }

    private void doPost(){
        SharedPreferences preference;
        FormBody formBody;
        Request request;
        if(name.equals("all")){
            preference = getContext().getSharedPreferences("login_info", MODE_PRIVATE);
            dept = preference.getString("dept","");
            status = "0";
            formBody = new FormBody.Builder()
                    .add("dept",dept)
                    .add("stauts",status)
                    .build();
           request = new Request.Builder()
                    .url(baseHttp.getUrl()+"APIActivityShowAll")
                    .post(formBody)
                    .build();
        }else{
            formBody = new FormBody.Builder()
                    .add("name",name)
                    .build();
            request = new Request.Builder()
                    .url(baseHttp.getUrl()+"APIActivityQuery")
                    .post(formBody)
                    .build();
        }


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("返回的结果是",result);
                JsonElement je = new JsonParser().parse(result);
                JsonObject jo = je.getAsJsonObject();
                JsonArray ja = jo.getAsJsonArray("activityList");
                Gson gson = new Gson();
                for(JsonElement activity :ja){
                    VolunteerActivity va = gson.fromJson(activity,VolunteerActivity.class);
                    list.add(va);
                }
                initContent();
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String num = list.get(position).getNum();
                        QMUIFragment fragment = newInstance(num,dept);
                        startFragment(fragment);
                    }
                });
            }
        });


    }

    public static ActivityInfoFragment newInstance(String num, String dept) {
        //数据传入
        Bundle args = new Bundle();
        args.putString("num",num);
        args.putString("dept",dept);
        ActivityInfoFragment fragment = new ActivityInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
