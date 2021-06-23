package com.saheralsous.dogs.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saheralsous.dogs.R

class ListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        buttonDetail.setOnClickListener {
//            /**
//             * [Navigation] component created these actions to be used by the programmer.
//             */
//            val action = ListFragmentDirections.actionDetailFragment()
//            /**
//             * adding a value to argument transferred
//             */
//            action.dogUid = 5
//            Navigation.findNavController(it).navigate(action)
//        }


    }

}