package com.broersen.madlevel4task2

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HistoryActivity: AppCompatActivity() {
    private lateinit var gameRepository: GameRepository

    private var games = arrayListOf<Game>()
    private var gameAdapter = GameAdapter(games)

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_history)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        gameRepository = GameRepository(applicationContext)

        initRv()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return true
    }

    private fun initRv(){

        rv_games.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rv_games.adapter = gameAdapter

        createItemTouchHelper().attachToRecyclerView(rv_games)
        getGamesFromDatabase()
    }

    private fun getGamesFromDatabase() {
        mainScope.launch {
            val games = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }

            this@HistoryActivity.games.clear()
            this@HistoryActivity.games.addAll(games)
            this@HistoryActivity.gameAdapter.notifyDataSetChanged()
        }
    }

    private fun removeAllGames() {
        mainScope.launch {
            withContext(Dispatchers.IO){
                gameRepository.deleteAllGames()
            }

            getGamesFromDatabase()
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper{
        val callback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val productToDelete = games[position]

                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        gameRepository.deleteGame(productToDelete)
                        getGamesFromDatabase()
                    }
                }
            }
        }

        return ItemTouchHelper(callback)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//         Handle action bar item clicks here. The action bar will
//         automatically handle clicks on the Home/Up button, so long
//         as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.menuItem_delete_all -> {
                removeAllGames()
                return true
            }
            R.id.home -> {
                this.finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}