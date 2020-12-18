package com.virogu.paging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class Person(var name: String = "", var age: Int = 0)

class MainViewModel : ViewModel() {
    private val dataSourceFactory = DataSourceFactory()
    val dataSourceLiveData = dataSourceFactory.sourceLiveData
    val concertList: LiveData<PagedList<Person>> = dataSourceFactory.toLiveData(pageSize = 20)
}

class MainActivity : AppCompatActivity() {
    private lateinit var model: MainViewModel
    private var loadingStateLiveDate: MutableLiveData<Boolean>? = null
    private var loadStateLiveDate: MutableLiveData<Boolean>? = null
    private var dataSource: MyDataSource? = null
        set(value) {
            if (field != value) {
                field = value
                initLiveData()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        model = ViewModelProvider(this)[MainViewModel::class.java]
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = MyAdapter()
        recyclerView.adapter = adapter
        model.dataSourceLiveData.observe(this, Observer {
            dataSource = it
        })
        model.concertList.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun initLiveData() {
        loadingStateLiveDate?.removeObservers(this)
        loadStateLiveDate?.removeObservers(this)
        loadStateLiveDate = dataSource?.loadStateLiveDate
        loadingStateLiveDate = dataSource?.loadingStateLiveDate
        loadingStateLiveDate?.observe(this, Observer {
            lay_loading.visibility = if (it)
                View.VISIBLE
            else
                View.GONE
        })
        loadStateLiveDate?.observe(this, Observer {
            if (it) {
                showToast("加载成功")
            } else {
                showToast("加载失败")
                GlobalScope.launch(Dispatchers.IO) {
                    dataSource?.retryAllFailed()
                }
            }
        })
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}

internal class MyAdapter :
    PagedListAdapter<Person, MyViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val concert: Person? = getItem(position)
        // Note that "concert" is a placeholder if it's null.
        holder.bindTo(concert)
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Person>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldConcert: Person,
                newConcert: Person
            ) = oldConcert.name == newConcert.name

            override fun areContentsTheSame(
                oldConcert: Person,
                newConcert: Person
            ) = (oldConcert.name == newConcert.name) &&
                    (oldConcert.name == newConcert.name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_2, parent, false)
        )
    }
}

internal class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(android.R.id.text1)
    private val age: TextView = itemView.findViewById(android.R.id.text2)
    fun bindTo(item: Person?) {
        if (null == item) {
            name.text = "加载中..."
            age.text = "加载中..."
        } else {
            name.text = item.name
            age.text = item.age.toString()
        }
    }
}


class MyDataSource : PageKeyedDataSource<Int, Person>() {

    val loadingStateLiveDate = MutableLiveData<Boolean>()
    val loadStateLiveDate = MutableLiveData<Boolean>()
    private var retry: (() -> Any)? = null
    private var retryTimes = 0
    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        if (retryTimes >= 3) {
            retryTimes = 0
            return
        } else {
            retryTimes++
            prevRetry?.invoke()
        }

    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Person>
    ) {
        loadingStateLiveDate.postValue(true)
        try {
            Timber.i("loadInitial,size:${params.requestedLoadSize}")
            val p: MutableList<Person> = ArrayList()
            for (i in 0 until params.requestedLoadSize) {
                p.add(Person("Tom${i}", 22))
            }
            Thread.sleep(3000)
            Timber.i("返回数据")
            loadStateLiveDate.postValue(true)
            retryTimes = 0
            callback.onResult(p, null, 2)
        } catch (e: Throwable) {
            loadStateLiveDate.postValue(false)
            retry = {
                loadInitial(params, callback)
            }
        } finally {
            loadingStateLiveDate.postValue(false)
        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Person>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Person>) {
        loadingStateLiveDate.postValue(true)
        try {
            Timber.i("loadInitial,size:${params.requestedLoadSize}")
            val p: MutableList<Person> = ArrayList()
            for (i in 0 until params.requestedLoadSize) {
                p.add(Person("Tom${i}", 22))
            }
            Timber.i("sleep 500")
            Thread.sleep(3000)
            if (params.key > 3) {
                throw Throwable("test error")
            }
            Timber.i("返回数据")
            loadStateLiveDate.postValue(true)
            retryTimes = 0
            callback.onResult(p, params.key + 1)
        } catch (e: Throwable) {
            loadStateLiveDate.postValue(false)
            retry = {
                loadAfter(params, callback)
            }
        } finally {
            loadingStateLiveDate.postValue(false)
        }
    }

}

class DataSourceFactory : DataSource.Factory<Int, Person>() {
    val sourceLiveData = MutableLiveData<MyDataSource>()
    override fun create(): DataSource<Int, Person> {
        val source = MyDataSource()
        sourceLiveData.postValue(source)
        return source
    }

}