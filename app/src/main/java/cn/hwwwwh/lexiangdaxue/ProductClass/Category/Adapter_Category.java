package cn.hwwwwh.lexiangdaxue.ProductClass.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Category;
import cn.hwwwwh.lexiangdaxue.R;

/**
 * Created by 97481 on 2016/11/13.
 */
public class Adapter_Category extends BaseAdapter {
    private Context  context;
    private List<Category> list;
    private LayoutInflater inflater;
    private ListView listView;

    public Adapter_Category(Context context,List<Category> list){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category categoryDetail=list.get(position);
        ViewHolder viewHolder;
        if(convertView ==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.category_lv,null);
            viewHolder.tv_summary=(TextView)convertView.findViewById(R.id.category_name);
            viewHolder.imageView=(ImageView)convertView.findViewById(R.id.gg);
            convertView.setTag(viewHolder);
            viewHolder.tv_summary.setText(categoryDetail.getSummary());
            if(position==0){
                viewHolder.imageView.setVisibility(View.VISIBLE);
                convertView.setBackground(convertView.getResources().getDrawable(R.drawable.lv_select_bg));
            }
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder{
        private TextView tv_summary;
        private ImageView imageView;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
