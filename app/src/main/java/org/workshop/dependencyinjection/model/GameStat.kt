package org.workshop.dependencyinjection.model

import android.util.Log
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class GameStat @Inject constructor() {

    init {
        Log.d("DependencyInjection", "GameStat has been created at ${Date()}")
    }

    enum class MonsterActionType { ATTACK, DEFEND }
    data class MonsterAction(
            val turn: Int,
            val monster: Monster,
            val actionType: MonsterActionType) {
        override fun toString(): String {
            return "Turn $turn : " + when(actionType) {
                MonsterActionType.ATTACK -> {
                    "${monster.name} has attacking to the opponent."
                }
                MonsterActionType.DEFEND -> {
                    "${monster.name} has defending from the opponent."
                }
            }
        }
    }

    private val actionList: ArrayList<MonsterAction> = arrayListOf()
    var player1WinCount = 0; private set
    fun increasePlayer1WinCount() { player1WinCount++ }
    var player2WinCount = 0; private set
    fun increasePlayer2WinCount() { player2WinCount++ }


    fun addMonsterAction(monsterAction: MonsterAction) {
        actionList.add(monsterAction)
    }

    fun reset() {
        Log.d("DependencyInjection", "GameStat has been reset.")
        actionList.clear()
    }

    override fun toString(): String {
        var result = ">>> Player 1 vs Player 2 <<<"
        actionList.forEach {
            result += "\n$it"
        }
        return result
    }
}