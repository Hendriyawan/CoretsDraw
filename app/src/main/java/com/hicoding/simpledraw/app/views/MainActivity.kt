package com.hicoding.simpledraw.app.views

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.enrico.colorpicker.colorDialog
import com.google.android.material.tabs.TabLayout
import com.hicoding.simpledraw.app.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        initTabLayout()
    }

    @Suppress("DEPRECATION")
    private fun initTabLayout() {
        (tab_layout as TabLayout).apply {
            addTab(this.newTab().setIcon(R.drawable.ic_action_undo))
            addTab(this.newTab().setIcon(R.drawable.ic_action_redo))
            addTab(this.newTab().setIcon(R.drawable.ic_action_delete))
            getTabAt(0)?.icon?.setColorFilter(ContextCompat.getColor(this@MainActivity, android.R.color.white), PorterDuff.Mode.SRC_IN)
            getTabAt(1)?.icon?.setColorFilter(ContextCompat.getColor(this@MainActivity, android.R.color.white), PorterDuff.Mode.SRC_IN)
            getTabAt(2)?.icon?.setColorFilter(ContextCompat.getColor(this@MainActivity, android.R.color.white), PorterDuff.Mode.SRC_IN)
            setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    setActionTabSelected(tab)
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                    setActionTabSelected(tab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }

    private fun setActionTabSelected(tab: TabLayout.Tab) {
        when (tab.position) {
            0 -> drawing_view.undo()
            1 -> drawing_view.redo()
            2 -> colorDialog.showColorPicker(this, 1)
        }
    }
}