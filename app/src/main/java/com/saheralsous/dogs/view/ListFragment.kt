package com.saheralsous.dogs.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.saheralsous.dogs.R
import com.saheralsous.dogs.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*



class ListFragment : Fragment() {

    private lateinit var viewModel : ListViewModel
    private val dogsListAdapter by lazy {
        DogsListAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * enabling the menu
         */
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()

        /**
         * the recycler view
         */
        dogs_list.apply {
            layoutManager =LinearLayoutManager(context)
            adapter = dogsListAdapter
        }

        /**
         * creating a refresh process (getting it from cache this time)
         */
        refresh_layout.setOnRefreshListener {
            dogs_list.visibility =View.GONE
            list_error.visibility = View.GONE
            loading_view.visibility = View.VISIBLE
            viewModel.refreshByPassCache()
            refresh_layout.isRefreshing = false
        }

        observerViewModel()

    }

    private fun observerViewModel() {
        viewModel.dogs.observe(viewLifecycleOwner,
        Observer { dogs ->
            dogs?.let {
                dogs_list.visibility = View.VISIBLE
                dogsListAdapter.updateDogList(dogs)
            }
        })
        viewModel.dogsLoadError.observe(viewLifecycleOwner,
        Observer { isError ->
            isError?.let {
                list_error.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(
            viewLifecycleOwner,
            Observer { isLoading ->
            isLoading?.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    list_error.visibility = View.GONE
                    dogs_list.visibility = View.GONE
                }
            }
          }
        )
    }

    /**
     * creating the menu and setting it
     * video 58
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_settingsFragment -> {
                view?.let{
                    Navigation.findNavController(it).navigate(ListFragmentDirections.actionSettingsFragment())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}


/**
 * codes used in certain parts of the course
 */
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