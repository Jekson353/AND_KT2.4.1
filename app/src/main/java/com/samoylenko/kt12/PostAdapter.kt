package com.samoylenko.kt12

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.samoylenko.kt12.databinding.CardPostBinding

interface OnInteractionListener{
    fun onLike(post: Post){}
    fun onEdit(post: Post){}
    fun onRemove(post: Post){}
    fun onShare(post: Post){}
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            imgAvatar.setImageResource(R.drawable.ic_netology)
            author.text = post.author
            txtDate.text = post.published
            textData.text = post.content
            countView.text = post.countVisability.toString()
            countLike.text = Calc.intToText(post.like)
            countShare.text = post.sharing.toString()
            like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_baseline_favorite_border_24)

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener {itemView ->
                        when (itemView.itemId){
                            R.id.deleteView ->{
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.editView -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }

                    }
                }.show()
            }
        }
    }
}