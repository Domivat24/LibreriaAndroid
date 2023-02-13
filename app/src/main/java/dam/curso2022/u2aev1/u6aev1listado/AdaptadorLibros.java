package dam.curso2022.u2aev1.u6aev1listado;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorLibros extends RecyclerView.Adapter<AdaptadorLibros.ViewHolderLibros> {
    ArrayList<Libro> listLibros;
    private OnItemClickListener onItemClickListener;
    private OnCreateContextMenu onCreateContextMenu;
    private OnContextMenuItemClickListener onContextMenuItemClickListener;


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
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(view, getAdapterPosition());
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (onCreateContextMenu != null) {
                onCreateContextMenu.onCreateContextMenu(menu, v, menuInfo, getAdapterPosition(), onMenuItemClickListener);
            }
        }

        //context menu item click listener
        private MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (onContextMenuItemClickListener != null) {
                    onContextMenuItemClickListener.onContextMenuItemClick(item, getAdapterPosition());
                }
                return true;
            }
        };
    }

    //get Libro to esasily manage it
    Libro getItem(int id) {
        return listLibros.get(id);
    }

    // allows clicks events to be caught

/*
     ===========================><============================
     * Custom interface for handle event from other class file
     ===========================><============================
     */

    //Interface for onItemClickListener;
    //Interface for onItemClickListener;
    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //Interface for onCreateContextMenu
    interface OnCreateContextMenu {
        void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo, int position, MenuItem.OnMenuItemClickListener menuItemClickListener);
    }

    public void setOnCreateContextMenu(OnCreateContextMenu onCreateContextMenu) {
        this.onCreateContextMenu = onCreateContextMenu;
    }

    //Interface for onContextMenuItemClickListener
    interface OnContextMenuItemClickListener {
        void onContextMenuItemClick(MenuItem menuItem, int position);
    }

    public void setOnContextMenuItemClickListener(OnContextMenuItemClickListener onContextMenuItemClickListener) {
        this.onContextMenuItemClickListener = onContextMenuItemClickListener;
    }
}
