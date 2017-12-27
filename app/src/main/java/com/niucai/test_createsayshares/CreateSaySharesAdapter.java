package com.niucai.test_createsayshares;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.niucai.test_createsayshares.avtivity.SaySharesActivity;

import java.util.List;

/**
 * Created by Qi on 2017/12/25.
 */

public class CreateSaySharesAdapter extends BaseAdapter {
    private List<Integer> list;
    private Context context;

    public CreateSaySharesAdapter(List<Integer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return 5;
    }

    @Override
    public long getItemId(int position) {
        return 5;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_addtitle, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_SayShares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "录制", Toast.LENGTH_SHORT).show();

                context.startActivity(new Intent(context, SaySharesActivity.class));
            }
        });

        holder.tv_item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "编辑", Toast.LENGTH_SHORT).show();
            }
        });

        holder.tv_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "删除", Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public EditText et_addTitle;
        public TextView tv_SayShares;
        public TextView tv_item_edit;
        public TextView tv_item_delete;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.et_addTitle = (EditText) rootView.findViewById(R.id.et_addTitle);
            this.tv_SayShares = (TextView) rootView.findViewById(R.id.tv_SayShares);
            this.tv_item_edit = (TextView) rootView.findViewById(R.id.tv_item_edit);
            this.tv_item_delete = (TextView) rootView.findViewById(R.id.tv_item_delete);
        }

    }
}
