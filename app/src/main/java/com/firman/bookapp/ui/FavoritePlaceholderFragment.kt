package com.firman.bookapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import com.firman.bookapp.R
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest

class FavoritePlaceholderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_placeholder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentContainer = view.findViewById<FrameLayout>(R.id.fragment_container)
        if (childFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            loadFavoriteModule()
        } else {
            val loadingGroup = view.findViewById<Group>(R.id.loading_group)
            loadingGroup.visibility = View.GONE
            fragmentContainer.visibility = View.VISIBLE
        }
    }

    private fun loadFavoriteModule() {
        val context = context ?: return

        val splitInstallManager = SplitInstallManagerFactory.create(context)
        val request = SplitInstallRequest.newBuilder()
            .addModule("favorite")
            .build()

        if (splitInstallManager.installedModules.contains("favorite")) {
            navigateToRealFavoriteFragment()
            return
        }

        splitInstallManager.startInstall(request)
            .addOnSuccessListener {
                navigateToRealFavoriteFragment()
            }
            .addOnFailureListener {
            }
    }

    private fun navigateToRealFavoriteFragment() {
        try {
            view?.let { view ->
                val loadingGroup = view.findViewById<Group>(R.id.loading_group)
                val fragmentContainer = view.findViewById<FrameLayout>(R.id.fragment_container)

                loadingGroup.visibility = View.GONE
                fragmentContainer.visibility = View.VISIBLE

                val fragmentClass = Class.forName("com.firman.favorite.ui.FavoriteFragment")
                val fragment = fragmentClass.newInstance() as Fragment

                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}