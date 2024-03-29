package com.example.rigobobo.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rigobobo.DataManager.AppelloManager;
import com.example.rigobobo.DataManager.InfoManager;
import com.example.rigobobo.DataManager.PrenotazioneManager;
import com.example.rigobobo.DataManager.TassaManager;
import com.example.rigobobo.DataManager.VotoManager;
import com.example.rigobobo.Model.Info;
import com.example.rigobobo.Parser.Esse3Parser;
import com.example.rigobobo.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText username,password;
    Button login;
    AsyncTask<Void, Void, Info> runningTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        //Check se il login ha failato in un tentativo precedente
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        if(sharedPreferences.contains("loginFailed")){
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Errore")
                    .setMessage("C'è stato un errore durante il login. Assicurati di aver inserito le giuste credenziali di Esse3.")
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                        }
                    })
                    .show();
        }
        deleteSharedPreferences("login");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(username.getText().toString(), password.getText().toString());
            }
        });
    }

    private void login (final String username, final String password){
        ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                "Caricamento...", true);

        //Check dei dati di login
        Esse3Parser.getInstance().setCredentials(username, password);
        if (runningTask != null) runningTask.cancel(true);
        runningTask = new CheckLogin();
        runningTask.execute();
    }

    private final class CheckLogin extends AsyncTask<Void, Void, Info> {

        @Override
        protected Info doInBackground(Void... params) {
            Info info;
            try {
                info = InfoManager.getInstance().getInfoData(true);
            }
            catch (Exception e){
                return null;
            }
            if(info != null){
                AppelloManager.getInstance().getAppelliData(true);
                VotoManager.getInstance().getVotiData(true);
                PrenotazioneManager.getInstance().getPrenotazioniData(true);
                TassaManager.getInstance().getTasseData(true);
            }
            return info;
        }

        @Override
        protected void onPostExecute(Info info) {
            SharedPreferences sharedPreferences =  getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(info != null) {
                //Check passato
                editor.putString("username", Esse3Parser.getInstance().getUsername());
                editor.putString("password", Esse3Parser.getInstance().getPassword());
            }
            else {
                //Errore nel login
                editor.putBoolean("loginFailed", true);
            }
            editor.commit();

            finish();
            Context context = LoginActivity.this;
            Intent intent = new Intent(context,MainActivity.class);
            context.startActivity(intent);
        }

    }
}
