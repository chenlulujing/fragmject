package com.example.fragment.module.wan.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fragment.library.base.adapter.BaseAdapter
import com.example.fragment.library.base.model.BaseViewModel
import com.example.fragment.library.base.utils.toppingToPosition
import com.example.fragment.library.common.bean.ArticleBean
import com.example.fragment.library.common.bean.NavigationBean
import com.example.fragment.library.common.constant.Keys
import com.example.fragment.library.common.constant.Router
import com.example.fragment.library.common.fragment.RouterFragment
import com.example.fragment.library.common.model.TabEventViewMode
import com.example.fragment.module.wan.R
import com.example.fragment.module.wan.adapter.LinkMenuAdapter
import com.example.fragment.module.wan.databinding.NavigationLinkFragmentBinding
import com.example.fragment.module.wan.model.NavigationViewModel

class NavigationLinkFragment : RouterFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): NavigationLinkFragment {
            return NavigationLinkFragment()
        }
    }

    private val tabEventViewModel: TabEventViewMode by activityViewModels()
    private val navigationViewModel: NavigationViewModel by viewModels()
    private var _binding: NavigationLinkFragmentBinding? = null
    private val binding get() = _binding!!
    private val linkMenuAdapter = LinkMenuAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NavigationLinkFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        tabEventViewModel.navigationTab().observe(viewLifecycleOwner) {
            if (it == 1) {
                binding.list.toppingToPosition(0)
                tabEventViewModel.setNavigationTab(0)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        tabEventViewModel.navigationTab().removeObservers(viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.list.adapter = null
        _binding = null
    }

    override fun initView() {
        //导航列表
        binding.list.layoutManager = LinearLayoutManager(binding.list.context)
        binding.list.adapter = linkMenuAdapter
        linkMenuAdapter.setOnItemSelectedListener(object : BaseAdapter.OnItemSelectedListener {
            override fun onItemSelected(itemView: View, position: Int) {
                val item = linkMenuAdapter.getItem(position)
                item.isSelected = true
                binding.list.findViewHolderForAdapterPosition(position)?.apply {
                    if (this is BaseAdapter.ViewBindHolder) {
                        getView<View>(R.id.bg)?.setBackgroundResource(R.drawable.rectangle_solid_white_top0_5bottom0_5_line)
                    }
                }
                fillFlexboxLayout(item.articles)
            }

            override fun onItemUnselected(itemView: View, position: Int) {
                val item = linkMenuAdapter.getItem(position)
                item.isSelected = false
                binding.list.findViewHolderForAdapterPosition(position)?.apply {
                    if (this is BaseAdapter.ViewBindHolder) {
                        getView<View>(R.id.bg)?.setBackgroundResource(R.drawable.rectangle_solid_gray_bottom1_line)
                    }
                }
            }
        })
    }

    override fun initViewModel(): BaseViewModel {
        navigationViewModel.navigationResult().observe(viewLifecycleOwner) {
            updateView(it)
        }
        return navigationViewModel
    }

    private fun updateView(data: List<NavigationBean>) {
        var selectItem = 0
        data.forEachIndexed { index, bean ->
            if (bean.isSelected) {
                selectItem = index
            }
        }
        data[selectItem].isSelected = true
        linkMenuAdapter.setNewData(data)
        linkMenuAdapter.selectItem(selectItem)
        fillFlexboxLayout(data[selectItem].articles)
    }

    private fun fillFlexboxLayout(data: List<ArticleBean>? = null) {
        binding.fbl.removeAllViews()
        data?.forEach { article ->
            val inflater = LayoutInflater.from(binding.fbl.context)
            val tv = inflater.inflate(R.layout.link_fbl, binding.fbl, false) as TextView
            tv.text = article.title
            tv.setOnClickListener {
                val url = Uri.encode(article.link)
                navigation(Router.WEB, bundleOf(Keys.URL to url))
            }
            binding.fbl.addView(tv)
        }
    }

}