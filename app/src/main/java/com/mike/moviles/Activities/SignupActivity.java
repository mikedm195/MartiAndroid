package com.mike.moviles.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.mike.moviles.Activities.Admin.newUser;
import com.mike.moviles.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mike.moviles.Services.Services.SIGNUP_API;

public class SignupActivity extends AppCompatActivity {

    Button loginBtn, signupBtn;
    EditText firstnameTxt, lastnameTxt, emailTxt, passwordOneTxt, passwordTwoText, addressTxt, genderText, heightText, weightText;
    ProgressDialog progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        View view = this.getWindow().getDecorView();
        view.setBackgroundResource(R.color.green);

        signupBtn = (Button)findViewById(R.id.signupButton);
        firstnameTxt = (EditText)findViewById(R.id.firstNameTextField);
        lastnameTxt = (EditText)findViewById(R.id.lastNameTextField);
        addressTxt = (EditText)findViewById(R.id.addressTextField);
        emailTxt = (EditText)findViewById(R.id.emailTextFieldSignup);
        passwordOneTxt = (EditText)findViewById(R.id.passwordOneTextField);
        passwordTwoText = (EditText)findViewById(R.id.passwordTwoTextField);
        genderText = (EditText)findViewById(R.id.genderTextField);
        heightText = (EditText)findViewById(R.id.heightTextField);
        weightText = (EditText)findViewById(R.id.weightTextField);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent actividad = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(actividad);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordOneTxt.getText().toString().equals(passwordTwoText.getText().toString()))
                {
                    signup();
                } else {
                    Toast.makeText(SignupActivity.this, R.string.passwordsNotMatchText, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signup()
    {
        final ProgressDialog progress_bar = new ProgressDialog(SignupActivity.this);
        progress_bar.setMessage(SignupActivity.this.getString(R.string.signingupText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        StringRequest signupReq = new StringRequest(Request.Method.POST, SIGNUP_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if(res.getString("user_id").equals("-1"))
                            {
                                Toast.makeText(SignupActivity.this, R.string.queryErrorText , Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(SignupActivity.this, R.string.signedupText , Toast.LENGTH_SHORT).show();
                                Intent actividad = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(actividad);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(SignupActivity.this, "Error! " + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar.cancel();
                        Toast.makeText(SignupActivity.this, R.string.commsErrorText + " " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("first_name",firstnameTxt.getText().toString());
                params.put("last_name",lastnameTxt.getText().toString());
                params.put("email",emailTxt.getText().toString());
                params.put("password",passwordOneTxt.getText().toString());
                params.put("address",addressTxt.getText().toString());
                params.put("gender",genderText.getText().toString());
                params.put("weight",weightText.getText().toString());
                params.put("height",heightText.getText().toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(signupReq);
    }
}