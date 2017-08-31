package com.mrubel.msplayer;

/**
 * Created by mosharrofrubel on 8/31/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class CustomAdapter extends BaseAdapter{
    String [] result;
    Context context;
    Bitmap[] imageId;
    private static LayoutInflater inflater=null;
    public CustomAdapter(MainActivity mainActivity, String[] prgmNameList, Bitmap[] prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_view_lay, null);
        holder.tv=(TextView) rowView.findViewById(R.id.solo_song);
        holder.img=(ImageView) rowView.findViewById(R.id.albumart);
        holder.tv.setText(result[position]);

        try {
            if (imageId[position] == null) {
                holder.img.setImageResource(R.drawable.albumart);
            } else {
                holder.img.setImageBitmap(imageId[position]);
            }
        } catch (ArrayIndexOutOfBoundsException e){
            holder.img.setImageResource(R.drawable.albumart);
            System.out.println(this.getClass().getName()+" :"+e);
        }

        return rowView;
    }

}