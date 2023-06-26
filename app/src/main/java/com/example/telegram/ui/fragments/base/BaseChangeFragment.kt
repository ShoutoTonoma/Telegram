package com.example.telegram.ui.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.telegram.R
import com.example.telegram.ui.fragments.InflateChange
import com.example.telegram.utilits.APP_ACTIVITY
import com.example.telegram.utilits.hideKeyboard

open class BaseChangeFragment<VB: ViewBinding>(
    private val inflate: InflateChange<VB>
) : Fragment() {

    private var mBinding: VB? = null

    val binding get() = mBinding!!

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        APP_ACTIVITY.mAppDrawer.disableDrawer()
        hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        APP_ACTIVITY.menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings_confirm_change -> change()
        }
        return true
    }

    open fun change() {

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
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}