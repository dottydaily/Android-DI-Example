package org.workshop.dependencyinjection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import org.workshop.dependencyinjection.databinding.ActivityMainBinding
import org.workshop.dependencyinjection.databinding.MonsterLayoutBinding
import org.workshop.dependencyinjection.model.Monster

class MainActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityMainBinding

    // ViewModel
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleStartButton()
        observeTurnLiveData()
        observePlayerTurnLiveData()
        observeDescriptionLiveData()

        observeMonsterDetailLiveData(viewModel.monster1LiveData, binding.monster1Layout)
        observeMonsterDetailLiveData(viewModel.monster2LiveData, binding.monster2Layout)
    }

    ///////////////////
    // Helper method //
    ///////////////////

    private fun handleStartButton() {
        binding.startButton.setOnClickListener {
            if (!viewModel.isPlaying) {
                viewModel.startGame()
            } else {
                viewModel.stopGame()
            }
        }
        viewModel.currentJobLiveData.observe(this) { job ->
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
        viewModel.gameTurnLiveData.observe(this) {
            binding.turnTextView.text = it
        }
    }

    private fun observePlayerTurnLiveData() {
        viewModel.gamePlayerLiveData.observe(this) {
            binding.nowPlayerTextView.text = it
        }
    }

    private fun observeDescriptionLiveData() {
        viewModel.gameDescriptionLiveData.observe(this) {
            binding.gameDescriptionTextView.text = it
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