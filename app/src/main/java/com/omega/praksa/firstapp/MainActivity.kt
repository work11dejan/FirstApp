package com.omega.praksa.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    internal var score =0

    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView : TextView

    internal lateinit var tapMeButton : Button
    internal var    gameStarted = false

    internal lateinit var newButton:Button
    internal lateinit var btDejan:Button

    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown : Long = 60000
    internal val countDownInterval : Long = 1000
    internal var timeLeftOnTimer : Long = 60000


        companion object {
            private val TAG = MainActivity::class.java.simpleName
            private const val SCORE_KEY = "SCORE KEY "
            private const val TIME_KEY_LEFT = "TIME KEY LEFT"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called . Score is : $score")
        //Log.i(TAG, "onCreate called . Score is : $score")

        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreTextView = findViewById(R.id.gameScoreTextView)
        timeLeftTextView = findViewById(R.id.timeLeftTextView)
        newButton = findViewById<Button>(R.id.newButton)
        btDejan = findViewById(R.id.btDejan)

        tapMeButton.setOnClickListener { view ->
            bounce(view)
            incrementScore()
        }
        newButton.setOnClickListener{view ->
            zoomIn(btDejan)

        }

        if (savedInstanceState!=null){
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_KEY_LEFT)
            restoreGame()
        }else{
            resetGame()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionAbout -> showInfo()
            R.id.actionBounce -> bounce(tapMeButton)
            R.id.actionBlink -> blink(tapMeButton)
        }
        return true
    }


    private fun zoomIn(view: View){
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.newanim)
        view.startAnimation(zoomIn)
    }

    private fun blink(view: View) {
        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        view.startAnimation(blinkAnimation)
    }


    private fun bounce(view: View){
        val bounceAnimation = AnimationUtils.loadAnimation(this,R.anim.bounce)
        view.startAnimation(bounceAnimation)
    }

    private fun showInfo(){
        val dialogTitle = "Time Fighter"
        val dialogMessage = "Created by Argos The Beast Owner"

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }
        //var konj = findViewById<Button>(R.id.btDejan)
        //konj.text = "Dorat"

        override fun onSaveInstanceState(paket: Bundle) {
            super.onSaveInstanceState(paket)

            paket.putInt(SCORE_KEY, score)
            paket.putLong(TIME_KEY_LEFT, timeLeftOnTimer)
            countDownTimer.cancel()

            Log.d(TAG, "onSaveInstanceState: Saving score : $score & Time left : $timeLeftOnTimer")
        }

        override fun onDestroy() {
            super.onDestroy()
            Log.d(TAG, "onDestroy called")
        }


    private fun resetGame(){
        score =0
        gameScoreTextView.text = getString(R.string.yourScore,score)

        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text=getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval){
            override fun onTick(millishUntilFinished : Long){
                timeLeftOnTimer = millishUntilFinished
                val timeLeft = millishUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }
            override fun onFinish(){
                endGame()
            }
        }

            gameStarted = false
    }

    private fun restoreGame(){
        gameScoreTextView.text = getString(R.string.yourScore,score)

        val restoredTime = timeLeftOnTimer / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, restoredTime)

        countDownTimer = object : CountDownTimer(timeLeftOnTimer, countDownInterval){
            override fun onTick(millisUnitFinished: Long){
                timeLeftOnTimer = millisUnitFinished
                val timeLeft = millisUnitFinished / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true
    }

    private fun incrementScore() {
        if(!gameStarted){
            startGame()
        }

       score+=1
        val newScore = getString(R.string.yourScore, score)
        gameScoreTextView.text = newScore

       blink(gameScoreTextView)
    }





    private fun startGame(){
    countDownTimer.start()
        gameStarted = true


    }

    private fun endGame(){
        Toast.makeText(this, getString(R.string.gameOverMessage, score), Toast.LENGTH_LONG).show()
        resetGame()

    }
}