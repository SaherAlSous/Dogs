package com.saheralsous.dogs.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.saheralsous.dogs.R

class DetailFragment : Fragment() {
    private var dogUuid =0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    }

}

/**
 * codes used in certain parts of the course
 */

//        buttonList.setOnClickListener {
//            val action = DetailFragmentDirections.actionListFragment()
//            Navigation.findNavController(it).navigate(action)
//        }