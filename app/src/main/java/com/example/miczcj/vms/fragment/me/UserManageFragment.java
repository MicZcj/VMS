package com.example.miczcj.vms.fragment.me;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.lib.annotation.annotation.Widget;
import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.fragment.index.ActivityInfoFragment;
import com.example.miczcj.vms.manager.QDDataManager;
import com.example.miczcj.vms.model.QDItemDescription;
import com.example.miczcj.vms.model.User;
import com.example.miczcj.vms.model.VolunteerActivity;
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
import java.io.Serializable;
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
 * Created by MicZcj on 2018/5/30.
 */
@Widget(name = "用户管理")
public class UserManageFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.listview)
    ListView listview;

    private SimpleAdapter adapter;
    private ArrayList<User> list = new ArrayList<User>();

    private Handler handler = new Handler();
    private BaseHttp baseHttp = new BaseHttp();
    private OkHttpClient okHttpClient = new OkHttpClient();


    private QDItemDescription mQDItemDescription;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doPost();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_activity, null);
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
        QMUIAlphaImageButton temp = mTopBar.addRightImageButton(R.mipmap.user_add, R.id.topbar_right_add_button);

        temp.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QMUIFragment fragment = new UserAddFragment();
                startFragment(fragment);
            }
        });
        mTopBar.setTitle(mQDItemDescription.getName());
    }

    private void initContent() {
        adapter = new SimpleAdapter(getContext(),getData(),R.layout.list_activity_item,
                new String[]{"uidName","dept"},
                new int[]{R.id.name,R.id.num});
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
        for (User u : list) {
            map = new HashMap<String, Object>();
            map.put("uidName", u.getUid()+" "+u.getUsername());
            map.put("dept", u.getDept());
            returnlist.add(map);
        }
        return returnlist;
    }

    private void doPost(){
        FormBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(baseHttp.getUrl()+"APIAdminUserShowAll")
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
                for(JsonElement l :ja){
                    User u = gson.fromJson(l,User.class);
                    list.add(u);
                }
                initContent();
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User user = list.get(position);
                        QMUIFragment fragment = newInstance(user);
                        startFragment(fragment);
                    }
                });
            }
        });
    }

    public static UserInfoDetailFragment newInstance(User user) {
        //数据传入
        Bundle args = new Bundle();
        args.putSerializable("user",(Serializable) user);
        UserInfoDetailFragment fragment = new UserInfoDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
