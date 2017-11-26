package com.aroliant.app.loginandregisterscreenusingvolley;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

public class RegisterActivity extends AppCompatActivity {

    EditText fname, lname, password, confirmPassword, email;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fname = (EditText) findViewById(R.id.firstName);
        lname = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);

        register = (Button) findViewById(R.id.registerBtn);

        // AwesomeValidation Library for Form Validation

        final AwesomeValidation mAwesomeValidation = new AwesomeValidation(UNDERLABEL);
        mAwesomeValidation.setContext(this);

        mAwesomeValidation.addValidation(this, R.id.firstName, "[a-zA-Z]+", R.string
                .err_name);
        mAwesomeValidation.addValidation(this, R.id.lastName, "[a-zA-Z]+", R.string
                .err_name);
        mAwesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R
                .string.err_email);
        mAwesomeValidation.addValidation(this, R.id.password, RegexTemplate.NOT_EMPTY, R.string.err_password);
        mAwesomeValidation.addValidation(this, R.id.confirmPassword, RegexTemplate.NOT_EMPTY, R.string.err_conPass);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {

                    if (mAwesomeValidation.validate()) {

                        //Add API Url

                        StringRequest postRequest = new StringRequest(Request.Method.POST, "API URL",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {

                                            //JSON RESPONSE FROM SERVER

                                            JSONObject res = new JSONObject(response);
                                            String responseData = res.getString("success");
                                            if (responseData.equals("true")) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT);
                                                toast.show();

                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT);
                                                toast.show();
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("firstname", fname.getText().toString().trim());
                                params.put("lastname", lname.getText().toString().trim());
                                params.put("email", email.getText().toString().trim());
                                params.put("password", password.getText().toString().trim());

                                return params;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        queue.add(postRequest);
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Password and Confirm Password does not match", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }

        });

    }
}
