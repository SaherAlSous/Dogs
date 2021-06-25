package com.saheralsous.dogs.util


import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.saheralsous.dogs.R

/**
 * extending imageview with KTX to create a spinner image while the actual image is being loaded
 */

fun getProgressDrawable(context: Context) : CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius =50f
        start()
    }
}

/**
 * creating an extension to load images
 */

fun ImageView.loadImage(url: String?, progressDrawable: CircularProgressDrawable){
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_dog_round)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

/**
 * biding image with data binding
 */
@BindingAdapter("android:imageUrl") // <-- this tag will show inside the XML image values to load the imageUrl in it.
fun loadImage(view: ImageView, url: String?){
    view.loadImage(url, getProgressDrawable(view.context))
}