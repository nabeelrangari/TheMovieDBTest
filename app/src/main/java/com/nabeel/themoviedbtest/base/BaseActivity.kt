package com.nabeel.themoviedbtest.base

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.nabeel.themoviedbtest.util.NetworkStateReceiver

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() ,
    NetworkStateReceiver.NetworkStateReceiverListener{
    protected var mViewModel: VM? = null
    private var networkStateReceiver: NetworkStateReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        addListener()
        setToolbar()
        initView()
        initVM()
        setListeners()
    }

    private fun addListener() {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    private fun removeListener() {
        try {
            networkStateReceiver!!.removeListener(this)
            unregisterReceiver(networkStateReceiver)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
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

    open fun initView() {}

    open fun setListeners() {}

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
        removeListener()
        super.onDestroy()
    }
}