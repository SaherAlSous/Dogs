package com.saheralsous.dogs.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.saheralsous.dogs.R
import com.saheralsous.dogs.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    private var dogUuid =0
    private lateinit var viewModel : DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.fetch()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * getting the argument from navigation
         */
        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUid
        }
        observerViewModel()
    }

    private fun observerViewModel() {
        viewModel.dogLiveData.observe(
            viewLifecycleOwner,
            Observer {  dog ->
                dog_name.text = dog.dogBreed
                dog_purpose.text = dog.bredFor
                dog_Temperament.text = dog.temperament
                dog_lifespan.text = dog.LifeSpan
            }
        )
    }

}

/**
 * codes used in certain parts of the course
 */

//        buttonList.setOnClickListener {
//            val action = DetailFragmentDirections.actionListFragment()
//            Navigation.findNavController(it).navigate(action)
//        }