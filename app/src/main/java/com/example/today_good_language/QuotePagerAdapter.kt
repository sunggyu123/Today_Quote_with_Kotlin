package com.example.today_good_language

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotePagerAdapter(
    private val quotes: List<Quote>,
    private val isNameRevealed : Boolean
    ) : RecyclerView.Adapter<QuotePagerAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quote,parent,false)
        )


    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote_index = position % quotes.size
        holder.bind(quotes[quote_index],isNameRevealed)
    }

    override fun getItemCount() = Int.MAX_VALUE

    class QuoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val quoteTextView:TextView = itemView.findViewById(R.id.quoteTextView)

        private val nameTextView:TextView = itemView.findViewById(R.id.nameTextView)

        @SuppressLint("SetTextI18n")
        fun bind(quote: Quote, isNameRevealed: Boolean){
            quoteTextView.text = "\"${quote.quote}\""

            if (isNameRevealed){
                nameTextView.text =" - ${quote.name}"
                nameTextView.visibility = View.VISIBLE
            } else{
                nameTextView.visibility = View.GONE
            }
        }
    }
}