package com.illiarb.tmdbclient.feature.movies

import android.os.Bundle
import com.arellomobile.mvp.MvpDelegate
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment

/**
 * AndroidX version of moxy MvpAppCompatFragment
 *
 * Date: 19-Dec-15
 * Time: 13:25
 *
 * @author Alexander Blinov
 * @author Yuri Shmakov
 * @author Konstantin Tckhovrebov
 */
abstract class MvpAppCompatFragment : BaseFragment() {

    private var _mvpDelegate: MvpDelegate<out MvpAppCompatFragment>? = null

    private var isStateWasSaved: Boolean = false

    /**
     * @return The [MvpDelegate] being used by this Fragment.
     */
    val mvpDelegate: MvpDelegate<*>
        get() {
            if (_mvpDelegate == null) {
                _mvpDelegate = MvpDelegate(this)
            }
            return _mvpDelegate!!
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvpDelegate.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        isStateWasSaved = false

        mvpDelegate.onAttach()
    }

    override fun onResume() {
        super.onResume()

        isStateWasSaved = false

        mvpDelegate.onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        isStateWasSaved = true

        mvpDelegate.onSaveInstanceState(outState)
        mvpDelegate.onDetach()
    }

    override fun onStop() {
        super.onStop()
        mvpDelegate.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mvpDelegate.onDetach()
        mvpDelegate.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

        //We leave the screen and respectively all fragments will be destroyed
        if (activity!!.isFinishing) {
            mvpDelegate.onDestroy()
            return
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (isStateWasSaved) {
            isStateWasSaved = false
            return
        }

        // See https://github.com/Arello-Mobile/Moxy/issues/24
        var anyParentIsRemoving = false
        var parent = parentFragment

        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }

        if (isRemoving || anyParentIsRemoving) {
            mvpDelegate.onDestroy()
        }
    }
}