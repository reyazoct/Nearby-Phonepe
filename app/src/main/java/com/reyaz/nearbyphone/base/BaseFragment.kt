package com.reyaz.nearbyphone.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.reyaz.nearbyphone.utils.Inflate

abstract class BaseFragment<T : ViewDataBinding>(private val inflate: Inflate<T>): Fragment() {

    private var _binding: T? = null

    protected val binding: T
        get() = _binding ?: throw IllegalStateException("View not initialized")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }
}