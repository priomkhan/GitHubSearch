package com.priomkhan.githubsearch.utilities

import android.util.Log
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.data.UserRepo
import com.priomkhan.githubsearch.ui.details.DetailedFragment
import com.priomkhan.githubsearch.ui.details.DetailedRepoRecyclerAdapter
import com.priomkhan.githubsearch.ui.shared.SharedViewModel
import java.text.NumberFormat


/*
Simple text values can be displayed with data binding expressions in the layout file. More complex
assignments though, might require the use of adapter functions. These are top level functions that
 are associated with your layout file with property names that you assign. If you're programming in
 kotlin, these functions can be placed in any kotlin file, not necessarily in a class. The classic
 example of a binding adapter is the dynamic presentation of an image. The image view component has
 a source attribute that can work directly with a URL or a file, but I'm using the Glide open source
  library to present my images, and there's no way to call complex kotlin or java code from within
  the layout. So instead, I'll create a binding adapter function.


  To create a binding adapter function, start with an annotation of binding adapter. This is from
  the Android X dot data binding library. Then, pass in the name of an attribute that you want to
  use to assign a value from the layout file. You can name this anything you like. For consistency,
   though, I'll use imageurl, the same way I've spelled it previously in other parts of the app.
   Next I'll define a function. The name of the function could be anything you want, but it should
   require two arguments. The first is a reference to the view object, and for this I'll be passing
   in an image view, and the second is a value that you can use to assign something to the view.
   Once again, I'll create a property called imageurl and this will be a string. Within the function,
   I simply need to use that html_url to assign the image to the image view and as I've done previously,
   I'll use the Glide library. I'll call Glide dot with, I'll pass in view dot context as the context,
   then dot load, and I'll pass in imageurl and then dot into and I'll pass in the view object,
   and my binding adapter function is complete.

   It can be called from anywhere in the application and it's identified by this "imageUrl" string which becomes
   the name of an attribute you can use in the layout.

   Next go to the fragment_detail.xml and add a app attribute to show monsterImage.

   In same way we can also make adapter for price.
 */


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String){
    Glide.with(view.context)
        .load(imageUrl)
        .into(view)
}

@BindingAdapter("userName")
fun userName(view: TextView, value: String?){
    var text =" "
    if(value.equals(null)){
        text = "Not Specified"
    }else{
        text = "${value}"

    }
    view.text = text
}

@BindingAdapter("userEmail")
fun userEmail(view: TextView, value: String?){
    var text =" "
    if(value.equals(null)){
        text = "Email: n/a "
    }else{
        text = "Email: ${value}"

    }

    view.text = text
}
@BindingAdapter("userLocation")
fun userLocation(view: TextView, value: String?){
    var text =" "
    if(value.equals(null)){
        text = "Location: n/a "
    }else{
        text = "Location: ${value}"

    }

    view.text = text
}

@BindingAdapter("userJoinDate")
fun userJoinDate(view: TextView, value: String?){
    var text =" "
    //val formatter = DateTimeFormatter.ISO_INSTANT
    if(value.equals(null)){
        text = "Join Date: n/a"
    }else{
       // val date = formatter.parse(value)
       // val formattedTo = DateTimeFormatter.ISO_DATE
       // val formattedDate = formattedTo.format(date)
        text = "Date: ${value}"
        //Log.i(LOG_TAG, "Date: ${formattedDate}")
    }

    view.text = text
}

@BindingAdapter("followers")
fun userFollower(view: TextView, value: Int){
    val formatter = NumberFormat.getNumberInstance()
    val text = "${formatter.format(value)}  Followers"
    view.text = text
}

@BindingAdapter("following")
fun userFollowing(view: TextView, value: Int){
    val formatter = NumberFormat.getNumberInstance()
    val text = "Following ${formatter.format(value)}"
    view.text = text
}

@BindingAdapter("userBio")
fun userBio(view: TextView, value: String?){
    var text =" "
    if(value.equals(null)){
        text = "Biography: Not Specified"
    }else{
        text = "${value}"

    }
    view.text = text
}

@BindingAdapter("adapter", "repoList", requireAll = false)
fun repoList(view: RecyclerView, adapter: DetailedRepoRecyclerAdapter, repos: List<UserRepo>){

    view.adapter = adapter
}


@BindingAdapter("searchValue")
fun searchView(view: SearchView, value: String?){
    var text =""

}