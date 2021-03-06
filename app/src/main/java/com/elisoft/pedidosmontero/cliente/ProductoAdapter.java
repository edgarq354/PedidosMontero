package com.elisoft.pedidosmontero.cliente;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elisoft.pedidosmontero.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


//This is a custom adapter. It has been extended from BaseAdapter because
//we need to overrider the getView function for changing the layout of each Grid View Item
public class ProductoAdapter extends BaseAdapter {

    private ArrayList<CProducto> mArray = new ArrayList<>();
    private LayoutInflater mInflaterListItems;
    Context context;

    public ProductoAdapter(Context context, ArrayList<CProducto> array) {
        mArray = array;
        this.context=context;
        mInflaterListItems = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //This function will determine how many items to be displayed
    @Override
    public int getCount() {
        return mArray.size();
    }

    @Override
    public Object getItem(int position) {
        return mArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //This function will iterate through each object in the Data Set. This function will form each item in a Grid View
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CProducto  ped = mArray.get(position);
        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = mInflaterListItems.inflate(R.layout.item_producto,
                    null);
            holder.tv_nombre= convertView.findViewById(R.id.tv_nombre);
            holder.tv_precio= convertView.findViewById(R.id.tv_precio);
            holder.im_imagen=convertView.findViewById(R.id.im_imagen);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //Change the content here
        if (mArray.get(position) != null) {
            holder.tv_nombre.setText(ped.getNombre());
            holder.tv_precio.setText(ped.getPrecio_unit()+" Bs.");
            try {
                if(ped.getDireccion_imagen().length()>4) {

                    String url = context.getString(R.string.servidor)+"imagen/" + ped.getDireccion_imagen();
                    try {
                        Picasso.get().load(url).placeholder(R.drawable.producto).into(holder.im_imagen);
                    }catch (Exception ee)
                    {
                        ee.printStackTrace();
                    }

                }else{

                    holder.im_imagen.setImageBitmap(null);
                }

            } catch (Exception e) {

                e.printStackTrace();

            }
        }

        return convertView;
    }

    //View Holder class used for reusing the same inflated view. It will decrease the inflation overhead @getView
    private static class ViewHolder {
        TextView tv_nombre,tv_precio;
        ImageView im_imagen;

    }

}
