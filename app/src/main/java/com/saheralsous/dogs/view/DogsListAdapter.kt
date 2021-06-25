package com.saheralsous.dogs.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.saheralsous.dogs.R
import com.saheralsous.dogs.databinding.ItemDogBinding
import com.saheralsous.dogs.model.DogBreed
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.item_dog.view.*



class DogsListAdapter : RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(), DogClickListener {

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

    /**
     * adding databinding to the adapter. we change it
     * from View to ItemDogBinding after adding it to the xml
     */
    inner class DogViewHolder(val view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater  = LayoutInflater.from(parent.context)
      //  val view = inflater.inflate(R.layout.item_dog, parent, false)
        val view = DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false) // adding databinding
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.dogs = dogsList[position]
        // attaching the click listener to the adapter.
        holder.view.listener = this

//        holder.itemView.name.text = dogsList.get(position).dogBreed
//        holder.itemView.life_span.text = dogsList.get(position).LifeSpan
//        holder.itemView.setOnClickListener {
//            Navigation.findNavController(it).navigate(ListFragmentDirections.actionDetailFragment())
//        }
    }

    override fun getItemCount(): Int {
        return dogsList.size
    }

    /**
     * creating the click listener and taking the uuid from the xml
     * and passing it to the detail fragment using navigation
     */
    override fun onDogClicked(v: View) {
        val uuid = v.dog_id.text.toString().toInt()
        val action = ListFragmentDirections.actionDetailFragment()
        action.dogUid = uuid
        Navigation.findNavController(v).navigate(action)
    }
}