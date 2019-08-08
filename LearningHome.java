package com.example.manuel.signum;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import Models.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;
import util.GSONSingleton;
import util.SharedPreferencesSingleton;
import util.VolleySingleton;

public class LearningHome extends AppCompatActivity {
    Usuario usuario;
    TextView txtNom;
    TextView txtApePat;
    CircleImageView fotoPerfil;
    ImageRequest imageRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_home);
        usuario = (Usuario) GSONSingleton.getInstance().JSONStringToObject(
                SharedPreferencesSingleton.getInstance(getApplicationContext()).getString(SharedPreferencesSingleton.Key.USUARIO),
                GSONSingleton.OriginClass.USUARIO_CLASS
        );
        fotoPerfil = findViewById(R.id.fotoPer);
        txtNom = findViewById(R.id.txtNom);
        txtApePat = findViewById(R.id.txtApePat);
        loadUserInterfaceData();
    }

    private void loadUserInterfaceData() {
        loadImageFromServer();
        txtNom.setText(usuario.getNombre());
        txtApePat.setText(usuario.getApellidoPaterno());
    }

    private void loadImageFromServer() {
        String address = getString(R.string.ip) + getString(R.string.loadImgDir) + usuario.getFotoPerfil();
        imageRequest = new ImageRequest(address, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                fotoPerfil.setImageBitmap(response);
            }
        }, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        fotoPerfil.setImageResource(R.mipmap.fondobtnperfil);
                    }
                });
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(imageRequest);
    }
}
