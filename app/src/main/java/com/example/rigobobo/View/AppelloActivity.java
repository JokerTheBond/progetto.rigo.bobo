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

import com.example.rigobobo.DataManager.AppelloManager;
import com.example.rigobobo.Model.Appello;
import com.example.rigobobo.R;

import java.util.List;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class AppelloActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    AppelloActivity instance;
    AsyncTask<Void, Void, List<Appello>> runningTask;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.activity_generic);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Prossimi appelli");

        swipeRefreshLayout = findViewById(R.id.content);
        swipeRefreshLayout.setOnRefreshListener(instance);
        ScrollView scrollList = (ScrollView) getLayoutInflater().inflate(R.layout.item_scroll_list, null);
        swipeRefreshLayout.addView(scrollList);

        LinearLayout parentLayout = findViewById(R.id.parent);
        Button topButton = (Button) getLayoutInflater().inflate(R.layout.item_top_button, null);
        topButton.setText("Vai su Esse3 per prenotare appelli");
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

    private final class LoadData extends AsyncTask<Void, Void, List<Appello>> {

        private Boolean forceUpdate;

        public LoadData(Boolean forceUpdate){
            this.forceUpdate = forceUpdate;
        }

        @Override
        protected List<Appello> doInBackground(Void... params) {
            List<Appello> appelli;
            try {
                AppelloManager.getInstance().getAppelliData(forceUpdate);
                appelli = AppelloManager.getInstance().getAppelliData(AppelloManager.APPELLO_ORDER_BY_AZ);
            }
            catch (Exception e){
                return null;
            }
            return appelli;
        }

        @Override
        protected void onPostExecute(List<Appello> appelli) {
            swipeRefreshLayout.setRefreshing(false); //Set this off if the page has reloaded
            LinearLayout list = findViewById(R.id.list);
            list.removeAllViews();

            if(appelli == null || appelli.size() == 0){
                //TODO Set empty view or show alert if fail
                View alertItem = getLayoutInflater().inflate(R.layout.item_alert, null);
                TextView info = alertItem.findViewById(R.id.info);
                info.setText( "Non sono stati trovati appelli" );
                list.addView(alertItem);
                return;
            }

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
