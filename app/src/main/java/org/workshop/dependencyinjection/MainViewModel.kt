package org.workshop.dependencyinjection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import org.workshop.dependencyinjection.model.Armor
import org.workshop.dependencyinjection.model.Monster
import org.workshop.dependencyinjection.model.Weapon
import kotlin.random.Random

class MainViewModel: ViewModel() {

    // LiveData
    private val _monster1LiveData = MutableLiveData<Monster>().apply { value = null }
    val monster1LiveData: LiveData<Monster> get() = _monster1LiveData
    fun updateMonster1() { _monster1LiveData.postValue(_monster1LiveData.value) }

    private val _monster2LiveData = MutableLiveData<Monster>().apply { value = null }
    val monster2LiveData: LiveData<Monster> get() = _monster2LiveData
    fun updateMonster2() { _monster2LiveData.postValue(_monster2LiveData.value) }

    private val _gameTurnLiveData = MutableLiveData<String>().apply { value = null }
    val gameTurnLiveData: LiveData<String> get() = _gameTurnLiveData

    private val _gamePlayerLiveData = MutableLiveData<String>().apply { value = null }
    val gamePlayerLiveData: LiveData<String> get() = _gamePlayerLiveData

    private val _gameDescriptionLiveData = MutableLiveData<String>().apply { value = null }
    val gameDescriptionLiveData: LiveData<String> get() = _gameDescriptionLiveData

    private val _currentJobLiveData = MutableLiveData<Job>().apply { value = null }
    val currentJobLiveData: LiveData<Job>
        get() = _currentJobLiveData
    val isPlaying get() = currentJobLiveData.value != null

    fun startGame() {
        // Background Thread
        _currentJobLiveData.postValue(
            viewModelScope.launch(Dispatchers.IO) {
                // Create Weapon and Armor for each monsters.
                val weapon1 = Weapon()
                val armor1 = Armor()
                val monster1 = Monster("Pikachu", 200.0, 50.0, 2000.0, weapon1, armor1)
                _monster1LiveData.postValue(monster1)

                val weapon2 = Weapon()
                val armor2 = Armor()
                val monster2 = Monster("Lizardon", 100.0, 100.0, 2000.0, weapon2, armor2)
                _monster2LiveData.postValue(monster2)

                var isPlayer1Turn = true
                var turn = 1.0

                while (isActive &&
                        monster1.state != Monster.MonsterState.DEAD &&
                        monster2.state != Monster.MonsterState.DEAD) {
                    // Update turn's number and player's turn, then set delay to 2 second.
                    _gameTurnLiveData.postValue("Turn ${turn.toInt()}")
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
                    delay(2000L)

                    // Do the action and update the monster's detail.
                    var description: String? = null
                    if (isPlayer1Turn) {
                        if (willAttack) {
                            description = monster1.attack(monster2)
                            monster1.state = Monster.MonsterState.NORMAL
                        } else {
                            monster1.state = Monster.MonsterState.GUARD
                        }
                    } else {
                        if (willAttack) {
                            description = monster2.attack(monster1)
                            monster2.state = Monster.MonsterState.NORMAL
                        } else {
                            monster2.state = Monster.MonsterState.GUARD
                        }
                    }
                    description?.let { _gameDescriptionLiveData.postValue(it) }
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

                stopGame()
            }
        )
    }

    fun stopGame() {
        _monster1LiveData.postValue(null)
        _monster2LiveData.postValue(null)
        _gameTurnLiveData.postValue(null)
        _gamePlayerLiveData.postValue(null)
        _gameDescriptionLiveData.postValue(null)

        currentJobLiveData.value?.cancel()
        _currentJobLiveData.postValue(null)
    }
}