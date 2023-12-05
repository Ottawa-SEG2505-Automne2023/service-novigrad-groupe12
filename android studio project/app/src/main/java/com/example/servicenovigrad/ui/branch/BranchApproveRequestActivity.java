package com.example.servicenovigrad.ui.branch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.account.BranchAccount;
import com.example.servicenovigrad.backend.services.FilledForm;

import java.util.List;

public class BranchApproveRequestActivity extends AppCompatActivity {
    private static class FilledRequestAdapter extends ArrayAdapter<String> {
        private final AppCompatActivity context;
        private final List<String> lines;
        public FilledRequestAdapter(@NonNull AppCompatActivity context, List<String> lines) {
            super(context, R.layout.simpletext, lines);
            this.context = context;
            this.lines = lines;
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.simpletext, null, true);

            TextView text = listViewItem.findViewById(R.id.simpletext);
            text.setText(lines.get(position));

            return listViewItem;
        }
    }

    private BranchAccount user;
    private FilledForm req;
    private int reqID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_approve_request);

        user = (BranchAccount) DatabaseHandler.user;
        // Request # stored in intExtra requestNumber
        reqID = getIntent().getIntExtra("requestNumber", -1);
        req = user.getRequests().get(reqID);

        ListView formViewer = findViewById(R.id.formViewer);
        formViewer.setAdapter(new FilledRequestAdapter(this, req.getTextSequence()));

        Button yes = findViewById(R.id.yesBtn);
        yes.setOnClickListener(v -> removeRequest(true));
        Button no = findViewById(R.id.noBtn);
        no.setOnClickListener(v -> removeRequest(false));
    }

    private void removeRequest(boolean approved) {
        user.getRequests().remove(reqID);
        DatabaseHandler.getDatabase().getReference("users/" + user.getUsername() + "/requests/" + reqID).removeValue();
        req.respond(approved);
        finish();
    }
}