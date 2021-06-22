package com.ildus.myrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class PackRecyclerAdapter(private val packs: MutableList<Pack>) :
    RecyclerView.Adapter<PackRecyclerAdapter.PackHolder>() {

    inner class PackHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {

        private val textViewStatus: TextView = itemView.findViewById(R.id.textViewStatus)
        private val textViewCode: TextView = itemView.findViewById(R.id.textViewCode)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        var pack: Pack? = null

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        fun bind(position: Int) {

            pack = packs[position]

            textViewCode.text = pack?.code

            pack?.let {
                when {
                    it.isSurplus -> {
                        textViewStatus.text = "isSurplus"
                        imageView.setImageResource(android.R.drawable.presence_busy)
                    }
                    it.isFound -> {
                        textViewStatus.text = "isFound"
                        imageView.setImageResource(android.R.drawable.presence_online)
                    }
                    else -> {
                        imageView.setImageResource(android.R.drawable.presence_invisible)
                        textViewStatus.text = "not"
                    }
                }
            }

        }

        override fun onClick(v: View?) {
//            Log.d(TAG, "onClick " + packs[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {

            val pack = packs[adapterPosition]
            Toast.makeText(
                v?.context,
                v?.context?.getString(R.string.deleted) + packs[adapterPosition].code,
                Toast.LENGTH_SHORT
            ).show()
            packs.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)

            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
        return PackHolder(itemView)
    }

    override fun onBindViewHolder(holder: PackHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = packs.size
}