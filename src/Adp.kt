package com.example.day2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adp(private val dataItems: List<M>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val USER_VIEW = 101
        private const val AI_VIEW = 102
    }

    class UserViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val userTxtView: TextView = root.findViewById(R.id.tvMessage)
    }

    class AiViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val aiTxtView: TextView = root.findViewById(R.id.tvMessage)
    }

    override fun getItemViewType(idx: Int): Int {
        return if (dataItems[idx].fromUser) USER_VIEW else AI_VIEW
    }

    override fun onCreateViewHolder(parentGroup: ViewGroup, kind: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parentGroup.context)
        return if (kind == USER_VIEW) {
            val userLayout = layoutInflater.inflate(R.layout.item_u, parentGroup, false)
            UserViewHolder(userLayout)
        } else {
            val aiLayout = layoutInflater.inflate(R.layout.item_a, parentGroup, false)
            AiViewHolder(aiLayout)
        }
    }

    override fun getItemCount(): Int = dataItems.size

    override fun onBindViewHolder(holderInstance: RecyclerView.ViewHolder, pos: Int) {
        val currentMsg = dataItems[pos]
        if (holderInstance is UserViewHolder) {
            holderInstance.userTxtView.text = currentMsg.messageText
        } else if (holderInstance is AiViewHolder) {
            holderInstance.aiTxtView.text = currentMsg.messageText
        }
    }
}