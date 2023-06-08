package com.example.telegram.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.telegram.MainActivity

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB: ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    private var mBinding: VB? = null

    val binding get() = mBinding!!

    override fun onStart() {
        super.onStart()
        val parentActivity = activity
        if (parentActivity is MainActivity) parentActivity.mAppDrawer.disableDrawer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = inflate.invoke(inflater, container, false)
        return this.binding.root
    }

    override fun onStop() {
        super.onStop()
        val parentActivity = activity
        if (parentActivity is MainActivity) parentActivity.mAppDrawer.enableDrawer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}