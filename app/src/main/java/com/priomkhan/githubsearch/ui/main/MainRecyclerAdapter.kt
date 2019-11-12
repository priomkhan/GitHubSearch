package com.priomkhan.githubsearch.ui.main

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.priomkhan.githubsearch.R
import com.priomkhan.githubsearch.data.GitHubUser

/*
You need at least two layout files to display data in a recycler view.
The primary layout (main_fragment.xml) that contains the recycler view itself, and a secondary layout(monster_grid_item.xml).
That defines the appearance of each data item. In this case of each tile within a grid.
Navigate to Http://git.io/fjrzl to get the fresh layout.

 */

/*
An adapter class is designed to receive data and then apply it to each item in a recycler view.
The primary constructor for this class will receive two arguments. They'll both start with
the val keyword, so that they persist for the lifetime of the adapter. The first will be a
Context and the second will be the data (list of monster objects) that's being passed in.


 This class will be extended from a class called RecyclerView.adapter. It has a generic
 notation and I'm going to reference a class that doesn't exist yet, it'll be called ViewHolder
 and it'll be an inner class of the adapter itself.

 */


class MainRecyclerAdapter(val context: Context,
                          val users : List<GitHubUser>,
                          val itemListener: UserItemListener): RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>(){

    /*
    The ViewHolder receives one value, we will call it itemView and it'll be an instance
    of the Android view class. This class will extend another class called RecyclerView.ViewHolder
    and it requires that itemView to be passed in.
    */

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        /*
        One of the jobs of the ViewHolder is to contain references to its child views.
        So, I'll add three declarations.

        I'll create each variable get it's value by calling the expression, itemView.findViewById.
        */

        val userImage = itemView.findViewById<ImageView>(R.id.userImage)

        val nameText = itemView.findViewById<TextView>(R.id.userText)

        val repoCount = itemView.findViewById<TextView>(R.id.repoCountText)

        val repoHeader = itemView.findViewById<TextView>(R.id.repoHeadText)

    }

    /*
     Next, I need to implement a couple of functions. I'll click on the class declaration
     (MainRecyclerAdapter) and use an intention action and select implement members.
     I have three functions that I have to implement.

      The getItemCount simply returns the number of data items in your data collection.
     */
    override fun getItemCount(): Int {
        return users.size
    }

    /*
    The job of onCreateViewHolder function is to create a layout view.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context) //The parent is the ViewGroup at the root of the layout.
        val view = inflater.inflate(R.layout.search_grid_item, parent, false)
        return ViewHolder(view)
    }

    /*
     onBindViewHolder function bind data to the ViewHolder.

     The holder reference is passed in when this function is called and it'll be called for
     each item in the grid.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]

        /*
         Next, I need to assign values to each of the view objects in the holder.
         I'll start with a 'with' expression, because I'm going to be referencing the
         holder a number of different times and within this block I can reference each
         of the view objects directly.
         */

        with(holder){
            //
            nameText?.let{
                it.text = user.userSearchResult?.userName
                it.contentDescription = user.userSearchResult?.userName
            }

            //
            repoCount?.let{
                it.text = user.userDetails?.public_repos.toString()
                it.contentDescription = user.userDetails?.public_repos.toString()
            }

            repoHeader?.let{
                it.text = context.getString(R.string.repo_count_head)

                it.contentDescription = context.getString(R.string.repo_count_head)
            }

            //
            Glide.with(context)
                .load(user.userSearchResult?.avatarUrl)
                .into(userImage)



            /*
            The holder, that is the view holder has a property called item view that represents
            the root element of your layout. So I'll start with holder.itemView.setOnClickListener.
            I'll replace these parenthesis with braces because I want to use a Lambda expression.
            I want to say when the user selects this item, execute this code and I'll call
            itemListener.onMonsterItemClick and I'll pass into current monster object so I'm passing
            the selected data item back up to the fragment. And then I'll complete this code by
            implementing the onMonsterItemclick function in MainFragment.kt.
            */
            holder.itemView.setOnClickListener{
                itemListener.onUserItemClick(user)
            }

        }
    }

    interface UserItemListener{
        fun onUserItemClick(user: GitHubUser)
    }

}