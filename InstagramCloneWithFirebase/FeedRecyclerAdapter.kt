package com.janfranco.kotlinfirebaseinsta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter(private val postArrList : ArrayList<Post>) : RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {

    class PostHolder(view : View) : RecyclerView.ViewHolder(view) {
        var emailText : TextView? = null
        var commentText : TextView? = null
        var imageView : ImageView? = null

        init {
            emailText = view.findViewById(R.id.emailTextView)
            commentText = view.findViewById(R.id.commentTextView)
            imageView = view.findViewById(R.id.postImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_view_row, parent, false)
        return PostHolder(view)
    }

    override fun getItemCount(): Int {
        return postArrList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.emailText?.text = postArrList[position].email
        holder.commentText?.text = postArrList[position].comment
        Picasso.get().load(postArrList[position].downloadUrl).into(holder.imageView)
    }

}
