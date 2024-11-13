package com.example.proyecto_ago_dic_2024;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistryActivity extends AppCompatActivity {
    private EditText nameInput, lastName1Input, lastName2Input,emailInput, passwordInput, profileImage, userAge, state, city, socialNetwork, description;
    private Button registerButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        nameInput = findViewById(R.id.nameInput);
        lastName1Input = findViewById(R.id.lastName1Input);
        lastName2Input = findViewById(R.id.lastName2Input);
        profileImage = findViewById(R.id.profileImage);
        userAge = findViewById(R.id.userAge);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        socialNetwork = findViewById(R.id.socialNetwork);
        description = findViewById(R.id.description);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.pass);
        registerButton = findViewById(R.id.registerButton);
        requestQueue = Volley.newRequestQueue(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String lastName1 = lastName1Input.getText().toString().trim();
        String lastName2 = lastName2Input.getText().toString().trim();
        String profileImg = profileImage.getText().toString().trim();
        String age = userAge.getText().toString().trim();
        String State = state.getText().toString().trim();
        String City = city.getText().toString().trim();
        String SocialNetwork = socialNetwork.getText().toString().trim();
        String Description = description.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (name.isEmpty() || lastName1.isEmpty()|| lastName2.isEmpty() || profileImg.isEmpty() || age.isEmpty()||State.isEmpty()||City.isEmpty()||SocialNetwork.isEmpty()||Description.isEmpty()|| email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://david255311.pythonanywhere.com/register";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("lastName1", lastName1);
            jsonBody.put("lastName2", lastName2);
            jsonBody.put("profileImage", profileImg );
            jsonBody.put("userAge", age );
            jsonBody.put("state", State );
            jsonBody.put("city", City);
            jsonBody.put("socialNetwork", SocialNetwork );
            jsonBody.put("description", Description );
            jsonBody.put("email", email);
            jsonBody.put("pass", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            finish();  // Cierra la actividad de registro
                        } else {
                            Toast.makeText(this, "Error en el registro: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error en la solicitud", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}
