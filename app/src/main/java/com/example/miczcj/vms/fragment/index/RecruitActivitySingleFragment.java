package com.example.miczcj.vms.fragment.index;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.example.miczcj.vms.model.QDItemDescription;
import com.example.miczcj.vms.model.RecruitActivity;
import com.example.miczcj.vms.model.RecruitStudent;
import com.example.miczcj.vms.okhttp.BaseHttp;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



/**
 * Created by MicZcj on 2018/5/27.
 */
@Widget(group = Group.Other, name = "活动详情")
public class RecruitActivitySingleFragment extends BaseFragment {
    //private static final String ARG_INDEX = "arg_index";

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    private View view1;
    private QMUIAlphaTextView name;
    private QMUIAlphaTextView dept;
    private QMUIAlphaTextView address;
    private QMUIAlphaTextView time;
    private QMUIAlphaTextView num;
    private QMUIAlphaTextView content;
    private QMUIAlphaTextView phone;
    private RecruitActivity activity;


    private View view2;
    private ListView listview2;
    private SimpleAdapter adapter;
    private ArrayList<RecruitStudent> list  = new ArrayList<>();

    private String id;

    private Handler handler = new Handler();

    private Map<ContentPage, View> mPageMap = new HashMap<>();
    private ContentPage mDestPage = ContentPage.Item1;
    private QDItemDescription mQDItemDescription;
    private PagerAdapter mPagerAdapter = new PagerAdapter() {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        id = (String)bundle.get("id");
        doPost();
    }

    @Override
    protected View onCreateView() {
        Bundle args = getArguments();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_recruit_activity_single, null);
        ButterKnife.bind(this, view);

        initTopBar();
        initTabAndPager();


        return view;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mTopBar.setTitle("浙江图书馆志愿者活动");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheetList();
                    }
                });
    }

    private void showBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("开启活动招募")
                .addItem("停止活动招募")
                .addItem("修改活动招募")
                .addItem("删除本招募信息")
                .addItem("导出报名信息（Excel）")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                //开启活动招募
                                break;
                            case 1:
                                //停止活动招募
                                break;
                            case 2:
                                //修改活动招募,跳转到RecruitNewActivity
                                QMUIFragment fragment = new RecruitActivityNewFragment();
                                startFragment(fragment);
                                break;
                            case 3:
                                //删除本招募信息
                                break;
                            case 4:
                                break;
                        }
                    }
                })
                .build()
                .show();
    }

    private void initTabAndPager() {
        mContentViewPager.setAdapter(mPagerAdapter);
        mContentViewPager.setCurrentItem(mDestPage.getPosition(), false);
        mTabSegment.setHasIndicator(true);
        mTabSegment.setIndicatorPosition(false);
        mTabSegment.setIndicatorWidthAdjustContent(false);
        mTabSegment.addTab(new QMUITabSegment.Tab("招募详情"));
        mTabSegment.addTab(new QMUITabSegment.Tab("报名信息"));
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

    public static RecruitActivitySingleFragment newInstance(int index) {
        Bundle args = new Bundle();
        RecruitActivitySingleFragment fragment = new RecruitActivitySingleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View getPageView(ContentPage page) {

        View view;


        if (page == ContentPage.Item1) {
            view1 = View.inflate(getContext(), R.layout.recruit_single, null);
            name = view1.findViewById(R.id.name);
            dept = view1.findViewById(R.id.dept);
            address = view1.findViewById(R.id.address);
            time=view1.findViewById(R.id.time);
            num = view1.findViewById(R.id.num);
            content = view1.findViewById(R.id.content);
            phone = view1.findViewById(R.id.phone);
            view = view1;
            //把数据库去除的数据设置进去
        } else {
            view2 = View.inflate(getContext(),R.layout.list_activity,null);
            listview2 = view2.findViewById(R.id.listview);
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

    private void doPost(){
        BaseHttp baseHttp = new BaseHttp();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("id",id)
                .build();
        Request request = new Request.Builder()
                .url(baseHttp.getUrl()+"APIRecruitActivitySingle")
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
                JsonObject jsonObject = je.getAsJsonObject();
                if(je.getAsJsonObject().get("code").toString().equals("0")){
                    Gson gson = new Gson();
                    activity = gson.fromJson(jsonObject.getAsJsonObject("activity"),RecruitActivity.class);
                    JsonArray jsonArray = jsonObject.getAsJsonArray("list");
                    for(JsonElement student:jsonArray){
                        RecruitStudent recruitStudent = gson.fromJson(student,RecruitStudent.class);
                        list.add(recruitStudent);
                    }
                }else{
                    Toast.makeText(getContext(), "查询结果为空", Toast.LENGTH_SHORT).show();
                }

                initPage1();
                initPage2();
                //设置page2的item监听事件
                listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        RecruitStudent rs = list.get(position);
                        QMUIFragment fragment = newInstance(rs);
                        startFragment(fragment);
                    }
                });

            }
        });
    }
    private void initPage1(){
        new Thread(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(activity.getName());
                        dept.setText(activity.getDept());
                        address.setText(activity.getAddress());
                        time.setText(activity.getTime());
                        num.setText(activity.getNum());
                        content.setText(activity.getContent());
                        phone.setText(activity.getPhone());
                        mTopBar.setTitle(activity.getName());
                    }
                });
            }
        }.start();
    }
    private void initPage2(){
        adapter = new SimpleAdapter(getContext(),getData(list),R.layout.list_activity_item,
                new String[]{"name","num"},
                new int[]{R.id.name,R.id.num});
        new Thread(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       listview2.setAdapter(adapter);
                       adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }
    private ArrayList<HashMap<String, Object>> getData(ArrayList<RecruitStudent> asList){
        ArrayList<HashMap<String, Object>> returnList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        for (RecruitStudent as : asList) {
            map = new HashMap<String, Object>();
            map.put("name", as.getName());
            map.put("num", as.getStuid());
            returnList.add(map);
        }
        return returnList;
    }
    public static RecruitInfoFragment newInstance(RecruitStudent rs) {
        //数据传入
        Bundle args = new Bundle();
        args.putSerializable("recruitStudent",rs);
        RecruitInfoFragment fragment = new RecruitInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
