package com.example.telegram.ui.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.telegram.ui.fragments.Inflate
import com.example.telegram.utilits.APP_ACTIVITY

abstract class BaseRegisterFragment<VB: ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    private var mBinding: VB? = null

    val binding get() = mBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = inflate.invoke(inflater, container, false)
        return this.binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}