package com.example.proyecto_ago_dic_2024;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class firstFragment extends Fragment {
    private ListView listView;
    private UserAdapter adapter;
    private ArrayList<User> userList;
    private RequestQueue requestQueue;
    public firstFragment() {}
    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        logoutButton = view.findViewById(R.id.logoutButton);
        listView = view.findViewById(R.id.listViewUser);
        userList = new ArrayList<>();
        adapter = new UserAdapter(getActivity(), userList);
        listView.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(getActivity());
        fetchUserData();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        return view;
    }

    private void fetchUserData() {
        String url = "https://david255311.pythonanywhere.com/user/1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("name");
                            User user = new User(name);

                            userList.add(user);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error al cargar los datos del usuario", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", error.toString());
            }
        });
        requestQueue.add(request);
    }

    private void logoutUser() {
        SessionManager sessionManager = new SessionManager(getContext());
        sessionManager.clearSession();  // Borra la sesión del usuario

        // Redirige al usuario a la pantalla de inicio de sesión
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}