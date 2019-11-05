package com.example.rigobobo.View;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rigobobo.DataManager.VotoManager;
import com.example.rigobobo.Model.Voto;
import com.example.rigobobo.R;

import java.util.List;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class VotoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    VotoActivity instance;
    AsyncTask<Void, Void, List<Voto>> runningTask;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.activity_generic);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("I miei voti");

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

    private final class LoadData extends AsyncTask<Void, Void, List<Voto>> {

        private Boolean forceUpdate;

        public LoadData(Boolean forceUpdate){
            this.forceUpdate = forceUpdate;
        }

        @Override
        protected List<Voto> doInBackground(Void... params) {
            List<Voto> voti;
            try {
                VotoManager.getInstance().getVotiData(forceUpdate);
                voti = VotoManager.getInstance().getVotiData(VotoManager.VOTO_ORDER_BY_NEWEST);
            }
            catch (Exception e){
                return null;
            }
            return voti;
        }

        @Override
        protected void onPostExecute(List<Voto> voti) {
            swipeRefreshLayout.setRefreshing(false); //Set this off if the page has reloaded
            LinearLayout list = findViewById(R.id.list);
            list.removeAllViews();

            if(voti == null || voti.size() == 0){
                View alertItem = getLayoutInflater().inflate(R.layout.item_alert, null);
                TextView info = alertItem.findViewById(R.id.info);
                info.setText( "Non hai ancora nessun voto" );
                list.addView(alertItem);
                return;
            }

            for(Voto it: voti){
                View item = getLayoutInflater().inflate(R.layout.activity_voto_item, null);
                TextView nome = item.findViewById(R.id.nome);
                TextView voto = item.findViewById(R.id.voto);
                TextView data = item.findViewById(R.id.data);
                TextView crediti = item.findViewById(R.id.crediti);
                nome.setText(it.getNome());
                voto.setText(it.getVoto());
                if(it.getVotoI() > 27) voto.setTextColor( getResources().getColor(R.color.color2830) );
                else if(it.getVotoI() > 23) voto.setTextColor( getResources().getColor(R.color.color2427) );
                else if(it.getVotoI() > 20) voto.setTextColor( getResources().getColor(R.color.color2123) );
                else voto.setTextColor( getResources().getColor(R.color.color1820) );
                data.setText( it.getDataFormatted() );
                crediti.setText(Integer.toString(it.getCrediti()));
                list.addView(item);
            }
        }
    }
}
