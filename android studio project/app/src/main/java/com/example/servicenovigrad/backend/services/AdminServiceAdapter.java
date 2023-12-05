package com.example.servicenovigrad.backend.services;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.ui.admin.AdminServiceManageActivity;

import java.util.List;

public class AdminServiceAdapter extends ArrayAdapter<ServiceForm> {
    private final AdminServiceManageActivity context;
    private final List<ServiceForm> services;
    public AdminServiceAdapter(AdminServiceManageActivity context, List<ServiceForm> services) {
        super(context, R.layout.admin_service_list_layout, services);
        this.context = context;
        this.services = services;
    }

    // Creates the items of the ListView
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.admin_service_list_layout, null, true);

        // Get the service
        ServiceForm service = services.get(position);
        TextView nameText = listViewItem.findViewById(R.id.formName);
        Button editButton = listViewItem.findViewById(R.id.editFormButton);

        // Set its name and allow you to edit it
        nameText.setText(service.getName());
        editButton.setOnClickListener(v -> service.open(context, "edit", null));

        return listViewItem;
    }
}
