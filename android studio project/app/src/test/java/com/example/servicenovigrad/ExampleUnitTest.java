package com.example.lab6sql;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.account.Account;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.PrintStream;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void add_account() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String username = "test";
        String root = "users/" + username + "/";
        DatabaseReference acctRef = database.getReference(root);
        Account acct = new Account(username, "nom", "prenom", "Client", "password");
        acctRef.setValue(acct); //regarder dans la base de donnés si le nouveau compte a été ajouté
        assertEquals("nom", DatabaseHandler.user.getNom());
        assertEquals("prenom", DatabaseHandler.user.getPrenom());
        assertEquals("Client", DatabaseHandler.user.getRole());
        assertEquals("password", DatabaseHandler.user.getPassword());
    }

    @Test
    public void delete_Account() {
        ;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String username = "test";
        String root = "users/" + username + "/";
        DatabaseReference acctRef = database.getReference(root);
        Account acct = new Account(username, "nom", "prenom", "Client", "password");
        DatabaseHandler.deleteUser(acct); // regarder dans la base de donné si le nouveau compte a été enlevé (suppose que account_database_test() fonctionne)
    }

    @Test
    public long stress_test() {
        int number_of_accounts=10;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String username = "test";
        String root = "users/" + username + "/";
        DatabaseReference acctRef = database.getReference(root);
        Long start = System.currentTimeMillis();
        for (int i = 0; i < number_of_accounts; i++) {
            Account acct = new Account(Integer.toString(i), Integer.toString(i), Integer.toString(i), "Client", "password");
            acctRef.setValue(acct);
        }
        for (int i = 0; i < number_of_accounts; i++) {
            Account acct = new Account(Integer.toString(i), Integer.toString(i), Integer.toString(i), "Client", "password");
            DatabaseHandler.deleteUser(acct);
        }
        Long end=System.currentTimeMillis();
        return end-start;
    }
}