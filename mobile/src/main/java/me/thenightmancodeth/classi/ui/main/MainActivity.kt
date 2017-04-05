package me.thenightmancodeth.classi.ui.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import me.thenightmancodeth.classi.R
import me.thenightmancodeth.classi.data.model.Class
import me.thenightmancodeth.classi.ui.base.BaseActivity
import me.thenightmancodeth.classi.ui.main.list.MainAdapter
import me.thenightmancodeth.classi.utilities.extensions.DividerItemDecoration
import me.thenightmancodeth.classi.utilities.extensions.dpToPx
import me.thenightmancodeth.classi.utilities.extensions.hide
import javax.inject.Inject

/**
 * Created by joe on 4/5/17.
 */

open class MainActivity : BaseActivity(), MainView {

    @Inject lateinit var presenter: MainPresenter
    @Inject lateinit var adapter: MainAdapter

    override fun getLayoutResID() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)

        setupToolbar()
        setupRecycler()
        setupSwipeToRefresh()

        presenter.bind(this)
    }

    private fun setupToolbar() {
        toolbarTitle.text = "Classi"
    }

    private fun setupRecycler() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 8.dpToPx(this)))
        recyclerView.adapter = adapter
    }

    private fun setupSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener { presenter.fetchClasses() }
    }

    override fun onFetchClassSuccess(classes: List<Class>) {
        adapter.clearItems()
        adapter.addItems(classes)
        swipeRefreshLayout.isRefreshing = false
        statusText.hide()
    }

    override fun onFetchClassError(error: Throwable) {
        Toast.makeText(this, "Error fetching classes", Toast.LENGTH_LONG).show()
        error.printStackTrace()
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}
