package com.tqbfoxx.mcryptotwo.main.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.tqbfoxx.mcryptotwo.R
import com.tqbfoxx.mcryptotwo.databinding.FragmentAddCipherBinding
import com.tqbfoxx.mcryptotwo.databinding.FragmentCiphersBinding
import com.tqbfoxx.mcryptotwo.databinding.FragmentEditCipherBinding
import com.tqbfoxx.mcryptotwo.encryption.CipherX
import com.tqbfoxx.mcryptotwo.encryption.activeCipher
import com.tqbfoxx.mcryptotwo.encryption.ciphers
import com.tqbfoxx.mcryptotwo.extensions.textString
import com.tqbfoxx.mcryptotwo.main.MainActivity
import com.tqbfoxx.mcryptotwo.main.adapters.CipherRecyclerAdapter
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

/**
 * A simple [Fragment] subclass.
 */
class CiphersFragment : Fragment() {
	
	lateinit var mainActivity: MainActivity
	
	lateinit var binding: com.tqbfoxx.mcryptotwo.databinding.FragmentCiphersBinding
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentCiphersBinding.inflate(inflater, container, false)
		
		mainActivity = activity as MainActivity
		
		binding.cipherRecycler.adapter =
			CipherRecyclerAdapter(context!!, context!!.ciphers)
		
		mainActivity.apply {
			
			hideBottomAppBar()
			showFAB()
			
			binding.fab.setOnClickListener{
				this.findNavController(R.id.fragment_host)
					.navigate(CiphersFragmentDirections.actionCiphersFragmentToAddCipherFragment())
			}
			
		}
		
		return binding.root
		
	}
	
	
}

class EditCipherFragment : Fragment() {
	
	lateinit var binding: FragmentEditCipherBinding
	
	lateinit var mainActivity: MainActivity
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentEditCipherBinding.inflate(inflater, container, false)
		
		mainActivity = activity as MainActivity
		
		val cipher = EditCipherFragmentArgs.fromBundle(
			arguments!!
		).cipher
		
		mainActivity.hideFAB()
		
		binding.apply {
			
			nameField.textString = cipher.name
			keyValueField.textString = cipher.keyValue
			
		}
		
		return binding.root
	}
	
}

class AddCipherFragment : Fragment() {
	
	lateinit var binding: FragmentAddCipherBinding
	
	lateinit var mainActivity: MainActivity
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentAddCipherBinding.inflate(inflater, container, false)
		
		mainActivity = activity as MainActivity
		
		mainActivity.hideBottomAppBar()
		
		mainActivity.hideFAB()
		
		context?.apply {
			
			binding.apply {
				
				saveAndSetButton.setOnClickListener { view ->
					if (nameField.textString.isNotBlank()) {
						if (keyValueField.textString.length == 16) {
							val name = nameField.textString
							val keyValue = keyValueField.textString
							val index = ciphers.size
							val newCipher = CipherX(name, keyValue, index)
							ciphers = (ciphers + newCipher) as ArrayList<CipherX>
							view.findNavController().navigateUp()
							
							activeCipher = newCipher
							
						} else {
							longToast("Key value must be exactly 16 characters long.")
						}
					} else {
						toast("Name shouldn't be blank.")
					}
				}
				
			}
			
		}
		
		return binding.root
	}
	
}