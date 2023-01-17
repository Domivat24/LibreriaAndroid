package dam.curso2022.u2aev1.u6aev1listado;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorLibros extends RecyclerView.Adapter<AdaptadorLibros.ViewHolderLibros> implements View.OnClickListener {
    ArrayList<Libro> listLibros;
    private View.OnClickListener listener;

    public AdaptadorLibros(ArrayList<Libro> listLibros) {
        this.listLibros = listLibros;
    }

    @NonNull
    @Override
    public AdaptadorLibros.ViewHolderLibros onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null, false);
        view.setOnClickListener(this);
        return new ViewHolderLibros(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderLibros viewHolderLibros, int i) {
        viewHolderLibros.etTitulo.setText(listLibros.get(i).getTitulo());
        viewHolderLibros.portada.setImageResource(listLibros.get(i).getPortada());
    }

    @Override
    public int getItemCount() {
        return listLibros.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class ViewHolderLibros extends RecyclerView.ViewHolder {
        TextView etTitulo;
        ImageView portada;

        public ViewHolderLibros(@NonNull View itemView) {
            super(itemView);
            etTitulo = itemView.findViewById(R.id.idLibro);
            portada = itemView.findViewById(R.id.idPortada);


        }
    }
}
