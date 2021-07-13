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
import com.elisoft.pedidosmontero.Suceso;
import com.elisoft.pedidosmontero.cliente.Principal;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.elisoft.pedidosmontero.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registro_conductor extends AppCompatActivity {

    EditText et_nombre_conductor;
    EditText et_paterno_conductor;
    EditText et_materno_conductor;
    EditText et_celular_conductor;
    EditText et_correo_conductor;
    EditText et_licencia_conductor;
    EditText et_token_conductor;
    EditText et_user_conductor;
    EditText et_pass_conductor;
    Button bt_registrar_conductor;

    ProgressDialog pDialog;
    RequestQueue queue;
    Suceso suceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_conductor);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et_nombre_conductor = findViewById(R.id.et_nombre_conductor);
        et_paterno_conductor = findViewById(R.id.et_paterno_conductor);
        et_materno_conductor = findViewById(R.id.et_materno_conductor);
        et_celular_conductor = findViewById(R.id.et_celular_conductor);
        et_correo_conductor = findViewById(R.id.et_correo_conductor);
        et_licencia_conductor = findViewById(R.id.et_licencia_conductor);
        et_token_conductor = findViewById(R.id.et_token_conductor);
        et_user_conductor = findViewById(R.id.et_user_conductor);
        et_pass_conductor = findViewById(R.id.et_pass_conductor);
        bt_registrar_conductor = findViewById(R.id.bt_registrar_conductor);

        bt_registrar_conductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicio_registro_volley();
            }
        });


    }


    // Configuracion voley

    public void servicio_registro_volley(){
        try {
            pDialog = new ProgressDialog( Registro_conductor.this);
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setMessage("Registrando");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();




            JSONObject jsonParam= new JSONObject();
            jsonParam.put("nombre", et_nombre_conductor.getText().toString());
            jsonParam.put("paterno", et_paterno_conductor.getText().toString());
            jsonParam.put("materno", et_materno_conductor.getText().toString());
            jsonParam.put("celular", et_celular_conductor.getText().toString());
            jsonParam.put("correo", et_correo_conductor.getText().toString());
            jsonParam.put("licencia", et_licencia_conductor.getText().toString());
            jsonParam.put("token", et_token_conductor.getText().toString());
            jsonParam.put("user", et_user_conductor.getText().toString());
            jsonParam.put("pass", et_pass_conductor.getText().toString());

            String url=getString(R.string.servidor) + "frmConductor.php?opcion=insertar_conductor";

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
                            pDialog.cancel();
                            try {

                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {

                                    String id = respuestaJSON.getString("id");
                                    String nombre = respuestaJSON.getString("nombre");
                                    String paterno = respuestaJSON.getString("paterno");
                                    String materno = respuestaJSON.getString("materno");
                                    String celular = respuestaJSON.getString("celular");
                                    String correo = respuestaJSON.getString("correo");
                                    String licencia = respuestaJSON.getString("licencia");
                                    String token = respuestaJSON.getString("token");
                                    String user = respuestaJSON.getString("user");
                                    String pass = respuestaJSON.getString("pass");

                                    guardar_datos(id,nombre,paterno,materno,celular,correo,licencia,token,user,pass);

                                    startActivity(new Intent(Registro_conductor.this, Principal.class));

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
    private void guardar_datos(String id, String nombre, String paterno, String materno, String celular, String correo, String licencia, String token, String user, String pass) {
        SharedPreferences perfil=getSharedPreferences("perfil",MODE_PRIVATE);
        SharedPreferences.Editor editar= perfil.edit();
        editar.putString("id",id);
        editar.putString("nombre",nombre);
        editar.putString("paterno",paterno);
        editar.putString("materno",materno);
        editar.putString("celular",celular);
        editar.putString("correo",correo);
        editar.putString("licencia",licencia);
        editar.putString("token",token);
        editar.putString("user",user);
        editar.putString("pass",pass);

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