package com.saheralsous.dogs.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.saheralsous.dogs.R
import com.saheralsous.dogs.Util.getProgressDrawable
import com.saheralsous.dogs.Util.loadImage
import com.saheralsous.dogs.model.DogBreed
import kotlinx.android.synthetic.main.item_dog.view.*



class DogsListAdapter : RecyclerView.Adapter<DogsListAdapter.DogViewHolder>() {

    private lateinit var dogsList : ArrayList<DogBreed>

    /**
     * update the dog list
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateDogList(newDogsList: List<DogBreed>){
        dogsList = ArrayList(newDogsList)
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    class DogViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater  = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.itemView.name.text = dogsList.get(position).dogBreed
        holder.itemView.life_span.text = dogsList.get(position).LifeSpan
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(ListFragmentDirections.actionDetailFragment())
        }
        /**
         * loading image using the ImageView Extension
         */
        holder.itemView.image_view.loadImage(dogsList[position].imageUrl, getProgressDrawable(holder.itemView.image_view.context))
        /**
         * getting the context from the image_view context.
         * we removed the old image because we assigned the loading spinner instead inside the Util.
         */
    }

    override fun getItemCount(): Int {
        return dogsList.size
    }
}