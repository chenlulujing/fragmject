package com.example.fragment.library.base.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import coil.dispose
import coil.load
import com.example.fragment.library.base.R
import com.example.fragment.library.base.model.BaseViewModel

/**
 * 注意：Fragment 的存在时间比其视图长。请务必在 Fragment 的 onDestroyView() 方法中清除对视图的所有引用。
 */
abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isClickable = true
        view.isFocusable = true
        //添加loading动画
        val loadGif = ImageView(view.context)
        if (view is RelativeLayout) {
            view.addView(loadGif, RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).also {
                it.addRule(RelativeLayout.CENTER_IN_PARENT)
            })
        } else if (view is ConstraintLayout) {
            loadGif.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).also {
                it.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                it.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                it.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                it.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            }
            view.addView(loadGif)
        }
        initView()
        //更新loading动画状态
        initViewModel()?.progress(viewLifecycleOwner, {
            loadGif.load(R.drawable.icons8_monkey)
            loadGif.visibility = View.VISIBLE
        }, {
            loadGif.dispose()
            loadGif.visibility = View.GONE
        })
    }

    abstract fun initView()

    abstract fun initViewModel(): BaseViewModel?

    fun hideInputMethod() {
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: return
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}