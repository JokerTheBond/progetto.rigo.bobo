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

import com.example.rigobobo.DataManager.TassaManager;
import com.example.rigobobo.Model.Tassa;
import com.example.rigobobo.R;

import java.util.List;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class TassaActivity extends AppCompatActivity {

    AsyncTask<Void, Void, List<Tassa>> runningTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generic);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tasse universitarie");

        //TODO Qua potrei mostrare un loader (ma non è necessario se ho la cache)

        if (runningTask != null) runningTask.cancel(true);
        runningTask = new LoadData();
        runningTask.execute();
    }

    private final class LoadData extends AsyncTask<Void, Void, List<Tassa>> {

        @Override
        protected List<Tassa> doInBackground(Void... params) {
            List<Tassa> tasse;
            try {
                tasse = TassaManager.getInstance().getTasseData();
            }
            catch (Exception e){
                return null;
            }
            return tasse;
        }

        @Override
        protected void onPostExecute(List<Tassa> tasse) {
            LinearLayout content = findViewById(R.id.content);
            View scrollList = getLayoutInflater().inflate(R.layout.item_scroll_list, null);
            content.addView(scrollList);
            LinearLayout list = findViewById(R.id.list);

            if(tasse == null || tasse.size() == 0){
                //TODO Set empty view or show alert if fail
                View infoItem = getLayoutInflater().inflate(R.layout.item_info, null);
                TextView info = infoItem.findViewById(R.id.info);
                info.setText( "Non sono presenti nuove tasse da pagare" );
                list.addView(infoItem);
                return;
            }

            Button topButton = (Button) getLayoutInflater().inflate(R.layout.item_top_button, null);
            topButton.setText("Vai su Esse3 per pagare le tasse");
            content.addView(topButton, 1);

            View alertItem = getLayoutInflater().inflate(R.layout.item_alert, null);
            TextView info = alertItem.findViewById(R.id.info);
            info.setText( "Sono presenti nuove tasse da pagare" );
            list.addView(alertItem);

            for(Tassa it: tasse){
                View item = getLayoutInflater().inflate(R.layout.activity_tassa_item, null);
                TextView importo = item.findViewById(R.id.importo);
                TextView scadenza = item.findViewById(R.id.scadenza);
                importo.setText( Float.toString(it.getImporto()) );
                scadenza.setText( it.getScadenzaFormatted() );
                list.addView(item);
            }
        }

    }

    public void topButtonClick(View view){
        Intent browserInt = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.esse3.unimore.it/auth/studente/Tasse/ListaFatture.do"));
        startActivity(browserInt);
    }

}
