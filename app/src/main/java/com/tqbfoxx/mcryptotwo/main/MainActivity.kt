package com.tqbfoxx.mcryptotwo.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.tqbfoxx.mcryptotwo.R
import com.tqbfoxx.mcryptotwo.databinding.ActivityMainBinding
import com.tqbfoxx.mcryptotwo.extensions.setBindingContentView

class MainActivity : AppCompatActivity() {
	
	lateinit var binding: ActivityMainBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = setBindingContentView(R.layout.activity_main)
		
//		// TODO: remove assets folder
//		// TODO: use val typeface = ResourcesCompat.getFont(context, R.font.myfont)
//
//		val metropolisReg = Typeface.createFromAsset(assets, "fonts/quicksand_regular_regular.ttf")
//		val metropolisMed = Typeface.createFromAsset(assets, "fonts/quicksand_medium.ttf")
//
//		binding.toolbarLayout.apply {
//			setExpandedTitleTypeface(metropolisReg)
//			setCollapsedTitleTypeface(metropolisMed)
//		}
		
			NavigationUI.setupWithNavController(
				binding.toolbarLayout,
				binding.toolbar,
				this.findNavController(R.id.fragment_host)
			)
	
		/*
		 * TODO: Add backwards support by allowing encryption without the prefix and suffix
		 *  even if it'll make the paste dumber??
		 *  PROBLEM: NEEDS A DECRYPT BUTTON. (possible fix: long pressing the encrypt button decrypts
		 *  the text)
		 */
		
	}
	
	fun showBottomAppBar() {
		binding.bottomAppBar.visibility = View.VISIBLE
	}
	
	fun hideBottomAppBar() {
		binding.bottomAppBar.visibility = View.GONE
	}
	
	fun showFAB() {
		binding.fab.visibility = View.VISIBLE
	}
	
	fun hideFAB() {
		binding.fab.visibility = View.GONE
	}
	
	fun collapseAppBar() {
		binding.appBar.setExpanded(false)
	}
	
}
