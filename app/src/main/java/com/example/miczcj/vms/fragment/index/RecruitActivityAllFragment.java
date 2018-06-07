package com.example.miczcj.vms.fragment.index;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.lib.annotation.Group;
import com.example.lib.annotation.annotation.Widget;
import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.manager.QDDataManager;
import com.example.miczcj.vms.model.QDItemDescription;
import com.example.miczcj.vms.model.RecruitActivity;
import com.example.miczcj.vms.okhttp.BaseHttp;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * {@link QMUIGroupListView} 的使用示例。
 * Created by Kayo on 2016/11/21.
 */

@Widget(group = Group.INDEX, name = "我的招募", iconRes = R.mipmap.recruit_all)
public class RecruitActivityAllFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;




    private View view1;
    private View view2;
    private SimpleAdapter adapter1;
    private SimpleAdapter adapter2;
    private ListView listview1;
    private ListView listview2;

    ArrayList<RecruitActivity> list1 = new ArrayList<RecruitActivity>();
    ArrayList<RecruitActivity> list2 = new ArrayList<RecruitActivity>();

    private Map<ContentPage, View> mPageMap = new HashMap<>();
    private ContentPage mDestPage = ContentPage.Item1;
    private QDItemDescription mQDItemDescription;
    private PagerAdapter mPagerAdapter;
    private Handler handler =new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("运行到这里","1");
        doPost();
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recruit_activity, null);
        ButterKnife.bind(this, root);

        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());
        mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return ContentPage.SIZE;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(final ViewGroup container, int position) {
                ContentPage page = ContentPage.getPage(position);
                Log.i("position",""+position);
                Log.i("运行到这里","4");
                View view = getPageView(page);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                container.addView(view, params);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };
        initTopBar();
        initTabAndPager();
        /**
         * 查出来的 我的活动 用下面的方法加进入
         */


        return root;
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

    public static RecruitActivitySingleFragment newInstance(String id, String flag) {
        //数据传入
        Bundle args = new Bundle();
        args.putString("id",id);
        args.putString("flag",flag);
        RecruitActivitySingleFragment fragment = new RecruitActivitySingleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void doPost() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.i("运行到这里","2");
                    BaseHttp baseHttp = new BaseHttp();
                    SharedPreferences preference = getContext().getSharedPreferences("login_info", MODE_PRIVATE);
                    String dept = preference.getString("dept", "");
                    //1.new一个OkhttpClient对象
                    OkHttpClient okHttpClient = new OkHttpClient();
                    //2.构造Request对象

                    FormBody fromBody = new FormBody.Builder()
                            .add("dept", dept)
                            .build();
                    Request request = new Request.Builder()
                            .url(baseHttp.getUrl() + "APIRecruitActivity")
                            .post(fromBody)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    Response response = call.execute();
                    String result = response.body().string();
                    JsonElement je = new JsonParser().parse(result);
                    JsonObject jsonObject = je.getAsJsonObject();
                    if (je.getAsJsonObject().get("code").toString().equals("0")) {
                        JsonArray jsonArray1 = jsonObject.getAsJsonArray("activityList1");

                        JsonArray jsonArray2 = jsonObject.getAsJsonArray("activityList2");
                        Gson gson = new Gson();
                        for (JsonElement activity : jsonArray1) {
                            RecruitActivity recruitActivity = gson.fromJson(activity, RecruitActivity.class);
                            list1.add(recruitActivity);
                        }
                        for (JsonElement activity : jsonArray2) {
                            RecruitActivity recruitActivity = gson.fromJson(activity, RecruitActivity.class);
                            list2.add(recruitActivity);
                        }
                    } else {
                        Toast.makeText(getContext(), "查询结果为空", Toast.LENGTH_SHORT).show();
                    }
                    initListView(list1,1);
                    initListView(list2,2);
                    listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String id1 = list1.get(position).getId();
                            String flag1 = list1.get(position).getFlag();
                            QMUIFragment fragment = newInstance(id1,flag1);
                            startFragment(fragment);
                            //Toast.makeText(getContext(),id1,Toast.LENGTH_SHORT).show();
                        }
                    });

                    listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String id2 = list2.get(position).getId();
                            String flag2 = list2.get(position).getFlag();
                            QMUIFragment fragment = newInstance(id2,flag2);
                            startFragment(fragment);
                           // Toast.makeText(getContext(),id2,Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.i("list1大小",list1.size()+"");
                    Log.i("list2大小",list2.size()+"");
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initListView(ArrayList<RecruitActivity> list,int i) {
        Log.i("i=",i+"");
        if(i==1){
            adapter1 = new SimpleAdapter(getContext(), getData(list), R.layout.list_activity_item,
                    new String[]{"name", "num"},
                    new int[]{R.id.name, R.id.num});
        }else{
            adapter2 = new SimpleAdapter(getContext(), getData(list), R.layout.list_activity_item,
                    new String[]{"name", "num"},
                    new int[]{R.id.name, R.id.num});
        }


        new Thread(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listview1.setAdapter(adapter1);
                        adapter1.notifyDataSetChanged();
                        listview2.setAdapter(adapter2);
                        adapter2.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }

    public ArrayList<HashMap<String, Object>> getData(ArrayList<RecruitActivity> acList) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        for (RecruitActivity ac : acList) {
            map = new HashMap<String, Object>();
            map.put("name", ac.getName());
            map.put("num", ac.getNum() + "人");
            list.add(map);
        }
        Log.i("ArrayList大小",list.size()+"");
        return list;
    }

    private void initTabAndPager() {
        mContentViewPager.setAdapter(mPagerAdapter);
        mContentViewPager.setCurrentItem(mDestPage.getPosition(), false);
        mTabSegment.setHasIndicator(true);
        mTabSegment.setIndicatorPosition(false);
        mTabSegment.setIndicatorWidthAdjustContent(false);
        mTabSegment.addTab(new QMUITabSegment.Tab("进行中"));
        mTabSegment.addTab(new QMUITabSegment.Tab("已结束"));
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }

    private View getPageView(ContentPage page) {

        View view;


        if (page == ContentPage.Item1) {
            view1 = View.inflate(getContext(), R.layout.list_activity, null);
            listview1 = view1.findViewById(R.id.listview);
            initListView(list1,1);
            view = view1;
        } else {
            view2 = View.inflate(getContext(), R.layout.list_activity, null);
            listview2 = view2.findViewById(R.id.listview);
            initListView(list2,2);
            view = view2;
        }
        return view;
    }

    public enum ContentPage {
        Item1(0),
        Item2(1);
        public static final int SIZE = 2;
        private final int position;

        ContentPage(int pos) {
            position = pos;
        }

        public static ContentPage getPage(int position) {
            switch (position) {
                case 0:
                    return Item1;
                case 1:
                    return Item2;
                default:
                    return Item1;
            }
        }

        public int getPosition() {
            return position;
        }
    }
}
