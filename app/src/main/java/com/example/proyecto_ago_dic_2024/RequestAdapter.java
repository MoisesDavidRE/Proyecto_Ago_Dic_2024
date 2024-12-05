package com.example.proyecto_ago_dic_2024;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private List<RequestSolicitudes> requestList;
    private Context context;

    public RequestAdapter(List<RequestSolicitudes> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
    }

    public void updateRequestList(List<RequestSolicitudes> newRequests) {
        requestList.clear();
        requestList.addAll(newRequests);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RequestSolicitudes request = requestList.get(position);
        holder.nameTextView.setText(request.getName());
        holder.messageTextView.setText(request.getMessage());
        holder.statusTextView.setText(request.getStatus());
        holder.btnCancel.setOnClickListener(v -> updateRequestStatus(request.getIdRequest(), "rejected", position));
        holder.btnConfirm.setOnClickListener(v -> updateRequestStatus(request.getIdRequest(), "accepted", position));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView messageTextView;
        public TextView statusTextView;
        public Button btnCancel;
        public Button btnConfirm;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            messageTextView = itemView.findViewById(R.id.textViewMessage);
            statusTextView = itemView.findViewById(R.id.textViewStatus);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
        }
    }

    private void updateRequestStatus(String requestId, String newStatus, int position) {
        String url = "https://david255311.pythonanywhere.com/update_request_status/" + requestId + "/" + newStatus;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    requestList.get(position).setStatus(newStatus);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Estado actualizado a " + newStatus, Toast.LENGTH_SHORT).show();
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(context, "Error al actualizar el estado", Toast.LENGTH_SHORT).show();
                });
        requestQueue.add(stringRequest);
    }
}
