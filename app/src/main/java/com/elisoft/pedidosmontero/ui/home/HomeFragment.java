package com.elisoft.pedidosmontero.ui.home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.pedidosmontero.R;
import com.elisoft.pedidosmontero.Suceso;
import com.elisoft.pedidosmontero.cliente.CProducto;
import com.elisoft.pedidosmontero.cliente.Principal;
import com.elisoft.pedidosmontero.cliente.ProductoAdapter;
import com.elisoft.pedidosmontero.cliente.Registro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;


    ListView lista;
    ProgressDialog pDialog;
    ArrayList<CProducto> producto;

    RequestQueue queue;

    Suceso suceso;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        lista=root.findViewById(R.id.lv_producto);
        //obtener la lista
        servicio_lista_producto_volley();

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });



        return root;

    }

    public void servicio_lista_producto_volley(){
        try {

            pDialog = new ProgressDialog( getContext());
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setMessage("Obteniendo lista");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();




            JSONObject jsonParam= new JSONObject();
              String url=getString(R.string.servidor) + "frmProducto.php?opcion=lista_producto";
            if (queue == null) {
                queue = Volley.newRequestQueue(getContext());
                Log.e("volley","Setting a new request queue");
            }


            JsonObjectRequest myRequest= new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respuestaJSON) {
                            pDialog.cancel();
                            try {
                                producto= new ArrayList<CProducto>();
                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {


                                    JSONArray usu = respuestaJSON.getJSONArray("producto");

                                    for (int i = 0; i < usu.length(); i++) {

                                        CProducto hi = new CProducto(
                                                Integer.parseInt(usu.getJSONObject(i).getString("Id_producto")),
                                                usu.getJSONObject(i).getString("Nombre"),
                                                usu.getJSONObject(i).getString("Precio_unit"),
                                                usu.getJSONObject(i).getString("Direccion_imagen"),
                                                Integer.parseInt(usu.getJSONObject(i).getString("id_empresa"))
                                        );

                                        producto.add(hi);
                                    }
                                            ProductoAdapter adaptador = new ProductoAdapter(getContext(), producto);

                                            lista.setAdapter(adaptador);


                                }
                                else
                                {
                                    mensaje_error_final(suceso.getMensaje());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mensaje_error_final("Falla en tu conexión a Internet.");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.cancel();
                    mensaje_error_final("Falla en tu conexión a Internet.");
                }
            }
            ){
                public Map<String,String> getHeaders() throws AuthFailureError {
                    Map<String,String> parametros= new HashMap<>();
                    parametros.put("content-type","application/json; charset=utf-8");
                    parametros.put("Authorization","apikey 849442df8f0536d66de700a73ebca-us17");
                    parametros.put("Accept", "application/json");

                    return  parametros;
                }
            };


            // TIEMPO DE ESPERA
            myRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(myRequest);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void mensaje_error_final(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create();
        builder.show();
    }





}