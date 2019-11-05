package com.example.rigobobo.View;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rigobobo.DataManager.TassaManager;
import com.example.rigobobo.Model.Tassa;
import com.example.rigobobo.R;

import java.util.List;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class TassaActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    TassaActivity instance;
    AsyncTask<Void, Void, List<Tassa>> runningTask;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.activity_generic);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tasse universitarie");

        swipeRefreshLayout = findViewById(R.id.content);
        swipeRefreshLayout.setOnRefreshListener(instance);
        ScrollView scrollList = (ScrollView) getLayoutInflater().inflate(R.layout.item_scroll_list, null);
        swipeRefreshLayout.addView(scrollList);

        LinearLayout parentLayout = findViewById(R.id.parent);
        Button topButton = (Button) getLayoutInflater().inflate(R.layout.item_top_button, null);
        topButton.setText("Vai su Esse3 per pagare le tasse");
        parentLayout.addView(topButton, 1);

        if (runningTask != null) runningTask.cancel(true);
        runningTask = new LoadData(false);
        runningTask.execute();
    }

    @Override
    public void onRefresh() {
        if (runningTask != null) runningTask.cancel(true);
        runningTask = new LoadData(true);
        runningTask.execute();
    }

    private final class LoadData extends AsyncTask<Void, Void, List<Tassa>> {

        private Boolean forceUpdate;

        public LoadData(Boolean forceUpdate){
            this.forceUpdate = forceUpdate;
        }

        @Override
        protected List<Tassa> doInBackground(Void... params) {
            List<Tassa> tasse;
            try {
                tasse = TassaManager.getInstance().getTasseData(forceUpdate);
            }
            catch (Exception e){
                return null;
            }
            return tasse;
        }

        @Override
        protected void onPostExecute(List<Tassa> tasse) {
            swipeRefreshLayout.setRefreshing(false); //Set this off if the page has reloaded
            LinearLayout list = findViewById(R.id.list);
            list.removeAllViews();

            if(tasse == null || tasse.size() == 0){
                View infoItem = getLayoutInflater().inflate(R.layout.item_info, null);
                TextView info = infoItem.findViewById(R.id.info);
                info.setText( "Non sono presenti nuove tasse da pagare" );
                list.addView(infoItem);
                return;
            }

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
