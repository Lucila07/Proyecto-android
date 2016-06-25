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


    /**
     *Its a inner class to RecyclerViewAdapter Class.
     *This ViewHolder class implements View.OnClickListener to handle click events.
     *If the itemType==1 ; it implies that the view is a single row_item with TextView and ImageView.
     *This ViewHolder describes an item view with respect to its place within the RecyclerView.
     *For every item there is a ViewHolder associated with it .
     */

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView  navTitle;
        ImageView navIcon;
        Context context;

        public ViewHolder(View drawerItem, int itemType,Context context) {
            super(drawerItem);
            this.context = context;
            drawerItem.setOnClickListener(this);
            if(itemType==1){
                navTitle = (TextView) itemView.findViewById(R.id.tv_NavTitle);
                navIcon = (ImageView) itemView.findViewById(R.id.iv_NavIcon);
            }
        }



        @Override
        public void onClick(View v) {


           //----- MainActivity mainActivity = (MainActivity)context;
          /*  MainActivity mainActivity = (MainActivity)context;
            mainActivity.drawerLayout.closeDrawers();//------ esto es lo q no me andaba!!
            mainActivity.setInvisibleToolbar();
        */
              Intent i= new Intent(context, menuActivity.class);
              i.putExtra("opcion",getPosition());
               context.startActivity(i);


           // i= new Intent(context, PerfilActivity.class);
           // context.startActivity(i)
/*
            FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
            Intent i;
            switch (getPosition()){
                case 1: {
                 //----   i = new Intent(context, PerfilActivity.class);
                    //----  context.startActivity(i);
                    PerfilFragment perfilFragment = new PerfilFragment();
                    fragmentTransaction.replace(R.id.containerView, perfilFragment);
                    fragmentTransaction.commit();
                    break;
                }
                case 2:
                   // Fragment fixtureFragment = new FixtureFragment();
                    //fragmentTransaction.replace(R.id.containerView,fixtureFragment);
                    //fragmentTransaction.commit();
                    break;
                case 3:

                    /*
                     i= new Intent(context, ReservasUsuarioActivity.class);
                    context.startActivity(i);
                        */
             //       ReservasUsuarioFragment reservasFragment= new ReservasUsuarioFragment();
               //     fragmentTransaction.replace(R.id.containerView,reservasFragment);
                 //   fragmentTransaction.commit();
                   // break;

            }
        }








  //  } // cierra clas view holder



    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout =   layoutInflater.inflate(R.layout.drawer_item_layout,null);

        return new ViewHolder(itemLayout,viewType,context);


    }

    /**
     *This method is called by RecyclerView.Adapter to display the data at the specified position.
     *This method should update the contents of the itemView to reflect the item at the given position.
     *So here , if position!=0 it implies its a row_item and we set the title and icon of the view.
     */

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        if(position!=0){
            holder.navTitle.setText(titles[position - 1]);
            holder.navIcon.setImageResource(icons.getResourceId(position-1,-1));
        }

    }


    /**
     *It returns the total no. of items . We +1 count to include the header view.
     *So , it the total count is 5 , the method returns 6.
     *This 6 implies that there are 5 row_items and 1 header view with header at position zero.
     */

    @Override
    public int getItemCount() {

        return titles.length+1;
    }

    /**
     *This methods returns 0 if the position of the item is '0'.
     *If the position is zero its a header view and if its anything else
     *its a row_item with a title and icon.
     */

    @Override
    public int getItemViewType(int position) {
        if(position==0)return 0;
        else return 1;
    }


}