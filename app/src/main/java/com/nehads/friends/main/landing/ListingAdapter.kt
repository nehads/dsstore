package com.nehads.friends.main.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nehads.friends.R
import com.nehads.friends.model.remote.SeasonsItem
import com.nehads.friends.util.Constants
import com.nehads.friends.util.DiffUtilsExtension
import com.nehads.friends.util.load
import kotlinx.android.synthetic.main.item_season.view.*

class ListingAdapter :
    RecyclerView.Adapter<ListingAdapter.ViewHolder>(), DiffUtilsExtension {

    var list = listOf<SeasonsItem>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_season, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(list: List<SeasonsItem>) {
        autoNotify(this.list, list) { o, n -> o.id == n.id }
        this.list = list
        //notifyDataSetChanged()
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bindData(
            item: SeasonsItem
        ) {
            with(view) {
                tv_name.text = item.name
                iv_image.load(
                    "${Constants.IMAGE_BASE_URL}${item.posterPath}"
                )
                iv_image.clipToOutline = true

                btn_item.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("season_no", item.seasonNumber)
                    it.findNavController().navigate(R.id.toEpisodesFragment, bundle)
                    // onClickListener(item.seasonNumber)
                }
            }
        }
    }
}