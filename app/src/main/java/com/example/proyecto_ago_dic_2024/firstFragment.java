package com.example.proyecto_ago_dic_2024;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private ArrayList<RequestModel> userList;
    private RequestQueue requestQueue;
    private Button logoutButton;


    public firstFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        logoutButton = view.findViewById(R.id.logoutButton);
        listView = view.findViewById(R.id.listViewUser);
        userList = new ArrayList<>();
        adapter = new UserAdapter(getActivity(), userList);
        listView.setAdapter(adapter);










        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RequestModel selectedItem = userList.get(position);
                NavController navController = NavHostFragment.findNavController(getParentFragment());
                Bundle bundle = new Bundle();
                bundle.putInt("idPet", selectedItem.getPetId());
                bundle.putString("message", selectedItem.getMessage());
                bundle.putString("pet_name", selectedItem.getPet_name());

                navController.navigate(R.id.requestDetailFragment, bundle);
            }
        });







        requestQueue = Volley.newRequestQueue(getActivity());
        fetchRequestsData();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });
        return view;
    }

    private void fetchRequestsData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_doc", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("user_id", "Valor por defecto");
        String url = "https://david255311.pythonanywhere.com/get_requests_by_requester/" + idUser;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtiene la lista de solicitudes del JSON
                            JSONArray requestsArray = response.getJSONArray("requests");
                            for (int i = 0; i < requestsArray.length(); i++) {
                                JSONObject requestObject = requestsArray.getJSONObject(i);

                                // Extrae los campos relevantes de cada solicitud
                                String createdAt = requestObject.getString("created_at");
                                int idRequest = requestObject.getInt("idRequest");
                                String message = requestObject.getString("message");
                                int petId = requestObject.getInt("pet_id");
                                int requesterId = requestObject.getInt("requester_id");
                                String status = requestObject.getString("status");
                                String name = requestObject.getString("pet_name");
                                String image1 = requestObject.getString("image1");

                                // Crea un objeto Request con estos datos
                                RequestModel requestModel = new RequestModel(createdAt, idRequest, message, petId, requesterId, status, name, image1);

                                // Añade la solicitud a la lista
                                userList.add(requestModel);
                            }

                            // Notifica al adaptador que los datos han cambiado
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error al cargar los datos del servidor", Toast.LENGTH_SHORT).show();
                        Log.e("API_ERROR", error.toString());
                    }
                });

        requestQueue.add(request);
    }


    private void logoutUser() {
        SessionManager sessionManager = new SessionManager(getContext());
        sessionManager.clearSession();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}