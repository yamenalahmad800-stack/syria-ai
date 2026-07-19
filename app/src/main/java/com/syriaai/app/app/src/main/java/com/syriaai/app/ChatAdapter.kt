package com.syriaai.app

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: MutableList<Message>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtMessage: TextView = view.findViewById(R.id.txt_message)
        val container: View = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.txtMessage.text = message.text

        val root = holder.container as android.widget.LinearLayout
        root.gravity = if (message.isUser) Gravity.END else Gravity.START

        holder.txtMessage.setBackgroundResource(
            if (message.isUser) R.drawable.bg_bubble_user else R.drawable.bg_bubble_ai
        )
        holder.txtMessage.setTextColor(
            if (message.isUser)
                holder.itemView.context.getColor(R.color.white)
            else
                holder.itemView.context.getColor(R.color.text_dark)
        )
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}
