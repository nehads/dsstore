package com.nehads.friends.main.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nehads.friends.R
import com.nehads.friends.model.remote.EpisodesItem
import com.nehads.friends.util.DiffUtilsExtension
import com.nehads.friends.util.getPath
import com.nehads.friends.util.load
import kotlinx.android.synthetic.main.item_episode.view.*

class EpisodeListingAdapter :
    RecyclerView.Adapter<EpisodeListingAdapter.ViewHolder>(), DiffUtilsExtension {

    var list = listOf<EpisodesItem>()
    var filterConst: Int = 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position], filterConst)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(list: List<EpisodesItem>, filterConst: Int) {
        autoNotify(this.list, list) { o, n -> o.id == n.id }
        this.list = list
        this.filterConst = filterConst
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bindData(
            item: EpisodesItem,
            filterConst: Int
        ) {
            with(view) {
                tv_name.text = item.name
                tv_number.text = resources.getString(R.string.episode_no, item.episodeNumber)
                iv_card.load(item.stillPath.getPath())
                iv_card.clipToOutline = true

                /* If not sorted by any of the options:
                * show textField empty
                * if selected then show value of selected sort type */
                when (filterConst) {
                    0 -> {
                        tv_filer_val.text = ""
                    }
                    1 -> {
                        tv_filer_val.text = item.voteCount.toString()
                    }
                    2 -> {
                        tv_filer_val.text = item.voteAverage.toString()
                    }
                }
            }
        }
    }
}