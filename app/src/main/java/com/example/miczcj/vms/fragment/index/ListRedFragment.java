package com.example.miczcj.vms.fragment.index;

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
import com.example.miczcj.vms.model.QDItemDescription;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;


import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

//原本是 QMUIButton
@Widget(name = "红名单", iconRes = R.mipmap.list_red)
public class ListRedFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.listview)
    ListView listView;


    private SimpleAdapter adapter;
    private QDItemDescription mQDItemDescription;

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
        listView.setAdapter(adapter);
        //添加监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳出对话框 确认是否删除
            }
        });
    }

    public ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        String name[] = {"张昌健", "张忆霄"};
        String stuNum[] = {"201507420139", "201526810851"};
        String description[] = {"参与活动次数多，认真完成任务，礼貌接待需要帮助的人；作为领队，回馈及时认真 A",
                "在这两个月中积极参加志愿者活动，从无迟到现象，并且在活动中表现良好，展现出良好的志愿者形象。" +
                        "在活动中担任过领队，做好身为领队的工作 B"};
        int image[] = {R.color.app_color_theme_1, R.color.app_color_theme_1};

        for (int i = 0; i < 2; i++) {
            map = new HashMap<String, Object>();
            map.put("name", name[i]);
            map.put("stuNum", stuNum[i]);
            map.put("description", description[i]);
            map.put("image", image[i]);
            list.add(map);
        }

        return list;
    }
}
