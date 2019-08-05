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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Models.Usuario;
import util.VolleySingleton;

public class LogIn extends AppCompatActivity {
    EditText nickname;
    EditText password;
    Button btnLogIn;
    ProgressDialog progressDialog;
    StringRequest stringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        nickname = findViewById(R.id.txtUser);
        password = findViewById(R.id.txtPass);
    }
    public void logIn(View view){
        progressDialog = new ProgressDialog(LogIn.this);
        String address=getString(R.string.ip)+getString(R.string.logInDir);
        Log.i("address",address);
        progressDialog.setMessage(getString(R.string.progressLogIn));
        progressDialog.setTitle(getString(R.string.progressLogInMessage));
        progressDialog.show();
        stringRequest = new StringRequest(Request.Method.POST, address, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    progressDialog.hide();
                    Log.i("Response", response);
                    JSONObject jsonObject = new JSONObject(response);
                    int warning = Integer.parseInt(jsonObject.getString("warning"));
                    if (warning != 1){
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(LogIn.this, LearningHome.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                progressDialog.hide();
                Log.i("Error de sesión", e.getMessage());
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(),"No se ha podido conectar, error: "+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Usuario usuario = new Usuario();
                usuario.setNickname(nickname.getText().toString());
                usuario.setPassword(password.getText().toString());
                Map<String,String> parametros=new HashMap<>();
                parametros.put("nickname",usuario.getNickname());
                parametros.put("password",usuario.getPassword());
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    public void changeToActivity(View view){
        Intent intent = new Intent(LogIn.this, SignUp.class);
        startActivity(intent);
    }
}
