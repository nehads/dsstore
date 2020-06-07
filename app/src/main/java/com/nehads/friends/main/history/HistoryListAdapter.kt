package com.nehads.friends.main.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nehads.friends.R
import com.nehads.friends.model.DTO.HistoryModel
import com.nehads.friends.util.DiffUtilsExtension
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryListAdapter(private val clickListener: (HistoryModel) -> Unit) :
    RecyclerView.Adapter<HistoryListAdapter.ViewHolder>(), DiffUtilsExtension {

    var list = listOf<HistoryModel>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position], clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(list: List<HistoryModel>) {
        autoNotify(this.list, list) { o, n -> o.id == n.id }
        this.list = list
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bindData(
            historyModel: HistoryModel,
            clickListener: (HistoryModel) -> Unit
        ) {
            with(view) {
                tv_name.text = historyModel.date
                tv_description.text =
                    String.format("%s, %s", historyModel.season, historyModel.episode)
                tv_name.setOnClickListener {
                    clickListener(historyModel)
                }
            }
        }
    }
}
