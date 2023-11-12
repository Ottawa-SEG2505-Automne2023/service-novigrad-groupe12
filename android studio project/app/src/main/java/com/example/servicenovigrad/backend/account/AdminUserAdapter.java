package com.example.servicenovigrad.backend.account;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.ui.admin.AdminAccountManageActivity;

import java.util.List;

public class AdminUserAdapter extends ArrayAdapter<Account> {

    private final AdminAccountManageActivity context;
    private final List<Account> users;
    public AdminUserAdapter(AdminAccountManageActivity context, List<Account> users) {
        super(context, R.layout.admin_user_list_layout, users);
        this.context = context;
        this.users = users;
    }

    // Creates the items of the ListView
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.admin_user_list_layout, null, true);

        TextView nameInfo = listViewItem.findViewById(R.id.accountOwnerText);
        TextView usernameInfo = listViewItem.findViewById(R.id.accountUsernameText);
        TextView roleInfo = listViewItem.findViewById(R.id.accountRoleText);
        Button deleteBtn = listViewItem.findViewById(R.id.deleteAccountButton);

        // Set everything up
        Account account = users.get(position);
        String nameText = account.getNom() + ", " + account.getPrenom();
        String usernameText = "(" + account.getUsername() + ")";
        nameInfo.setText(nameText);
        usernameInfo.setText(usernameText);
        roleInfo.setText(account.getRole());
        deleteBtn.setOnClickListener(v -> context.showAccountDeletionConfirmation(account));

        return listViewItem;
    }
}
