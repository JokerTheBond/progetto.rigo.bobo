package com.example.rigobobo.View;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rigobobo.DataManager.PrenotazioneManager;
import com.example.rigobobo.Model.Prenotazione;
import com.example.rigobobo.R;

import java.util.List;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class PrenotazioneActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    PrenotazioneActivity instance;
    AsyncTask<Void, Void, List<Prenotazione>> runningTask;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.activity_generic);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("I miei appelli");

        swipeRefreshLayout = findViewById(R.id.content);
        swipeRefreshLayout.setOnRefreshListener(instance);
        ScrollView scrollList = (ScrollView) getLayoutInflater().inflate(R.layout.item_scroll_list, null);
        swipeRefreshLayout.addView(scrollList);

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

    private final class LoadData extends AsyncTask<Void, Void, List<Prenotazione>> {

        private Boolean forceUpdate;

        public LoadData(Boolean forceUpdate){
            this.forceUpdate = forceUpdate;
        }

        @Override
        protected List<Prenotazione> doInBackground(Void... params) {
            List<Prenotazione> prenotazioni;
            try {
                prenotazioni = PrenotazioneManager.getInstance().getPrenotazioniData(forceUpdate);
            }
            catch (Exception e){
                return null;
            }
            return prenotazioni;
        }

        @Override
        protected void onPostExecute(List<Prenotazione> prenotazioni) {
            swipeRefreshLayout.setRefreshing(false); //Set this off if the page has reloaded
            LinearLayout list = findViewById(R.id.list);
            list.removeAllViews();

            if(prenotazioni == null || prenotazioni.size() == 0){
                //TODO Set empty view or show alert if fail
                View alertItem = getLayoutInflater().inflate(R.layout.item_alert, null);
                TextView info = alertItem.findViewById(R.id.info);
                info.setText( "Non è stato prenotato alcun appello" );
                list.addView(alertItem);
                return;
            }

            for(Prenotazione it: prenotazioni){
                View item = getLayoutInflater().inflate(R.layout.activity_prenotazione_item, null);
                TextView nome = item.findViewById(R.id.nome);
                TextView data = item.findViewById(R.id.data);
                TextView desc = item.findViewById(R.id.desc);
                LinearLayout descContainer = item.findViewById(R.id.descContainer);
                TextView edificio = item.findViewById(R.id.edificio);
                LinearLayout edificioContainer = item.findViewById(R.id.edificioContainer);
                TextView aula = item.findViewById(R.id.aula);
                LinearLayout aulaContainer = item.findViewById(R.id.aulaContainer);
                nome.setText( it.getNome() );
                data.setText( it.getDataOraFormatted() );
                if(it.getDesc() != null) desc.setText( it.getDesc() );
                else descContainer.setVisibility(View.GONE);
                if(it.getEdificio() != null) edificio.setText( it.getEdificio() );
                else edificioContainer.setVisibility(View.GONE);
                if(it.getAula() != null) aula.setText( it.getAula() );
                else aulaContainer.setVisibility(View.GONE);

                list.addView(item);
            }
        }

    }

}
