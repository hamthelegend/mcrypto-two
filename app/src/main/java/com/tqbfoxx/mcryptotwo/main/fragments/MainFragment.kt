package com.tqbfoxx.mcryptotwo.main.fragments


import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonSyntaxException
import com.tqbfoxx.mcryptotwo.R
import com.tqbfoxx.mcryptotwo.databinding.FragmentMainBinding
import com.tqbfoxx.mcryptotwo.encryption.ciphers
import com.tqbfoxx.mcryptotwo.encryption.decrypt
import com.tqbfoxx.mcryptotwo.encryption.encrypt
import com.tqbfoxx.mcryptotwo.encryption.isEncrypted
import com.tqbfoxx.mcryptotwo.extensions.copy
import com.tqbfoxx.mcryptotwo.extensions.setTooltipCompat
import com.tqbfoxx.mcryptotwo.extensions.textString
import com.tqbfoxx.mcryptotwo.main.MainActivity
import com.tqbfoxx.mcryptotwo.main.adapters.Message
import com.tqbfoxx.mcryptotwo.main.adapters.MessageRecyclerAdapter
import org.jetbrains.anko.support.v4.toast

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {
	
	// Binding Object
	private lateinit var binding: FragmentMainBinding
	
	// Other Objects
	private lateinit var clipboardManager: ClipboardManager
	private lateinit var messages: ArrayList<Message>
	
	lateinit var mainActivity: MainActivity
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		
		// Inflate the layout for this fragment
		binding = FragmentMainBinding.inflate(inflater, container, false)
		
		// sets the objects up
		initObjects()
		
		
		mainActivity.binding.apply {
			
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
		mainActivity.binding.apply {
			
			navDrawerButton.setTooltipCompat("Open navigation drawer")
			pasteButton.setTooltipCompat("Paste")
			encryptButton.setTooltipCompat("Encrypt")
			
		}
		
		return binding.root
		
	}
	
	override fun onResume() {
		super.onResume()
		checkIfCipherIsAvaible()
		mainActivity.showBottomAppBar()
		mainActivity.hideFAB()
	}
	
	fun checkIfCipherIsAvaible() {
		context?.apply {
			
			try {
				ciphers
			} catch (e: JsonSyntaxException) {
				ciphers = ArrayList()
			}
			
			if (ciphers.isEmpty()) {
				this@MainFragment.findNavController()
					.navigate(MainFragmentDirections.actionMainFragmentToAddCipherFragment())
				toast("You need a cipher to use Mcrypto.")
			}
		}
	}
	
	
	/**
	 * Initializes the objects needed in the activity.
	 */
	private fun initObjects() {
		// Other Objects
		clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		
		initMessagesRecycler()
		
		mainActivity = activity as MainActivity
	}
	
	/**
	 * Sets up the messagesRecycler
	 */
	private fun initMessagesRecycler() {
		messages = ArrayList()
		binding.messagesRecycler.adapter =
			MessageRecyclerAdapter(
				context!!,
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
		navDrawerFragment.show(fragmentManager!!, navDrawerFragment.tag)
	}
	
	/**
	 * Paste the String directly to the input field
	 */
	private fun String.basicPaste() {
		when (this) {
			"" -> toast(getString(R.string.empty_clipboard))
			else -> mainActivity.binding.inputField.textString = this
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
		val encryptedMessage = context!!.encrypt(this)
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
		val decryptedMessage = context!!.decrypt(this)
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
		
		(activity as MainActivity).collapseAppBar()
		
	}
	
	
}
