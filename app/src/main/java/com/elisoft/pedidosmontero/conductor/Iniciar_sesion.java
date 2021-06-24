package com.elisoft.pedidosmontero.conductor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.pedidosmontero.cliente.Principal;
import com.elisoft.pedidosmontero.R;
import com.elisoft.pedidosmontero.cliente.Registro;
import com.elisoft.pedidosmontero.Suceso;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Iniciar_sesion extends AppCompatActivity {
    //variable de alerta de dialogo
    ProgressDialog pDialog;
    //consulta a una direccion url
    RequestQueue queue;

    //clase de resultado.
    Suceso suceso;

    EditText et_user;
    EditText et_password;

    Button bt_iniciar_sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et_user=findViewById(R.id.et_user);
        et_password=findViewById(R.id.et_password);


        Button bt_iniciar_sesion=findViewById(R.id.bt_iniciar_sesion);
        TextView txview =findViewById(R.id.tv_registro);

        bt_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               servicio_iniciar_sesion_volley(et_user.getText().toString().trim(),
                       et_password.getText().toString().trim(),
                       "token");
            }
        });


        txview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(Iniciar_sesion.this, Registro.class));
            }
        });

    }


    public void servicio_iniciar_sesion_volley(String user,String password,String token){
        try {
            //progressDialog visible
            pDialog = new ProgressDialog( Iniciar_sesion.this);
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setMessage("Iniciando sesion");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();



// preparamos los parametros en JSON
            JSONObject jsonParam= new JSONObject();
            jsonParam.put("user", user);
            jsonParam.put("password", password);
            jsonParam.put("token", token);

            //direccion url
            String url=getString(R.string.servidor) + "frmUsuario.php?opcion=insertar_usuario";

            if (queue == null) {
                queue = Volley.newRequestQueue(this);
                Log.e("volley","Setting a new request queue");
            }


            JsonObjectRequest myRequest= new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respuestaJSON) {
                          //obtenemos los resultados
                          //ocultar el progressDialogo
                            pDialog.cancel();

                            try {

                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {

                                    String id = respuestaJSON.getString("id_conductor");
                                    String nombre = respuestaJSON.getString("Nombres");
                                    String paterno = respuestaJSON.getString("Ap_paterno");
                                    String materno = respuestaJSON.getString("Ap_materno");
                                    String celular = respuestaJSON.getString("Num_telefono");

                                    guardar_datos(id,nombre,paterno,materno,celular,user,token);

                                    startActivity(new Intent(Iniciar_sesion.this, Principal.class));

                                }
                                else
                                {
                                    mensaje_error_final("Error: Al conectar con el Servidor.\nVerifique su acceso a Internet.");
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

    private void guardar_datos(String id, String nombre, String paterno, String materno, String celular, String user, String token) {
        SharedPreferences perfil=getSharedPreferences("perfil",MODE_PRIVATE);
        SharedPreferences.Editor editar= perfil.edit();
        editar.putString("id",id);
        editar.putString("nombre",nombre);
        editar.putString("paterno",paterno);
        editar.putString("materno",materno);
        editar.putString("celular",celular);
        editar.putString("user",user);
        editar.putString("token",token);
        editar.putString("tipo","conductor");
        editar.commit();
    }


    public void mensaje_error_final(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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