package com.example.administrator.zhbj.Fragment.pager.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.zhbj.Bean.PhotoData;
import com.example.administrator.zhbj.Constant.GloableConstant;
import com.example.administrator.zhbj.R;
import com.example.administrator.zhbj.Utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 四级页面组图
 * Created by Administrator on 2016/6/29.
 */
public class PhotoMenuDetailPager extends BaseMenuDatailPager {

    private ListView mlistview;
    private GridView mgirview;
    private ImageView mIvRight;
    private static final String TAG="组图数据";
    private ListPhotoAdapter photoAdapter;
    private ListPhotoAdapter mgridViewadapter;

    /**
     * 构造方法
     *
     * @param activity+++
     * @param mTVRight
     */
    public PhotoMenuDetailPager(Activity activity, ImageView mTVRight) {
        super(activity);
        mIvRight = mTVRight;
        mIvRight.setImageResource(View.VISIBLE);
        initListeners();
    }

    private boolean islistview = true;
    private List<PhotoData.DataBean.NewsBean> list=null;
    @Override
    public View initView() {
//        TextView contentView=new TextView(mActivity);
//        contentView.setText("四级页面的：组图");
//         contentView.setBackgroundColor(Color.WHITE);
//        contentView.setTextColor(Color.RED);
//        contentView.setGravity(Gravity.CENTER);
        View view = View.inflate(mActivity, R.layout.menu_detail_pager_phone, null);
        mlistview = (ListView) view.findViewById(R.id.listview_phone);
        mgirview = (GridView) view.findViewById(R.id.listview_gridview);
        photoAdapter = new ListPhotoAdapter();
        mlistview.setAdapter(photoAdapter);
        mgridViewadapter = new ListPhotoAdapter();
        mgirview.setAdapter(mgridViewadapter);
        return view;
    }
    public class ViewHolderPic{
        TextView desc;
        ImageView pic;
    }
private class  ListPhotoAdapter extends BaseAdapter{

    private final BitmapUtils bitmapUtils;

    public ListPhotoAdapter(){
      list= new ArrayList<PhotoData.DataBean.NewsBean>();
        bitmapUtils = new BitmapUtils(mActivity);
      bitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
  }
    @Override
    public int getCount()  {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int posation, View convertView, ViewGroup viewGroup) {
//         1数据2视图优化3赋值
        ViewHolderPic holder=null;
        PhotoData.DataBean.NewsBean newsBean = list.get(posation);
        Log.i(TAG, "getView: "+newsBean.toString());
        if(convertView==null){
           holder=new ViewHolderPic();
            convertView=View.inflate(mActivity, R.layout.item_photos ,null);
          holder.pic= (ImageView) convertView.findViewById(R.id.photo_ImageView);
          holder.desc= (TextView) convertView.findViewById(R.id.photo_textview);
           //[☆]绑定
            convertView.setTag(holder);
        }else {
            //[☆]赋值
            holder= (ViewHolderPic) convertView.getTag();
           bitmapUtils.display(holder.pic,newsBean.getListimage());
            holder.desc.setText(newsBean.getTitle());
        }

        return convertView;
    }
}
    /**
     * 当前是组图鉴定器添加方法
     */
    private void initListeners() {
        mIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                islistview = !islistview;
                if (islistview) {//显示列表
                    mlistview.setVisibility(View.VISIBLE);
                    mgirview.setVisibility(View.GONE);
                    mIvRight.setImageResource(R.drawable.icon_pic_grid_type);
                } else {//显示网格视图
                    mlistview.setVisibility(View.GONE);
                    mgirview.setVisibility(View.VISIBLE);
                    mIvRight.setImageResource(R.drawable.icon_pic_list_type);
                }
            }
        });
    }

    /**
     * 初始化页面的数据方法
     * @param params
     */
    @Override
    public void initData(Object params) {
        super.initData(params);
//        photos/photos_1.json
        /**
         *   先从本地取缓存
         *   发请求到服务端获取新的的数据与跟新界面与缓存
         */
        final String cache = CacheUtils.getstring(mActivity, GloableConstant.URL_PHOTO);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        String url = GloableConstant.SERVER_HOST + GloableConstant.URL_PHOTO;
        Log.i(TAG, "initData: "+ url);
        //[☆]创建请求工具
        HttpUtils httpUtils = new HttpUtils();
        RequestCallBack<String>  callback=new RequestCallBack<String>() {
            /**
             *
             * @param responseInfo
             */
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
               processData((String) responseInfo.result);
                //[☆]缓存
                CacheUtils.saveString(mActivity,GloableConstant.URL_PHOTO, (String) responseInfo.result);
            }

            /**
             *
             * @param e
             * @param s
             */
            @Override
            public void onFailure(HttpException e, String s) {
            }
        };

        httpUtils.send(HttpRequest.HttpMethod.GET,url,callback);
    }

    private void processData(String json) {
         json = json.replaceAll("http://10.0.2.2:8080/zhbj", GloableConstant.SERVER_HOST);

//        创建核心控件
        Gson gson = new Gson();
        PhotoData data = gson.fromJson(json, PhotoData.class);
        Log.i(TAG, "解析后的json: >>"+data.toString());
        List<PhotoData.DataBean.NewsBean> newsBeen = data.getData().getNews();
        if(list==null){
            list=new ArrayList<PhotoData.DataBean.NewsBean>();
        list.clear();
        }
        list.addAll(newsBeen);
        photoAdapter.notifyDataSetChanged();
        mgridViewadapter.notifyDataSetChanged();
        Log.i(TAG, "processData: "+newsBeen.toString());
    }
}
