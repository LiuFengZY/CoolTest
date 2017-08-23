package com.lenovo.cooltest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lenovo.cooltest.uiadapter.HomeListViewAdapter;
import com.lenovo.cooltest.utils.XMLParser;
import java.util.ArrayList;
import java.util.HashMap;

public class MyFragmentHome extends Fragment {

    MainActivity mMainActivity;

    public static final String KEY_FRUIT = "fruitgoods"; // parent node
    public static final String KEY_ID = "id";
    public static final String KEY_FRUIT_NAME = "fruitname";
    public static final String KEY_FRUIT_PLACE = "fruitplace";
    public static final String KEY_FRUIT_PRICE = "fruitprice";
    public static final String KEY_THUMB_URL = "thumb_url";

    static final String URL = "http://10.103.110.206:80/text.xml";
    ListView mHomeList;
    HomeListViewAdapter mHomeListAdapter;

    public static final String MSG_KEY_UPDATE_UI = "xml_update_ui";
    public static final int MSG_UPDATE_HOME_UI = 1001;

    /**
     *
     */
    public MyFragmentHome() {
    }

    ArrayList<HashMap<String, String>> songsList = null;
    private XMLParser parser = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_view, container, false);
        mMainActivity = (MainActivity)getActivity();

        songsList = new ArrayList<HashMap<String, String>>();
        mHomeList = (ListView) view.findViewById(R.id.home_listview);
        mHomeListAdapter=new HomeListViewAdapter(getActivity(), songsList);
        mHomeList.setAdapter(mHomeListAdapter);

        //为单一列表行添加单击事件
        mHomeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                System.out.println("onclick, position:" + position + ". id :" + id);
            }
        });

        //Load xml data.
        parser = new XMLParser(this.getContext(), mHandler, MSG_KEY_UPDATE_UI, MSG_UPDATE_HOME_UI);
        parser.doRequestXmlStringFromUrl(URL); // 从网络获取xml

        return view;
    }

    private void initUI() {

    }
    private void updateUI(String xml) {
        Document doc = parser.getDomElement(xml); // 获取 DOM 节点
        NodeList nl = doc.getElementsByTagName(KEY_FRUIT);
        // 循环遍历所有的歌节点 <song>
        for (int i = 0; i < nl.getLength(); i++) {
            // 新建一个 HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            //每个子节点添加到HashMap关键= >值
            map.put(KEY_ID, parser.getValue(e, KEY_ID));
            map.put(KEY_FRUIT_NAME, parser.getValue(e, KEY_FRUIT_NAME));
            map.put(KEY_FRUIT_PLACE, parser.getValue(e, KEY_FRUIT_PLACE));
            map.put(KEY_FRUIT_PRICE, parser.getValue(e, KEY_FRUIT_PRICE));
            map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));

            // HashList添加到数组列表
            songsList.add(map);
        }

        mHomeListAdapter.notifyDataSetChanged();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_HOME_UI:
                    String xml = msg.getData().getString(MSG_KEY_UPDATE_UI);
                    System.out.println("liufeng, update ui: xml: " + xml);
                    updateUI(xml);
                    break;
                default:
                    break;
            }
        }
    };
}
