package com.elisoft.pedidosmontero.cliente;

import android.content.SharedPreferences;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.elisoft.pedidosmontero.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class Principal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    // private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //clase inicializar busqueda
        //initSearhWidgets();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.registro,R.id.iniciar_sesion,R.id.perfil_conductor)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setBackgroundColor(getResources().getColor(R.color.amarillo2));


       // SharedPreferences perfil=getSharedPreferences("perfil",MODE_PRIVATE);
       // TextView et_nombre=findViewById(R.id.et_nombre);
       // et_nombre.setText(perfil.getString("nombre",""));


    }
    // Metodo para busqueda de datos
/*
    private void initSearhWidgets(){
        SearchView searchView = SearchView findViewById(R.id.lista_busqueda);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<Shape> filteredShapes = new ArrayList<Shape>();

                for (Shape shape: mInflaterListItems){
                    if (shape.mInfra().toLowerCase().contains(s.toLowerCase())){
                        filteredShapes.add(shape);
                    }
                }
                ProductoAdapter adapter = new ProductoAdapter(getApplicationContext(),0,filteredShapes);
                listView.setAdapter(adapter);

                return false;
            }
        });
    }  */



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();


    }
}