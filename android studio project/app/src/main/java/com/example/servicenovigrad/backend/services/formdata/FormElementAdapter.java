package com.example.servicenovigrad.backend.services.formdata;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.services.FormElement;
import com.example.servicenovigrad.backend.services.ServiceForm;
import com.example.servicenovigrad.ui.admin.AdminServiceManageActivity;
import com.example.servicenovigrad.ui.admin.EditFormActivity;

import java.util.List;

public class FormElementAdapter extends ArrayAdapter<FormElement> {
    private final EditFormActivity context;
    private final List<FormElement> elements;
    public FormElementAdapter(EditFormActivity context, List<FormElement> elements) {
        super(context, R.layout.admin_service_list_layout, elements);
        this.context = context;
        this.elements = elements;
    }

    // Creates the items of the ListView
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();


        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.admin_service_list_layout, null, true);



        return listViewItem;
    }
}
