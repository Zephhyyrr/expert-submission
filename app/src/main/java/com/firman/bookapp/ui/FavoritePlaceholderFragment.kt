package com.firman.bookapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        loadFavoriteModule()
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
                // Module installation has started
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    private fun navigateToRealFavoriteFragment() {
        try {
            // Dynamically load the FavoriteFragment class
            val fragmentClass = Class.forName("com.firman.favorite.ui.FavoriteFragment")
            val fragment = fragmentClass.newInstance() as Fragment

            // Replace this placeholder with the real fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}