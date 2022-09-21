package com.theappland.veroandroidtaskapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.theappland.veroandroidtaskapp.R
import com.theappland.veroandroidtaskapp.adapter.TaskRecyclerViewAdapter
import com.theappland.veroandroidtaskapp.databinding.FragmentTaskBinding
import com.theappland.veroandroidtaskapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment: Fragment(R.layout.fragment_task) {

    private lateinit var binding: FragmentTaskBinding
    private lateinit var taskViewModel: TaskViewModel
    private val taskRecyclerViewAdapter = TaskRecyclerViewAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        binding.taskRecyclerView.layoutManager = GridLayoutManager(requireContext(),1)
        binding.taskRecyclerView.adapter = taskRecyclerViewAdapter

        binding.taskSwipeRefreshLayout.setOnRefreshListener {
            binding.taskRecyclerView.visibility = View.GONE
            taskViewModel.getFromApi(requireContext())
            binding.taskSwipeRefreshLayout.isRefreshing = false
            binding.taskRecyclerView.visibility = View.VISIBLE
        }

        val taskSearch = view.findViewById<SearchView>(R.id.taskSearchView)

        val searchIcon = taskSearch.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.parseColor("#746D69"))

        val cancelIcon = taskSearch.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.parseColor("#746D69"))

        val textView = taskSearch.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        textView.setTextColor(Color.BLACK)

        binding.taskSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    taskRecyclerViewAdapter.filter.filter(it)
                }
                return false
            }
        })

        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val qrText = bundle.getString("bundleKey")

            qrText?.let {
                if (qrText != " ") {
                    taskViewModel.isLoading.value = true
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            binding.taskSearchView.setQuery(qrText,false)
                            taskViewModel.isLoading.value = false
                        },
                        200
                    )
                }
            }
        }

        binding.qrScannerImageView.setOnClickListener {
            taskViewModel.isLoading.value = true
            binding.taskSearchView.setQuery("",false)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    val action = TaskFragmentDirections.actionTaskFragmentToQRFragment()
                    findNavController().navigate(action)
                    taskViewModel.isLoading.value = false
                },
                200
            )
        }

        taskViewModel.getTaskData(requireContext())

        observeLiveData()
    }

    private fun observeLiveData() {
        taskViewModel.tasks.observe(viewLifecycleOwner) {tasks ->
            taskRecyclerViewAdapter.updateTasks(tasks)
        }

        taskViewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            if (isLoading) {
                binding.taskSearchView.visibility = View.GONE
                binding.qrScannerImageView.visibility = View.GONE
                binding.taskRecyclerView.visibility = View.GONE
                binding.taskProgressBar.visibility = View.VISIBLE
            } else {
                binding.taskProgressBar.visibility = View.GONE
                binding.taskSearchView.visibility = View.VISIBLE
                binding.qrScannerImageView.visibility = View.VISIBLE
                binding.taskRecyclerView.visibility = View.VISIBLE
            }
        }

        taskViewModel.dataLoading.observe(viewLifecycleOwner) {dataLoading ->
            if (dataLoading) {
                binding.taskRecyclerView.visibility = View.GONE
                binding.taskProgressBar.visibility = View.VISIBLE
            } else {
                binding.taskProgressBar.visibility = View.GONE
                binding.taskRecyclerView.visibility = View.VISIBLE
            }
        }
    }
}