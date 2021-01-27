package org.workshop.dependencyinjection.game

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.lifecycle.LiveData
import dagger.hilt.android.AndroidEntryPoint
import org.workshop.dependencyinjection.databinding.ActivityMainBinding
import org.workshop.dependencyinjection.databinding.MonsterLayoutBinding
import org.workshop.dependencyinjection.model.GameStat
import org.workshop.dependencyinjection.model.Monster
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityMainBinding

    // ViewModel
//    @Inject lateinit var viewModel: MainViewModel

    // GameStat
    @Inject lateinit var gameStat: GameStat

    // Bound Service - each element
    private lateinit var pokemonService: PokemonService
    private var isBound = false
    private val connection = object: ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as PokemonService.PokemonBinder
            pokemonService = binder.getService()
            isBound = true

            observeCurrentGameLiveData()
            observeTurnLiveData()
            observePlayerTurnLiveData()
            observeDescriptionLiveData()
            observePlayerWinCountLiveData()
            observeMonsterDetailLiveData(pokemonService.monster1LiveData, binding.monster1Layout)
            observeMonsterDetailLiveData(pokemonService.monster2LiveData, binding.monster2Layout)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false

            pokemonService.run {
                pokemonService.currentJobLiveData.removeObservers(this@MainActivity)
                gameTurnLiveData.removeObservers(this@MainActivity)
                gamePlayerLiveData.removeObservers(this@MainActivity)
                gameDescriptionLiveData.removeObservers(this@MainActivity)
                player1WinCountLiveData.removeObservers(this@MainActivity)
                player2WinCountLiveData.removeObservers(this@MainActivity)
                monster1LiveData.removeObservers(this@MainActivity)
                monster2LiveData.removeObservers(this@MainActivity)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleStartButton()
        registerPokemonService()
    }

    ///////////////////
    // Helper method //
    ///////////////////

    private fun registerPokemonService() {
        // Bind to CountdownService
        Intent(this, PokemonService::class.java).also { intent ->
            // Start a service as the STARTED SERVICE (to make it still run even though this activity has destroyed)
            startService(intent)

            // Bind the service to this Activity
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun handleStartButton() {
        binding.startButton.setOnClickListener {
            if (isBound) {
                if (!pokemonService.isPlaying) {
                    pokemonService.startGame(this, gameStat)
                } else {
                    pokemonService.cancelGame()
                }
            }
        }
    }

    private fun observeCurrentGameLiveData() {
        pokemonService.currentJobLiveData.observe(this) { job ->
            if (job == null) {
                binding.apply {
                    gameDescriptionTextView.visibility = View.INVISIBLE
                    textBox.visibility = View.INVISIBLE
                    startButton.text = "Start"
                }
            } else {
                binding.apply {
                    gameDescriptionTextView.visibility = View.VISIBLE
                    textBox.visibility = View.VISIBLE
                    startButton.text = "Stop"
                }
            }
        }
    }

    private fun observeTurnLiveData() {
        pokemonService.gameTurnLiveData.observe(this) {
            binding.turnTextView.text = it
        }
    }

    private fun observePlayerTurnLiveData() {
        pokemonService.gamePlayerLiveData.observe(this) {
            binding.nowPlayerTextView.text = it
        }
    }

    private fun observeDescriptionLiveData() {
        pokemonService.gameDescriptionLiveData.observe(this) {
            binding.gameDescriptionTextView.text = it
        }
    }

    private fun observePlayerWinCountLiveData() {
        // update for the first time
        pokemonService.updateWinCount(gameStat)

        pokemonService.player1WinCountLiveData.observe(this) {
            it?.let { binding.player1WinCountTextView.text = it.toString() }
        }

        pokemonService.player2WinCountLiveData.observe(this) {
            it?.let { binding.player2WinCountTextView.text = it.toString() }
        }
    }

    private fun observeMonsterDetailLiveData(
        monsterLiveData: LiveData<Monster>,
        monsterLayoutBinding: MonsterLayoutBinding
    ) {
        monsterLiveData.observe(this) {
            if (it != null) {
                monsterLayoutBinding.root.visibility = View.VISIBLE
                monsterLayoutBinding.apply {
                    monsterNameTextView.text = it.name
                    monsterHpTextView.text = "HP: ${it.hp.toInt()}"
                    monsterAtkTextView.text = "ATK : ${it.totalAtk.toInt()}"
                    monsterDefTextView.text = "DEF : ${it.totalDef.toInt()}"
                    monsterElementTextView.text = "Element : ${it.weapon.element.name}"
                    monsterStateTextView.text = "State : ${it.state.name}"
                }
            } else {
                monsterLayoutBinding.root.visibility = View.INVISIBLE
            }
        }
    }
}