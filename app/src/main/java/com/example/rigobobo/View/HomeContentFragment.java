package com.example.rigobobo.View;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rigobobo.DataManager.InfoManager;
import com.example.rigobobo.DataManager.TassaManager;
import com.example.rigobobo.DataManager.VotoManager;
import com.example.rigobobo.Model.Info;
import com.example.rigobobo.Model.Tassa;
import com.example.rigobobo.R;

import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;


public class HomeContentFragment extends Fragment {

    AsyncTask<Void, Void, HomeData> runningTask;
    RecyclerView recyclerView;
    LinearLayout thisView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisView = (LinearLayout) inflater.inflate(
                R.layout.fragment_home_content, container, false);
        recyclerView = thisView.findViewById(R.id.my_recycler_view);

        if (runningTask != null) runningTask.cancel(true);
        runningTask = new LoadData();
        runningTask.execute();

        return thisView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView color;
        public TextView name;
        public ImageView img;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_home_item_content, parent, false));

            name = (TextView) itemView.findViewById(R.id.tile_title);
            color = (ImageView) itemView.findViewById(R.id.tile_color);
            img = (ImageView) itemView.findViewById(R.id.tile_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Class<?> t;
                    if(getAdapterPosition() == 0) t = AppelloActivity.class;
                    else if(getAdapterPosition() == 1) t = VotoActivity.class;
                    else if(getAdapterPosition() == 2) t = PrenotazioneActivity.class;
                    else t = TassaActivity.class;
                    Intent intent = new Intent(context, t);
                    //intent.putExtra(VotoActivity.EXTRA_POSITION, getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 4;

        private final String[] mNames;
        private final int[] mColors;
        private final Drawable[] mImgs;

        public ContentAdapter(Context context) {
            Resources resources = context.getResources();

            mNames = resources.getStringArray(R.array.home_tiles_name);
            mColors = resources.getIntArray(R.array.home_tiles_color);
            TypedArray a = resources.obtainTypedArray(R.array.home_tiles_img);
            mImgs = new Drawable[a.length()];
            for (int i=0; i<mImgs.length; i++){
                mImgs[i] = a.getDrawable(i);
            }
            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(mNames[position % mNames.length]);
            holder.color.setBackgroundColor(mColors[position % mColors.length]);
            holder.img.setImageDrawable(mImgs[position % mImgs.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }


    //Async data loading

    private static class HomeData {
        Info info;
        float media;
        int crediti;
        int tasseCount;
        //Drawable fotoProfilo;
    }

    private final class LoadData extends AsyncTask<Void, Void, HomeData> {

        @Override
        protected HomeData doInBackground(Void... params) {
            HomeData data = new HomeData();
            try {
                data.info = InfoManager.getInstance().getInfoData();
                data.media = VotoManager.getInstance().getMediaPesata();
                data.crediti = VotoManager.getInstance().getCreditiConseguiti();
                data.tasseCount = TassaManager.getInstance().getTasseData().size();
            }
            catch (Exception e){
                return null;
            }
            /*try {
                Authenticator.setDefault (new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("242433", "D6YLRDSZ".toCharArray());
                    }
                });
                HttpURLConnection myURLConnection = (HttpURLConnection) new URL(data.info.getFotoProfilo()).openConnection();
                myURLConnection.setRequestProperty("Authorization", "MjQyNDMzOkQ2WUxSRFNa");
                InputStream is = (InputStream) myURLConnection.getContent();
                data.fotoProfilo = Drawable.createFromStream(is, "src name");
            }
            catch (Exception e){
                e.printStackTrace();
            }*/
            return data;
        }

        @Override
        protected void onPostExecute(HomeData data) {
            //Load layout
            ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);

            int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
            recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

            if(data == null) return;

            View homeInfoItem = getLayoutInflater().inflate(R.layout.fragment_home_info_item, null);
            TextView nome = homeInfoItem.findViewById(R.id.nome);
            nome.setText( "Bentornato " + data.info.getNome() );
            TextView corso_anno = homeInfoItem.findViewById(R.id.corso_anno);
            corso_anno.setText( "Sei iscritto al " + data.info.getAnnoStr() + " di " + data.info.getCorsoDiStudio() );
            TextView media_crediti = homeInfoItem.findViewById(R.id.media_crediti);
            DecimalFormat decimalFormat = new DecimalFormat("#.0");
            media_crediti.setText( "La tua media è " + decimalFormat.format(data.media) + " e in totale hai accumulato " + data.crediti + " crediti");
            if(data.tasseCount != 0) {
                TextView tasse = homeInfoItem.findViewById(R.id.tasse);
                tasse.setText("ATTENZIONE: hai nuove tasse da pagare");
                tasse.setVisibility(View.VISIBLE);
            }

            //ImageView fotoProfilo = (ImageView) homeInfoItem.findViewById(R.id.foto_profilo);
            //fotoProfilo.setImageDrawable(data.fotoProfilo);

            thisView.addView(homeInfoItem, 0);

            return;
        }

    }


}
