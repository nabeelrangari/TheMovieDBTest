package com.nabeel.themoviedbtest.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.nabeel.themoviedbtest.util.NetworkStateReceiver

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {
    private var mViewModel: VM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        setToolbar()
        initVM()
    }

    private fun setToolbar() {
        providerToolBar()?.let { setSupportActionBar(it) }
    }

    /**
     * id
     */
    abstract fun layoutId(): Int

    private fun initVM() {
        providerVMClass()?.let { it ->
            mViewModel = ViewModelProvider(this).get(it)
            lifecycle.addObserver(mViewModel!!)
        }
    }

    /**
    [Toolbar]
     */
    open fun providerToolBar(): Toolbar? = null

    /**
     * [BaseViewModel]
     */
    open fun providerVMClass(): Class<VM>? = null


    override fun onDestroy() {
        mViewModel?.let {
            lifecycle.removeObserver(it)
        }
        super.onDestroy()
    }
}