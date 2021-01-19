package org.workshop.dependencyinjection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.*
import org.workshop.dependencyinjection.hilt.MonsterLizardon
import org.workshop.dependencyinjection.hilt.MonsterPikachu
import org.workshop.dependencyinjection.model.GameStat
import org.workshop.dependencyinjection.model.Monster
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.random.Random

@ActivityScoped
class MainViewModel @Inject constructor(): ViewModel() {

    init {
        Log.d("DependencyInjection", "ViewModel has been created at ${Date()}")
    }

    // LiveData
    private val _monster1LiveData = MutableLiveData<Monster>().apply { value = null }
    val monster1LiveData: LiveData<Monster> get() = _monster1LiveData

    private val _monster2LiveData = MutableLiveData<Monster>().apply { value = null }
    val monster2LiveData: LiveData<Monster> get() = _monster2LiveData

    private val _gameTurnLiveData = MutableLiveData<String>().apply { value = null }
    val gameTurnLiveData: LiveData<String> get() = _gameTurnLiveData

    private val _gamePlayerLiveData = MutableLiveData<String>().apply { value = null }
    val gamePlayerLiveData: LiveData<String> get() = _gamePlayerLiveData

    private val _gameDescriptionLiveData = MutableLiveData<String>().apply { value = null }
    val gameDescriptionLiveData: LiveData<String> get() = _gameDescriptionLiveData

    private val _player1WinCountLiveData = MutableLiveData<Int>().apply { value = null }
    val player1WinCountLiveData: LiveData<Int> get() = _player1WinCountLiveData

    private val _player2WinCountLiveData = MutableLiveData<Int>().apply { value = null }
    val player2WinCountLiveData: LiveData<Int> get() = _player2WinCountLiveData

    private val _currentJobLiveData = MutableLiveData<Job>().apply { value = null }
    val currentJobLiveData: LiveData<Job>
        get() = _currentJobLiveData
    val isPlaying get() = currentJobLiveData.value != null

    // Dependency Injection
    @MonsterPikachu
    @Inject
    lateinit var monster1Provider: Provider<Monster>

    @MonsterLizardon
    @Inject
    lateinit var monster2Provider: Provider<Monster>

    fun startGame(gameStat: GameStat) {
        clearData()

        // Background Thread
        _currentJobLiveData.postValue(
            viewModelScope.launch(Dispatchers.IO) {
                // Inject monster 1
                val monster1 = monster1Provider.get()
                _monster1LiveData.postValue(monster1)

                // Inject monster 2
                val monster2 = monster2Provider.get()
                _monster2LiveData.postValue(monster2)

                var isPlayer1Turn = true
                var turn = 1.0

                while (isActive &&
                        monster1.state != Monster.MonsterState.DEAD &&
                        monster2.state != Monster.MonsterState.DEAD) {
                    // Update turn's number and player's turn, then set delay to 2 second.
                    val turnNumber = turn.toInt()
                    _gameTurnLiveData.postValue("Turn $turnNumber")
                    _gamePlayerLiveData.postValue(
                        if (isPlayer1Turn) "${monster1.name}'s turn."
                        else "${monster2.name}'s turn."
                    )
                    delay(2000L)

                    // Random Action (Attack or Guard), then update the description text.
                    val willAttack = Random.nextBoolean()
                    if (isPlayer1Turn) {
                        _gameDescriptionLiveData.postValue("${monster1.name}'s " +
                                (if (willAttack) "attacking" else "guarding") + " the opponent.")
                    } else {
                        _gameDescriptionLiveData.postValue("${monster2.name}'s " +
                                (if (willAttack) "attacking" else "guarding") + " the opponent.")
                    }
                    delay(1000L)

                    // Do the action and update the monster's detail.
                    val actionType = if (willAttack) GameStat.MonsterActionType.ATTACK
                                        else GameStat.MonsterActionType.DEFEND

                    // Create MonsterAction to save the action data into GameStat's object.
                    var monsterAction: GameStat.MonsterAction? = null

                    var description: String? = null
                    if (isPlayer1Turn) {
                        if (actionType == GameStat.MonsterActionType.ATTACK) {
                            description = monster1.attack(monster2)
                        } else {
                            monster1.defend()
                        }

                        monsterAction = GameStat.MonsterAction(turnNumber, monster1, actionType)
                    } else {
                        if (actionType == GameStat.MonsterActionType.ATTACK) {
                            description = monster2.attack(monster1)
                        } else {
                            monster2.defend()
                        }

                        monsterAction = GameStat.MonsterAction(turnNumber, monster2, actionType)
                    }

                    // Update MonsterAction to GameStat's object.
                    gameStat.addMonsterAction(monsterAction)

                    // Update description.
                    description?.let { _gameDescriptionLiveData.postValue(it) }

                    // Update each monsters' detail.
                    _monster1LiveData.postValue(monster1)
                    _monster2LiveData.postValue(monster2)
                    delay(3000L)

                    // Prepare data for the next turn.
                    isPlayer1Turn = !isPlayer1Turn
                    turn += 0.5
                    _gameDescriptionLiveData.postValue("")
                    if (monster1.isDead || monster2.isDead) {
                        this.cancel()
                    }
                }

                val winner = if (monster1.isDead) {
                    gameStat.increasePlayer2WinCount()
                    monster2.name
                } else {
                    gameStat.increasePlayer1WinCount()
                    monster1.name
                }
                _gameTurnLiveData.postValue("$winner wins!")

                stopGame(gameStat)
            }
        )
    }

    fun stopGame(gameStat: GameStat) {
        Log.d("DependencyInjection", gameStat.toString())
        updateWinCount(gameStat)
        gameStat.reset()
        _currentJobLiveData.value?.cancel()
        _currentJobLiveData.postValue(null)
    }

    fun clearData() {
        _monster1LiveData.postValue(null)
        _monster2LiveData.postValue(null)
        _gameTurnLiveData.postValue(null)
        _gamePlayerLiveData.postValue(null)
        _gameDescriptionLiveData.postValue(null)
    }

    fun updateWinCount(gameStat: GameStat) {
        // Update each players' win count.
        _player1WinCountLiveData.postValue(gameStat.player1WinCount)
        _player2WinCountLiveData.postValue(gameStat.player2WinCount)
    }
}