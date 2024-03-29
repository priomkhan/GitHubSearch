package com.priomkhan.githubsearch.ui.details

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.R
import com.priomkhan.githubsearch.data.UserRepo

class DetailedRepoRecyclerAdapter(val context: Context,
                                  var repos: List<UserRepo>,
                                  val itemListener: RepoItemListener): RecyclerView.Adapter<DetailedRepoRecyclerAdapter.ViewHolder>(){


    val repos_full = ArrayList<UserRepo>()
    var repos_filtered = ArrayList<UserRepo>()


    init {
        repos_full.addAll(repos)
        repos_filtered.addAll(repos)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        /*
        One of the jobs of the ViewHolder is to contain references to its child views.
        So, I'll add three declarations.

        I'll create each variable get it's value by calling the expression, itemView.findViewById.
        */

        val repoImage = itemView.findViewById<ImageView>(R.id.repoImage)

        val repoName = itemView.findViewById<TextView>(R.id.repoName)

        val forkHeader = itemView.findViewById<TextView>(R.id.forkHeader)

        val forkCount = itemView.findViewById<TextView>(R.id.forkCount)

        val starHeader = itemView.findViewById<TextView>(R.id.starHeader)

        val starCount = itemView.findViewById<TextView>(R.id.starCount)



    }

    override fun getItemCount(): Int {
        return repos_filtered.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context) //The parent is the ViewGroup at the root of the layout.
        val view = inflater.inflate(R.layout.repo_grid_item, parent, false)

        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = repos_filtered[position]

        with(holder){
            Glide.with(context)
                .load(R.drawable.repo_logo)
                .into(repoImage)

            repoName?.let{
                it.text = repo.repoName
                it.contentDescription = repo.repoName
            }

            forkHeader?.let{
                it.text = "#Fork: "
                it.contentDescription = "Fork Header"
            }

            forkCount?.let{
                it.text = repo.forks_count.toString()
                it.contentDescription = "Number of Fork"
            }

            starHeader?.let{
                it.text = "#Star: "
                it.contentDescription = "Star Header"
            }

            starCount?.let{
                it.text = repo.stargazers_count.toString()
                it.contentDescription = "Number of Star"
            }


            holder.itemView.setOnClickListener{
                itemListener.onUserItemClick(repo)
            }

        }
    }


    public fun getFilteredRepo(query: String) {
        repos_filtered.clear()

        if(query.isNullOrEmpty()){
            repos_filtered.addAll(repos_full)
        }else{
            if(repos_full.isNotEmpty() && repos_full.size>0){
                val filterPattern = query
                repos_filtered = repos_full.filter {
                    it.repoName!!.startsWith(filterPattern)
                } as ArrayList<UserRepo>
            }

        }
    }



    interface RepoItemListener{
        fun onUserItemClick(repo: UserRepo)
    }
}

