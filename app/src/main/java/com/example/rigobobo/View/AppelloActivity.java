package com.example.rigobobo.View;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.rigobobo.DataManager.AppelloManager;
import com.example.rigobobo.Model.Appello;
import com.example.rigobobo.R;

import java.util.List;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class AppelloActivity extends AppCompatActivity {

    AsyncTask<Void, Void, List<Appello>> runningTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generic);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Prossimi appelli");

        //TODO Qua potrei mostrare un loader (ma non è necessario se ho la cache)

        if (runningTask != null) runningTask.cancel(true);
        runningTask = new LoadData();
        runningTask.execute();
    }

    private final class LoadData extends AsyncTask<Void, Void, List<Appello>> {

        @Override
        protected List<Appello> doInBackground(Void... params) {
            List<Appello> appelli;
            try {
                appelli = AppelloManager.getInstance().getAppelliData(AppelloManager.APPELLO_ORDER_BY_AZ);
            }
            catch (Exception e){
                return null;
            }
            return appelli;
        }

        @Override
        protected void onPostExecute(List<Appello> appelli) {
            LinearLayout content = findViewById(R.id.content);
            View scrollList = getLayoutInflater().inflate(R.layout.item_scroll_list, null);
            content.addView(scrollList);
            LinearLayout list = findViewById(R.id.list);

            if(appelli == null || appelli.size() == 0){
                //TODO Set empty view or show alert if fail
                View alertItem = getLayoutInflater().inflate(R.layout.item_alert, null);
                TextView info = alertItem.findViewById(R.id.info);
                info.setText( "Non sono stati trovati appelli" );
                list.addView(alertItem);
                return;
            }

            Button topButton = (Button) getLayoutInflater().inflate(R.layout.item_top_button, null);
            topButton.setText("Vai su Esse3 per prenotare appelli");
            content.addView(topButton, 1);

            for(Appello it: appelli){
                View item = getLayoutInflater().inflate(R.layout.activity_appello_item, null);
                TextView nome = item.findViewById(R.id.nome);
                TextView data = item.findViewById(R.id.data);
                TextView desc = item.findViewById(R.id.desc);
                nome.setText( it.getNome() );
                data.setText( it.getDataFormatted() );
                desc.setText( it.getDesc() );
                list.addView(item);
            }
        }

    }

    public void topButtonClick(View view){
        Intent browserInt = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.esse3.unimore.it/auth/studente/Appelli/AppelliF.do"));
        startActivity(browserInt);
    }

}
