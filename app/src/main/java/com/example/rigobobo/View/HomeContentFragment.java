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
import com.example.rigobobo.Model.Info;
import com.example.rigobobo.Model.Tassa;
import com.example.rigobobo.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeContentFragment extends Fragment {

    AsyncTask<Void, Void, Info> runningTask;
    static View thisView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_home_content, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        if (runningTask != null) runningTask.cancel(true);
        runningTask = new LoadData();
        runningTask.execute();

        return recyclerView;
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
            thisView = itemView;

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


    private final class LoadData extends AsyncTask<Void, Void, Info> {

        @Override
        protected Info doInBackground(Void... params) {
            Info info;
            try {
                info = InfoManager.getInstance().getInfoData();
            }
            catch (Exception e){
                return null;
            }
            System.out.println(info);
            return info;
        }

        @Override
        protected void onPostExecute(Info info) {
            /*
            LinearLayout content = thisView.findViewById(R.id.content);
            View scrollList = getLayoutInflater().inflate(R.layout.item_scroll_list, null);
            content.addView(scrollList);
            LinearLayout list = thisView.findViewById(R.id.list);

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
            }*/
        }

    }


}
