package com.lenovo.cooltest.uiadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovo.cooltest.MyFragmentHome;
import com.lenovo.cooltest.utils.ImageLoader;
import com.lenovo.cooltest.R;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liufeng23 on 2017/8/21.
 */

public class HomeListViewAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public HomeListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> al) {
        mActivity = activity;
        data = al;
        inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(mActivity.getApplicationContext());
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi=view;
        if(view==null)
            vi = inflater.inflate(R.layout.home_listview_raw, null);

        TextView fruitname = (TextView)vi.findViewById(R.id.fruit_title); // fruit-name
        TextView fruitplace = (TextView)vi.findViewById(R.id.fruit_place); // fruit-place
        TextView fruitprice = (TextView)vi.findViewById(R.id.fruit_price); // fruit-price
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.fruit_list_image); //thumbnail

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(i);

        // 设置ListView的相关值
        fruitname.setText(song.get(MyFragmentHome.KEY_FRUIT_NAME));
        fruitplace.setText(song.get(MyFragmentHome.KEY_FRUIT_PLACE));
        fruitprice.setText(song.get(MyFragmentHome.KEY_FRUIT_PRICE));
        imageLoader.DisplayImage(song.get(MyFragmentHome.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}
