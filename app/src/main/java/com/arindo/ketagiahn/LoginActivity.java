package com.arindo.ketagiahn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button ButtonLogin;
    EditText edtusername, edtunitup, edtpassword;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    JSONObject playerObject;
    JSONArray playerArray;
    JSONObject obj;
    TextView contoh;
    JSONObject jsonObject;
    private ProgressBar progressBar;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtusername = (EditText) findViewById(R.id.edtusername);
        edtunitup = (EditText) findViewById(R.id.edtunitup);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
        contoh = findViewById(R.id.contoh);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

       edtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
       preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        String username = preferences.getString("userpref", "");
        if (!username.equals("")){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }

        editor = preferences.edit();

        ButtonLogin = findViewById(R.id.ButtonLogin);

        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtusername.getText().toString().equals("")) {
               Toast.makeText(LoginActivity.this, "isi username terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (edtunitup.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "isi unitup terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (edtpassword.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "isi password terlebih dahulu", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    Logindata();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
     finish();
    }

    private void Logindata(){
        String url_json = "https://bilman.arindo.net/index.php?r=bilman/login";
        final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_json,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                             jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("1")) {
                                obj = new JSONObject(response);
                                playerArray = obj.getJSONArray("data");
                                for (int i = 0; i < playerArray.length(); i++) {
                                    playerObject = playerArray.getJSONObject(i);
                                   String username = playerObject.getString("username");
                                   String unitup = playerObject.getString("unitup");
                                   String koked = playerObject.getString("koked");
                                   long login = System.currentTimeMillis();
                                   editor.putString("userpref", username);
                                   editor.putString("unitpref", unitup);
                                   editor.putString("kokedpref", response);
                                   editor.putString("passpref", edtpassword.getText().toString().trim());
                                   editor.putLong("waktu", login);
                                   editor.apply();
                                   Log.e("Stringkoked", response);
                                }
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                LoginActivity.this.finish();
                               // Toast.makeText(LoginActivity.this, "Login Berhasil ", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                } else if (jsonObject.getString("code").equals("0")) {
                                String respon = jsonObject.getString("message");
                                    Toast.makeText(LoginActivity.this, respon +"\nPeriksa kembali user dan password anda", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                }
                            } catch(JSONException e) {
                                e.printStackTrace();
                            }
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(LoginActivity.this,
                            "Oops. request ke server gagal coba lagi",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    lostkoneksi();
                }
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG);
                Log.e("pos trx Error", error.toString());
                progressBar.setVisibility(View.GONE);
                requestQueue.stop();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", edtusername.getText().toString().trim());
                params.put("unitup", edtunitup.getText().toString().trim());
                params.put("password", edtpassword.getText().toString().trim());
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void lostkoneksi() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Coba Lagi");
        alertDialogBuilder
                .setMessage("Periksa Internet anda dan coba lagi")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
