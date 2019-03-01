package com.illiarb.tmdbexplorer.coreui.ext

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText

/**
 * @author ilya-rb on 22.02.19.
 */
fun EditText.deleteContextMenuItems(vararg menuIds: Int) {
    customSelectionActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menuIds.forEach { id ->
                val menuItem = menu?.findItem(id)
                menuItem?.let {
                    menu.removeItem(id)
                }
            }
            return true
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean = false

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

        override fun onDestroyActionMode(mode: ActionMode?) {
            // No-op
        }
    }
}