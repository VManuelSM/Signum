package com.example.manuel.signum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Models.Usuario;
import util.GSONSingleton;
import util.SharedPreferencesSingleton;
import util.VolleySingleton;

public class LogIn extends AppCompatActivity {
    EditText nickname;
    EditText password;
    ProgressDialog progressDialog;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        nickname = findViewById(R.id.txtUser);
        password = findViewById(R.id.txtPass);
    }

    public void logIn(View view) {
        progressDialog = new ProgressDialog(LogIn.this);
        String address = getString(R.string.ip) + getString(R.string.logInDir);
        progressDialog.setMessage(getString(R.string.progressLogIn));
        progressDialog.setTitle(getString(R.string.progressMessage));
        progressDialog.show();
        stringRequest = new StringRequest(Request.Method.POST, address, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.hide();
                    Log.i("Response", response);
                    JSONObject responseJSON = new JSONObject(response);
                    int warning = Integer.parseInt(responseJSON.getString("warning"));
                    if (warning != 1) {
                        Toast.makeText(getApplicationContext(), responseJSON.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        JSONArray responseArray = responseJSON.optJSONArray("usuario");
                        JSONObject userJSON = responseArray.getJSONObject(0);
                        Log.i("usuario json: ", userJSON.toString());
                        Usuario usuario = new Usuario(
                                Integer.parseInt(userJSON.getString("id_usuario")),
                                userJSON.getString("nickname"),
                                userJSON.getString("nombre"),
                                userJSON.getString("apellidoPaterno"),
                                userJSON.getString("apellidoMaterno"),
                                userJSON.getString("fotoPerfil"),
                                userJSON.getString("email"),
                                userJSON.getString("password")

                        );
                        SharedPreferencesSingleton.getInstance(getApplicationContext()).put(SharedPreferencesSingleton.Key.USUARIO, GSONSingleton.getInstance().objectToJSONString(usuario));
                        Intent intent = new Intent(LogIn.this, LearningHome.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    progressDialog.hide();
                    Log.i("Error de sesión", e.getMessage());
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), "No se ha podido conectar, error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Usuario usuario = new Usuario();
                usuario.setNickname(nickname.getText().toString());
                usuario.setPassword(password.getText().toString());
                Map<String, String> parametros = new HashMap<>();
                parametros.put("nickname", usuario.getNickname());
                parametros.put("password", usuario.getPassword());
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void changeToActivity(View view) {
        Intent intent = new Intent(LogIn.this, SignUp.class);
        startActivity(intent);
    }
}
