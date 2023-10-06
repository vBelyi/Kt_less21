package com.example.kt_less21

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var listView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        listView = findViewById(R.id.listView)
        val api = ApiClient.client.create(ApiInterface::class.java)

        MainScope().launch {
            try {
                val response: List<GetResponse> = GlobalScope.async {
                    api.getRequest()
                }.await()
                if (response.isNotEmpty()) {
                    val items = response

                    // Створюємо адаптер та передаємо дані у фрагмент ListFragment
                    items?.let {
                        val myAdapter = MyRecyclerViewAdapter(it) { selectedItem ->
                            val detailFragment = ListFragment.newInstance(
                                selectedItem.biography.fullName,
                                selectedItem.biography.firstAppearance,
                                selectedItem.powerstats.power,
                                selectedItem.appearance.race,
                                selectedItem.images.lg
                            )
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.myFragment, detailFragment)
                                .addToBackStack(null)
                                .commit()

                            listView.visibility = View.GONE
                        }

                        listView.adapter = myAdapter
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        val dividerItemDecoration = DividerItemDecoration(listView.context, LinearLayoutManager.VERTICAL)
        listView.addItemDecoration(dividerItemDecoration)
        listView.layoutManager = LinearLayoutManager(this)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            listView.visibility = View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }
}