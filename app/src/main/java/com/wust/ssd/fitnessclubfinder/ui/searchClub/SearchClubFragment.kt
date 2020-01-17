package com.wust.ssd.fitnessclubfinder.ui.searchClub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.wust.ssd.fitnessclubfinder.R
import com.wust.ssd.fitnessclubfinder.di.Injectable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_search_club.*
import kotlinx.android.synthetic.main.search_club_item.view.*
import javax.inject.Inject

class SearchClubFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var searchClubViewModel: SearchClubViewModel? = null

    private lateinit var adapter: MySearchItemAdapter

    companion object {

        @JvmStatic
        fun newInstance() =
            SearchClubFragment().apply {
                arguments = Bundle().apply {
                    // putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            // columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search_club, container, false)
        val textView: TextView = root.findViewById(R.id.text_search_club)
        searchClubViewModel?.text?.observe(this, Observer {
            textView.text = it
        })

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (searchClubViewModel == null) {
            searchClubViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(SearchClubViewModel::class.java)
        }

        val items = listOf(
            Search_Item("Gym 01", null),
            Search_Item("Gym 02", "124"),
            Search_Item("Gym 03", "4"),
            Search_Item("Gym 04", null),
            Search_Item("Gym 05", "103")
        )
        adapter = MySearchItemAdapter()
        adapter.replaceItems(items)
        list.adapter = adapter
    }

    class MySearchItemAdapter : RecyclerView.Adapter<MySearchItemAdapter.ViewHolder>() {
        private var items = listOf<Search_Item>()


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_club_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]

            holder.itemView.contentTextView.text = item.content
            holder.itemView.distanceTextView.text = item.distance
        }

        fun replaceItems(items: List<Search_Item>) {
            this.items = items
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int = items.size

        inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer
    }
}