package com.example.hw01_15puzzle

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_view.view.*

class DataRecyclerViewAdapter(val context: Context, private val repo: PlayerRepository) :
    RecyclerView.Adapter<DataRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var dataSet: List<Leaderboard>

    private fun refreshData() {
        dataSet = repo.getRepoData()
    }

    init {
        refreshData()
    }

    private val layoutInflater = LayoutInflater.from(context)

    // create the row views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = layoutInflater.inflate(R.layout.row_view, parent, false)
        return ViewHolder(rowView)
    }

    // put data on row view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val leaderboard = dataSet[position]

        val textViewName = holder.itemView.textViewName
        textViewName.text = leaderboard.playerName

        val textViewMoves = holder.itemView.textViewMoves
        textViewMoves.text = (leaderboard.playerMoves.toString())

        val textViewTime = holder.itemView.textViewTime
        textViewTime.text = (leaderboard.playerTime.toString())

        val imageButtonDelete = holder.itemView.imageButtonDelete
        imageButtonDelete.setOnClickListener {
            Log.d("DELETE", dataSet[position].id.toString())
            repo.delete(dataSet[position].id)
            Log.d("DELETE", "deleted" + dataSet[position].id.toString())
            refreshData()
            this.notifyDataSetChanged()
        }

    }

    // how many rows we have
    override fun getItemCount(): Int {
        return dataSet.count()
    }
}