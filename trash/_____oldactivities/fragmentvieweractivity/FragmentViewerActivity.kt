package com.tqbfoxx.mcryptotwo._____oldactivities.fragmentvieweractivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tqbfoxx.mcryptotwo.R
import com.tqbfoxx.mcryptotwo.main.fragments.AddCipherFragment
import com.tqbfoxx.mcryptotwo.databinding.ActivityFragmentViewerBinding
import com.tqbfoxx.mcryptotwo.extensions.setBindingContentView

class FragmentViewerActivity : AppCompatActivity() {
	
	lateinit var binding: ActivityFragmentViewerBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = setBindingContentView(R.layout.activity_fragment_viewer)
		
		val fragmentString = intent.getStringExtra("fragmentString")
		
		val fragmentTransaction = supportFragmentManager.beginTransaction()
		
		fragmentTransaction.replace(
			R.id.fragment_view,
			when (fragmentString) {
				AddCipherFragment::class.java.toString() -> AddCipherFragment()
				else -> MissingFragment()
			}
		).commit()
		
	}
}
