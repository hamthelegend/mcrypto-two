package com.tqbfoxx.mcryptotwo._____oldactivities.ciphersactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tqbfoxx.mcryptotwo.R
import com.tqbfoxx.mcryptotwo.databinding.ActivityCiphersBinding

class CiphersActivity : AppCompatActivity() {
	
	lateinit var binding: ActivityCiphersBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_ciphers)
	}
}
