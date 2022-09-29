package com.arindo.ketagiahn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GantiPasswordActivity extends AppCompatActivity {

    EditText username, unitup, passwordlama, passwordbaru;
    Button gantipass, btnbatal;
    JSONObject jsonObject;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);

        username = findViewById(R.id.usernamechg);
        unitup = findViewById(R.id.unitupchg);
        passwordlama = findViewById(R.id.passlamachg);
        passwordbaru = findViewById(R.id.passbaruchg);
        gantipass = findViewById(R.id.Buttonchange);
        btnbatal = findViewById(R.id.btnbatal);

        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GantiPasswordActivity.this.finish();
            }
        });

        preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        gantipass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("")){
                    Toast.makeText(GantiPasswordActivity.this, "isi username terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (unitup.getText().toString().equals("")){
                    Toast.makeText(GantiPasswordActivity.this, "isi unitup terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (passwordlama.getText().toString().equals("")){
                    Toast.makeText(GantiPasswordActivity.this, "isi password lama terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (passwordbaru.getText().toString().equals("")){
                    Toast.makeText(GantiPasswordActivity.this, "isi password baru terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (passwordbaru.getText().toString().equals(preferences.getString("passpref", null))) {
                    Toast.makeText(GantiPasswordActivity.this, "password sudah pernah digunakan", Toast.LENGTH_LONG).show();
                } else {
                    getpasswordbaru();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
         startActivity(new Intent(GantiPasswordActivity.this, MainActivity.class));
         GantiPasswordActivity.this.finish();
    }

    private void getpasswordbaru(){
        String url_json = "http://bilman.arindo.net/index.php?r=bilman/cp";
        final RequestQueue requestQueue = Volley.newRequestQueue(GantiPasswordActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_json,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response kedua", response);
                        try {
                            jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("1")) {
                                pemberitahuan();
                            } else if (jsonObject.getString("code").equals("0")) {
                                Toast.makeText(GantiPasswordActivity.this, "Ada kesalahan, periksa kembali username, unitup dan password", Toast.LENGTH_LONG).show();
                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lostkoneksi();
                Log.e("pos trx Error", error.toString());
                requestQueue.stop();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username.getText().toString().trim());
                params.put("unitup", unitup.getText().toString().trim());
                params.put("passwordlama", passwordlama.getText().toString().trim());
                params.put("passwordbaru", passwordbaru.getText().toString().trim());
                // Log.e("Json", params.toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers =new HashMap<String, String>();
                headers.put("Authorization", "Basic YnVkaTpsaW1h");
                String credentials = "webapi"+":"+"Bi1lM3N@rind0";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void lostkoneksi() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Internet Terputus");
        alertDialogBuilder
                .setMessage("Sambungkan ke internet untuk ganti kata sandi")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void pemberitahuan() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Pemberitahuan");
        alertDialogBuilder
                .setMessage("Password berhasil diganti")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                        editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        GantiPasswordActivity.this.finish();
                        startActivity(new Intent(GantiPasswordActivity.this, LoginActivity.class));
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}