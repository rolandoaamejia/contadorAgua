package com.example.contadoragua

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Registro

class AdapterCustom(var context: Context, var items: ArrayList<Registro>, var longClickListener: LongClickListener): RecyclerView.Adapter<AdapterCustom.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.molde_lista, parent, false)

        return ViewHolder(view, longClickListener)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Mapeo
        holder.hora.text = "${item.hora }\n${item.fecha}"
        holder.ml.text = "${item.ml}ml"

    }

    // Mapear las variables de cada item dentro de items con los
    // widgets correspondientes dentro de la vista
    class ViewHolder(var view: View, var longClickListener: LongClickListener): RecyclerView.ViewHolder(view), View.OnLongClickListener {
        override fun onLongClick(v: View?): Boolean {
            longClickListener.LongClickListener(view, adapterPosition)

            return true
        }


        var hora: TextView
        var ml: TextView


        init {
            this.hora = view.findViewById(R.id.textView_Hora)
            this.ml = view.findViewById(R.id.textView_ML)


            view.setOnLongClickListener(this)
        }
    }

}