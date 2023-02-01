package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    /**
     * RecyclerView Adapter for converting a list of Asteroid to cards.
     */
    private var viewModelAdapter: AsteroidAdapter? = null


    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onViewCreated(), which we
     * do in this Fragment.
     */
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this,
            MainViewModelFactory(requireActivity().application))[MainViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModelAdapter = AsteroidAdapter(AsteroidClickListener { asteroid ->
            // When an Asteroid is clicked this block or lambda will be called by AsteroidAdapter
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })
        binding.asteroidRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter

        }

//        binding.apply {
//            asteroidRecycler.apply {
//                adapter?.stateRestorationPolicy =
//                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
//            }
//        }

        viewModel.asteroids.observe(viewLifecycleOwner) {
//            if (it.isNullOrEmpty()) {
////                if (it.fir)
//                binding.statusLoadingWheel.visibility = View.VISIBLE
//            }
        }


        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
