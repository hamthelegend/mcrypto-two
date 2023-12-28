package com.tqbfoxx.mcryptotwo._____oldactivities.mainactivity

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonSyntaxException
import com.tqbfoxx.mcryptotwo.R
import com.tqbfoxx.mcryptotwo.main.fragments.AddCipherFragment
import com.tqbfoxx.mcryptotwo.main.adapters.Message
import com.tqbfoxx.mcryptotwo.main.adapters.MessageRecyclerAdapter
import com.tqbfoxx.mcryptotwo.main.fragments.NavigationDrawerFragment
import com.tqbfoxx.mcryptotwo.databinding.ActivityMainBinding
import com.tqbfoxx.mcryptotwo.encryption.ciphers
import com.tqbfoxx.mcryptotwo.encryption.decrypt
import com.tqbfoxx.mcryptotwo.encryption.encrypt
import com.tqbfoxx.mcryptotwo.encryption.isEncrypted
import com.tqbfoxx.mcryptotwo.extensions.*
import org.jetbrains.anko.toast

class MainActivityOld : AppCompatActivity() {
	
	// TODO: Convert this to a fragment as well. Make everything a fragment.
	
	// Binding Object
	private lateinit var binding: ActivityMainBinding
	
	// Other Objects
	private lateinit var clipboardManager: ClipboardManager
	private lateinit var messages: ArrayList<Message>
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = setBindingContentView(R.layout.activity_main_old)
		
		// sets the onClickListeners
		binding.apply {
			
			// sets the objects up
			initObjects()
			
			// show nav drawer
			// on hamburger menu icon click
			navDrawerButton.setOnClickListener {
				showNavDrawer()
			}
			
			// pastes from clipboardManager
			// on smartPaste icon click
			pasteButton.setOnClickListener {
				smartPaste()
			}
			
			// encrypts the message
			// on lock icon click
			encryptButton.setOnClickListener {
				
				// encrypts and adds the new SENT Message if the input field isn't empty
				inputField.textString.apply {
					if (this.isNotEmpty()) encryptAndAdd() else toast(getString(R.string.empty_input_field))
				}
				
				// clears the input field
				inputField.textString = ""
				
			}
			
		}
		
		// sets the tooltips
		binding.apply {
			
			navDrawerButton.setTooltipCompat("Open navigation drawer")
			pasteButton.setTooltipCompat("Paste")
			encryptButton.setTooltipCompat("Encrypt")
			
		}
		
	}
	
	override fun onResume() {
		super.onResume()
		try {
			if (ciphers.isEmpty())
				openFragmentOnNewActivity(AddCipherFragment::class.java.toString())
		} catch (e: JsonSyntaxException) {
			ciphers = ArrayList()
			if (ciphers.isEmpty())
				openFragmentOnNewActivity(AddCipherFragment::class.java.toString())
		}
	}
	
	/**
	 * Initializes the objects needed in the activity.
	 */
	private fun initObjects() {
		// Other Objects
		clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		
		initMessagesRecycler()
	}
	
	/**
	 * Sets up the messagesRecycler
	 */
	private fun initMessagesRecycler() {
		messages = ArrayList()
		binding.messagesRecycler.adapter =
			MessageRecyclerAdapter(
				this,
				messages,
				clipboardManager
			)
	}
	
	/**
	 * Shows the Navigation Drawer.
	 */
	private fun showNavDrawer() {
		val navDrawerFragment =
			NavigationDrawerFragment()
		navDrawerFragment.show(supportFragmentManager, navDrawerFragment.tag)
	}
	
	/**
	 * Paste the String directly to the input field
	 */
	private fun String.basicPaste() {
		when (this) {
			"" -> toast(getString(R.string.empty_clipboard))
			else -> binding.inputField.textString = this
		}
	}
	
	/**
	 * Gets the primary clip's text from the clipboard
	 */
	private fun getPasteData(): String {
		// gets the primary clip from the clipboardManager
		val clipData = clipboardManager.primaryClip
		val clipCount = clipData?.itemCount ?: 0
		
		// if clipData is not empty
		if (clipCount > 0) {
			// gets source text.
			val item = clipData?.getItemAt(0)
			return item?.text.toString()
		} else {
			return ""
		}
	}
	
	/**
	 * Encrypts the given String and adds the message as Message.Type.SENT
	 */
	private fun String.encryptAndAdd() {
		val encryptedMessage = this.encrypt()
		Message(
			Message.Type.SENT,
			encryptedMessage,
			this
		).addMessageToList()
		clipboardManager.copy(encryptedMessage)
		toast(getString(R.string.copied_encrypted_text))
	}
	
	/**
	 * Decrypts the given String and adds the message as Message.Type.RECEIVED
	 */
	private fun String.decryptAndAdd() {
		val decryptedMessage = this.decrypt()
		Message(
			Message.Type.RECEIVED,
			this,
			decryptedMessage
		).addMessageToList()
	}
	
	/**
	 * Decrypts the text on clipboard if it's encrypte or pastes it to the inputField if it's not.
	 */
	private fun smartPaste() {
		val pasteData = getPasteData()
		
		if (pasteData.isEncrypted()) {
			pasteData.decryptAndAdd()
		} else {
			pasteData.basicPaste()
		}
		
	}
	
	/**
	 * Adds the message to list
	 */
	private fun Message.addMessageToList() {
		// adds the message
		messages.add(this)
		
		// scrolls the ListView to the latest message
		binding.messagesRecycler.apply {
			// updates messagesRecycler
			adapter?.notifyDataSetChanged()
			post { smoothScrollToPosition(adapter?.getItemCount()?.minus(1)!!) }
		}
		
		binding.appBar.setExpanded(false)
		
	}
	
}
