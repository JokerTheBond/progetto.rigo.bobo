package com.example.rigobobo.View;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rigobobo.DataManager.NotificaManager;
import com.example.rigobobo.Model.Notifica;
import com.example.rigobobo.R;

import java.util.List;


public class NotificaContentFragment extends Fragment {

    List<Notifica> notifiche;

    public NotificaContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notifiche = NotificaManager.getInstance().getNotifiche();

        if(notifiche.size() == 0){
            View emptyView = inflater.inflate(R.layout.empty_view, container, false);
            TextView textView = emptyView.findViewById(R.id.empty_text);
            textView.setText("Non sono presenti notifiche");
            return emptyView;
        }

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_notifica_content, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView description;
        public TextView tipo;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_notifica_item_content, parent, false));
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_desc);
            tipo = (TextView) itemView.findViewById(R.id.list_tipo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    if(tipo.getText().equals(Notifica.TIPO_VOTO)){
                        Intent intent = new Intent(context, VotoActivity.class);
                        context.startActivity(intent);
                    }
                    else if(tipo.getText().equals(Notifica.TIPO_TASSA)){
                        Intent intent = new Intent(context, TassaActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private int LENGTH;

        public ContentAdapter(Context context) {
            LENGTH = notifiche.size();
            if(LENGTH > 16) LENGTH = 16;
            //LENGTH = 16;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(notifiche.get(position % notifiche.size()).getTitolo());
            holder.description.setText(notifiche.get(position % notifiche.size()).getDataOraStr());
            holder.tipo.setText(notifiche.get(position % notifiche.size()).getTipo());
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
