package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() , MenuProvider {

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


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // Add menu items here
        menuInflater.inflate(R.menu.main_overflow_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        // Handle the menu selection
        return when (menuItem.itemId) {
            R.id.show_week_asteroid_menu -> {
                viewModel.onShowWeekAsteroidMenuClicked()
                true
            }
            R.id.show_tody_asteroid_menu -> {
                viewModel.onShowTodayAsteroidMenuClicked()
                true
            }
            R.id.show_saved_asteroid_menu -> {
                viewModel.onShowSavedAsteroidMenuClicked()
                true
            }
            else -> false
        }
    }
}
