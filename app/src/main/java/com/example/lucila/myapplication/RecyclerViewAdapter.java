package com.example.lucila.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    String[] titles;
    TypedArray icons;
    Context context;

    RecyclerViewAdapter(String[] titles, TypedArray icons, Context context) {

        this.titles = titles;
        this.icons = icons;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView  navTitle;
        ImageView navIcon;
        Context context;

        public ViewHolder(View drawerItem, int itemType,Context context) {
            super(drawerItem);
            this.context = context;

            if(itemType==1){
                navTitle = (TextView) itemView.findViewById(R.id.tv_NavTitle);
                navIcon = (ImageView) itemView.findViewById(R.id.iv_NavIcon);
                drawerItem.setOnClickListener(this);
            }
        }


        @Override
        public void onClick(View v) {
            Intent i= new Intent(context, MenuActivity.class);
            i.putExtra("opcion",getPosition());
            context.startActivity(i);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType==1){
        View itemLayout =   layoutInflater.inflate(R.layout.drawer_item_layout,null);
        return new ViewHolder(itemLayout,viewType,context);
        }
        else if (viewType==0) {
            View itemHeader = layoutInflater.inflate(R.layout.header_layout,null);
            return new ViewHolder(itemHeader,viewType,context);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position!=0){
            holder.navTitle.setText(titles[position - 1]);
            holder.navIcon.setImageResource(icons.getResourceId(position-1,-1));
        }
    }

    @Override
    public int getItemCount() {

        return titles.length+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)return 0;
        else return 1;
    }

}