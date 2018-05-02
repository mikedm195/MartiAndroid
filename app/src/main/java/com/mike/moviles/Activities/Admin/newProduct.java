package com.mike.moviles.Activities.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mike.moviles.Activities.LoginActivity;
import com.mike.moviles.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mike.moviles.Services.Services.PRODUCTS_API;
import static com.mike.moviles.Services.Services.SIGNUP_API;

public class newProduct extends AppCompatActivity {

    Button loginBtn, signupBtn;
    EditText nameTxt, idTxt, photoTxt, videoTxt, categoryTxt, colorTxt, priceText, ageText;
    ProgressDialog progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        View view = this.getWindow().getDecorView();
        view.setBackgroundResource(R.color.green);


        signupBtn = (Button)findViewById(R.id.signupButton);
        nameTxt = (EditText)findViewById(R.id.NameTextField);
        idTxt = (EditText)findViewById(R.id.IDTextField);
        photoTxt = (EditText)findViewById(R.id.PhotoTextField);
        videoTxt = (EditText)findViewById(R.id.VideoTextField);
        categoryTxt = (EditText)findViewById(R.id.CategoryTextField);
        colorTxt = (EditText)findViewById(R.id.ColoroTextField);
        priceText = (EditText)findViewById(R.id.PriceTextField);
        ageText = (EditText)findViewById(R.id.AgeTextField);

    }

    private void signup()
    {
        final ProgressDialog progress_bar = new ProgressDialog(newProduct.this);
        progress_bar.setMessage(newProduct.this.getString(R.string.uploadingProductText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        StringRequest signupReq = new StringRequest(Request.Method.POST, PRODUCTS_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if(res.getString("product_id").equals("-1"))
                            {
                                Toast.makeText(newProduct.this, R.string.queryErrorText , Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(newProduct.this, R.string.uploadedProductText , Toast.LENGTH_SHORT).show();
                                Intent actividad = new Intent(newProduct.this, LoginActivity.class);
                                startActivity(actividad);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(newProduct.this, "Error! " + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar.cancel();
                        Toast.makeText(newProduct.this, R.string.commsErrorText + " " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name",nameTxt.getText().toString());
                params.put("product_id",idTxt.getText().toString());
                params.put("photo",photoTxt.getText().toString());
                params.put("video",videoTxt.getText().toString());
                params.put("category_id",categoryTxt.getText().toString());
                params.put("color",colorTxt.getText().toString());
                params.put("price",priceText.getText().toString());
                params.put("age",ageText.getText().toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(signupReq);
    }
}