package com.elisoft.pedidosmontero;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    EditText et_nombre;
    EditText et_paterno;
    EditText et_materno;
    EditText et_celular;
    EditText et_correo;
    EditText et_token;
    Button bt_registrar;


    ProgressDialog pDialog;

    RequestQueue queue;
    Suceso suceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et_nombre=findViewById(R.id.et_nombre);
        et_paterno=findViewById(R.id.et_paterno);
        et_materno=findViewById(R.id.et_materno);
        et_celular=findViewById(R.id.et_celular);
        et_correo=findViewById(R.id.et_correo);
        et_token=findViewById(R.id.et_token);
        bt_registrar=findViewById(R.id.bt_registrar);




        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicio_registro_volley();
            }
        });
    }



    public void servicio_registro_volley(){
        try {
            pDialog = new ProgressDialog( Registro.this);
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setMessage("Registrando");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();




            JSONObject jsonParam= new JSONObject();
            jsonParam.put("nombre", et_nombre.getText().toString());
            jsonParam.put("paterno", et_paterno.getText().toString());
            jsonParam.put("materno", et_materno.getText().toString());
            jsonParam.put("celular", et_celular.getText().toString());
            jsonParam.put("email", et_correo.getText().toString());
            jsonParam.put("token", et_token.getText().toString());
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
                            pDialog.cancel();
                            try {

                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {

                                    String id = respuestaJSON.getString("id");
                                    String nombre = respuestaJSON.getString("nombre");
                                    String paterno = respuestaJSON.getString("paterno");
                                    String materno = respuestaJSON.getString("materno");
                                    String celular = respuestaJSON.getString("celular");
                                    String email = respuestaJSON.getString("email");
                                    String token = respuestaJSON.getString("token");

                                    guardar_datos(id,nombre,paterno,materno,celular,email,token);

                                    startActivity(new Intent(Registro.this,Principal.class));
//siempre q pase algo con tu proyecto vas a darle click en SYNC PROJECT
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

    private void guardar_datos(String id, String nombre, String paterno, String materno, String celular, String email, String token) {
        SharedPreferences perfil=getSharedPreferences("perfil",MODE_PRIVATE);
       SharedPreferences.Editor editar= perfil.edit();
       editar.putString("id",id);
       editar.putString("nombre",nombre);
       editar.putString("paterno",paterno);
       editar.putString("materno",materno);
       editar.putString("celular",celular);
       editar.putString("email",email);
       editar.putString("token",token);
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