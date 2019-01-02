package com.xin.lv.yang.utils.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xin.lv.yang.utils.R;
import com.xin.lv.yang.utils.info.PicItem;
import com.xin.lv.yang.utils.photo.image.AlbumBitmapCacheHelper;
import com.xin.lv.yang.utils.utils.BitmapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择一张图片
 */

public class PictureSelectorOneActivity extends AppCompatActivity {
    GridView gridView;
    RelativeLayout relativeLayout;
    private String mCurrentCatalog = "";
    private int perWidth;
    ListView mCatalogListView;
    TextView textView;
    LinearLayout linearLayout;
    ImageButton imageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_one_layout);
        gridView = findViewById(R.id.gridlist);
        relativeLayout = findViewById(R.id.catalog_window);
        mCatalogListView = findViewById(R.id.catalog_listview);
        textView = findViewById(R.id.type_text);
        linearLayout = findViewById(R.id.linear);
        imageButton = findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AlbumBitmapCacheHelper.init(this);

        updatePictureItems();

        gridView.setAdapter(new PicAdapter());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PicItem picItem = mAllItemList.get(position);
                String url = picItem.uri;
                Intent data = new Intent();
                data.putExtra(Intent.EXTRA_RETURN_RESULT, url);
                setResult(RESULT_OK, data);
                finish();

            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (relativeLayout.getVisibility() == View.VISIBLE) {
                    relativeLayout.setVisibility(View.GONE);
                } else {
                    relativeLayout.setVisibility(View.VISIBLE);
                }

            }
        });

        mCatalogListView.setAdapter(new CatalogAdapter());

        mCatalogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String catalog;
                if (position == 0) {
                    catalog = "all";
                } else {
                    catalog = mCatalogList.get(position - 1);
                }

                if (catalog.equals(mCurrentCatalog)) {
                    relativeLayout.setVisibility(View.GONE);
                } else {
                    mCurrentCatalog = catalog;
                    TextView textView = view.findViewById(R.id.name);
                    PictureSelectorOneActivity.this.textView.setText(textView.getText().toString());
                    relativeLayout.setVisibility(View.GONE);
                    // 筛选数据
                    if (catalog.equals("all")) {
                        mAllItemList = beifenList;
                        // 选择所有，无key值信息
                        mCurrentCatalog = "";
                    } else {
                        mAllItemList = mItemMap.get(mCurrentCatalog);
                    }

                    if (mAllItemList != null) {
                        ((PicAdapter) gridView.getAdapter()).notifyDataSetChanged();
                    }

                    ((CatalogAdapter) mCatalogListView.getAdapter()).notifyDataSetChanged();

                }
            }
        });

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        perWidth = (windowManager.getDefaultDisplay().getWidth() - BitmapUtil.dip2px(PictureSelectorOneActivity.this, 4.0f)) / 3;

    }


    private List<PicItem> mAllItemList;
    List<PicItem> beifenList;
    private Map<String, List<PicItem>> mItemMap;
    private List<String> mCatalogList;

    /**
     * 获取图片数据
     */
    private void updatePictureItems() {
        String[] projection = new String[]{"_data", "date_added"};
        String orderBy = "datetaken DESC";
        Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, orderBy);
        mAllItemList = new ArrayList<>();
        mItemMap = new HashMap<>();
        mCatalogList = new ArrayList<>();
        beifenList = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    PicItem item = new PicItem();
                    item.uri = cursor.getString(0);
                    if (item.uri != null) {
                        // 添加所有图片
                        this.mAllItemList.add(item);

                        int last = item.uri.lastIndexOf("/");

                        String catalog;
                        if (last == 0) {
                            catalog = "/";
                        } else {
                            int itemList = item.uri.lastIndexOf("/", last - 1);
                            catalog = item.uri.substring(itemList + 1, last);
                        }

                        if (this.mItemMap.containsKey(catalog)) {
                            (mItemMap.get(catalog)).add(item);
                        } else {
                            ArrayList<PicItem> itemList1 = new ArrayList<>();
                            itemList1.add(item);

                            mItemMap.put(catalog, itemList1);
                            mCatalogList.add(catalog);
                        }

                    }
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        Log.i("result", "集合长度-----" + mAllItemList.size());

        beifenList.addAll(mAllItemList);

    }


    /**
     * 图片显示的adapter
     */
    private class PicAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAllItemList == null ? 0 : mAllItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAllItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Log.i("result", "加载数据长度-----" + mAllItemList.size());

            VH vh;

            if (convertView == null) {
                vh = new VH();

                convertView = LayoutInflater.from(PictureSelectorOneActivity.this).inflate(R.layout.image_layout, parent,false);

                Log.i("result","convertView是否为空-----"+convertView);

                vh.imageView = convertView.findViewById(R.id.img_image_view);

                Log.i("result","是否为空-----"+vh.imageView);

                int w = getW() / 3;

                vh.imageView.setLayoutParams(new ViewGroup.LayoutParams(w, w));
                vh.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                vh.imageView.setPadding(2, 2, 2, 2);

                convertView.setTag(vh);

            } else {

                vh = (VH) convertView.getTag();

            }

            String path = mAllItemList.get(position).uri;

            AlbumBitmapCacheHelper.getInstance().addPathToShowlist(path);

            Bitmap bitmap = AlbumBitmapCacheHelper.getInstance().getBitmap(path, PictureSelectorOneActivity.this.perWidth, PictureSelectorOneActivity.this.perWidth, new AlbumBitmapCacheHelper.ILoadImageCallback() {
                @RequiresApi(api = Build.VERSION_CODES.DONUT)
                public void onLoadImageCallBack(Bitmap bitmap, String path1, Object... objects) {
                    if (bitmap != null) {
                        BitmapDrawable bd = new BitmapDrawable(PictureSelectorOneActivity.this.getResources(), bitmap);
                        View v = PictureSelectorOneActivity.this.gridView.findViewWithTag(path1);
                        if (v != null) {
                            v.setBackgroundDrawable(bd);
                        }

                    }
                }

            }, Integer.valueOf(position));

            vh.imageView.setImageBitmap(bitmap);

            return convertView;
        }

        private class VH {
            ImageView imageView;
        }

    }

    public int getW() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        return width;
    }

    /**
     * 筛选的adapter
     */
    private class CatalogAdapter extends BaseAdapter {
        private LayoutInflater mInflater = getLayoutInflater();

        public CatalogAdapter() {
        }

        public int getCount() {
            return mItemMap.size() + 1;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return (long) position;
        }

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @RequiresApi(api = Build.VERSION_CODES.DONUT)
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;
            if (convertView == null) {
                view = this.mInflater.inflate(R.layout.rc_picsel_catalog_listview, parent, false);
                holder = new ViewHolder();
                holder.image = view.findViewById(R.id.image);
                holder.name = view.findViewById(R.id.name);
                holder.number = view.findViewById(R.id.number);
                holder.selected = view.findViewById(R.id.selected);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String path;
            if (holder.image.getTag() != null) {
                path = (String) holder.image.getTag();
                AlbumBitmapCacheHelper.getInstance().removePathFromShowlist(path);
            }

            int num = 0;
            boolean showSelected = false;
            String name;
            Bitmap bitmap;
            BitmapDrawable bd;

            if (position == 0) {
                if (mItemMap.size() == 0) {
                    holder.image.setImageResource(R.drawable.rc_picsel_empty_pic);
                } else {
                    path = ((PicItem) ((List) mItemMap.get(mCatalogList.get(0))).get(0)).uri;

                    if (!path.equals("")) {
                        AlbumBitmapCacheHelper.getInstance().addPathToShowlist(path);
                        holder.image.setTag(path);
                        bitmap = AlbumBitmapCacheHelper.getInstance().getBitmap(path, perWidth, perWidth, new AlbumBitmapCacheHelper.ILoadImageCallback() {
                            @RequiresApi(api = Build.VERSION_CODES.DONUT)
                            public void onLoadImageCallBack(Bitmap bitmap, String path1, Object... objects) {
                                if (bitmap != null) {
                                    BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
                                    View v = gridView.findViewWithTag(path1);
                                    if (v != null) {
                                        v.setBackgroundDrawable(bd);
                                        notifyDataSetChanged();
                                    }

                                }

                            }
                        }, Integer.valueOf(position));

                        if (bitmap != null) {
                            bd = new BitmapDrawable(getResources(), bitmap);
                            holder.image.setBackgroundDrawable(bd);
                        } else {
                            holder.image.setBackgroundResource(R.drawable.rc_grid_image_default);
                        }
                    }
                }

                name = getResources().getString(R.string.rc_picsel_catalog_allpic);
                holder.number.setVisibility(View.GONE);
                showSelected = mCurrentCatalog.isEmpty();

            } else {
                path = ((PicItem) ((List) mItemMap.get(mCatalogList.get(position - 1))).get(0)).uri;
                name = mCatalogList.get(position - 1);
                num = (mItemMap.get(mCatalogList.get(position - 1))).size();
                holder.number.setVisibility(View.VISIBLE);
                showSelected = name.equals(mCurrentCatalog);
                AlbumBitmapCacheHelper.getInstance().addPathToShowlist(path);
                holder.image.setTag(path);

                bitmap = AlbumBitmapCacheHelper.getInstance().getBitmap(path, perWidth, perWidth, new AlbumBitmapCacheHelper.ILoadImageCallback() {
                    public void onLoadImageCallBack(Bitmap bitmap, String path1, Object... objects) {
                        if (bitmap != null) {
                            BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
                            View v = gridView.findViewWithTag(path1);
                            if (v != null) {
                                v.setBackgroundDrawable(bd);
                                notifyDataSetChanged();
                            }

                        }
                    }
                }, Integer.valueOf(position));
                if (bitmap != null) {
                    bd = new BitmapDrawable(getResources(), bitmap);
                    holder.image.setBackgroundDrawable(bd);
                } else {
                    holder.image.setBackgroundResource(R.drawable.rc_grid_image_default);
                }
            }

            holder.name.setText(name);
            holder.number.setText(String.format(getResources().getString(R.string.rc_picsel_catalog_number), new Object[]{Integer.valueOf(num)}));
            holder.selected.setVisibility(showSelected ? View.VISIBLE : View.INVISIBLE);
            return view;
        }

        private class ViewHolder {
            ImageView image;
            TextView name;
            TextView number;
            ImageView selected;

            private ViewHolder() {
            }
        }
    }

}
