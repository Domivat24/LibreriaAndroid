package dam.curso2022.u2aev1.u6aev1listado;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorLibros extends RecyclerView.Adapter<AdaptadorLibros.ViewHolderLibros> {
    ArrayList<Libro> listLibros;
    private ItemClickListener mClickListener;


    public AdaptadorLibros(ArrayList<Libro> listLibros) {
        this.listLibros = listLibros;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public AdaptadorLibros.ViewHolderLibros onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
        return new ViewHolderLibros(view);
    }

    // binds the data to the TextView and ImageView in each row
    @Override
    public void onBindViewHolder(ViewHolderLibros viewHolderLibros, int i) {
        viewHolderLibros.etTitulo.setText(listLibros.get(i).getTitulo());
        viewHolderLibros.portada.setImageBitmap(listLibros.get(i).getPortada());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return listLibros.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolderLibros extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView etTitulo;
        ImageView portada;

        public ViewHolderLibros(@NonNull View itemView) {
            super(itemView);
            etTitulo = itemView.findViewById(R.id.idLibro);
            portada = itemView.findViewById(R.id.idPortada);

            //Listeners para el clic y el menu contextual
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), v.getId(), 0, R.string.ctx_anadir_favoritos);//groupId, itemId, order, title
            menu.add(this.getAdapterPosition(), v.getId(), 0, R.string.ctx_anadir_wishlist);//groupId, itemId, order, title

        }
    }
    //get Libro to esasily manage it
    Libro getItem(int id) {
        return listLibros.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
