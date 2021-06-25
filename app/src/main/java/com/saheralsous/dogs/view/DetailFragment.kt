package com.saheralsous.dogs.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.saheralsous.dogs.R
import com.saheralsous.dogs.databinding.FragmentDetailBinding
import com.saheralsous.dogs.model.DogPalette
import com.saheralsous.dogs.util.getProgressDrawable
import com.saheralsous.dogs.util.loadImage
import com.saheralsous.dogs.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    private var dogUuid =0
    private lateinit var viewModel : DetailViewModel
    private lateinit var dataBinding : FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * implementing databinding in the fragment
         */
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * getting the argument from navigation
         */
        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUid
        }
        viewModel.fetch(dogUuid)

        observerViewModel()
    }

    private fun observerViewModel() {
        viewModel.dogLiveData.observe(
            viewLifecycleOwner,
            Observer {  dog ->
                dog.let {

                    dataBinding.dog = dog // binding the view with the observed data

                    it.imageUrl.let {
                        setupBackgroundColor(it!!) //using Palette
                        /**
                         * we can use the palette to color the text/boxes...etc
                         */
                    }
                }
            }
        )
    }
    private fun setupBackgroundColor(url : String){
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate{palette ->
                            val intColor = palette?.lightMutedSwatch?.rgb ?: 0 //getting the value of the color or 0
                            // palette?.vibrantSwatch?.rgb <-- play for it in other apps. this one has an issue.
                            val myPalette = DogPalette(intColor) //the class
                            dataBinding.palette = myPalette //attaching the palette to the layout xml.
                        }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    //nothing to do
                }

            })
    }
}

/**
 * codes used in certain parts of the course
 */

//        buttonList.setOnClickListener {
//            val action = DetailFragmentDirections.actionListFragment()
//            Navigation.findNavController(it).navigate(action)
//        }

//                dog_name.text = dog.dogBreed
//                dog_purpose.text = dog.bredFor
//                dog_Temperament.text = dog.temperament
//                dog_lifespan.text = dog.LifeSpan
//                dog_image.loadImage(dog.imageUrl, getProgressDrawable(dog_image.context))