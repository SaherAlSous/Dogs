package com.saheralsous.dogs.view

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.saheralsous.dogs.R
import com.saheralsous.dogs.databinding.FragmentDetailBinding
import com.saheralsous.dogs.databinding.SendSmsDialogBinding
import com.saheralsous.dogs.model.DogBreed
import com.saheralsous.dogs.model.DogPalette
import com.saheralsous.dogs.model.SmsInfo
import com.saheralsous.dogs.util.getProgressDrawable
import com.saheralsous.dogs.util.loadImage
import com.saheralsous.dogs.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    private var dogUuid = 0
    private lateinit var viewModel: DetailViewModel
    private lateinit var dataBinding: FragmentDetailBinding

    /**
     * asking for sms permission
     */
    private var sendSmsStarted = false

    private var currentDog: DogBreed? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /**
         * enabling menu
         */
        setHasOptionsMenu(true)
        /**
         * implementing databinding in the fragment
         */
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * getting the argument from navigation
         */
        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUid
        }
        viewModel.fetch(dogUuid)

        observerViewModel()
    }

    private fun observerViewModel() {
        viewModel.dogLiveData.observe(
            viewLifecycleOwner,
            Observer { dog ->
                currentDog = dog // getting the current dog for SMS

                dog.let { it ->

                    dataBinding.dog = dog // binding the view with the observed data

                    it.imageUrl.let {
                        setupBackgroundColor(it!!) //using Palette
                        /**
                         * we can use the palette to color the text/boxes...etc
                         */
                    }
                }
            }
        )
    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val intColor = palette?.lightMutedSwatch?.rgb
                                ?: 0 //getting the value of the color or 0
                            // palette?.vibrantSwatch?.rgb <-- play for it in other apps. this one has an issue.
                            val myPalette = DogPalette(intColor) //the class
                            dataBinding.palette =
                                myPalette //attaching the palette to the layout xml.
                        }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    //nothing to do
                }

            })
    }

    /**
     * creating and setting up the menu to send sms & share content
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_sms -> { //sending sms from the device
                sendSmsStarted = true
                (activity as MainActivity).checkSmsPermission() //only main activity can ask for permission not the fragment
            }

            R.id.action_share -> { //sharing content to another applications
                val intent = Intent(Intent.ACTION_SEND) //generic flag to share info with other application
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this dog")
                intent.putExtra(Intent.EXTRA_TEXT, "${currentDog?.dogBreed} bred for ${currentDog?.bredFor}")
                intent.putExtra(Intent.EXTRA_STREAM, currentDog?.imageUrl) //recieved app may chose to implement this or not.
                startActivity(Intent.createChooser(intent, "Share with")) //pop up a dialog for the user to chose which app to handle this.
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Working after getting the permission to create / Send SMS
     */
    fun onPermissionResult(permissionGranted: Boolean) {
        if (sendSmsStarted && permissionGranted) {
            context?.let {
                val smsInfo = SmsInfo(
                    "",
                    "${currentDog?.dogBreed} bred for ${currentDog?.bredFor}",
                    currentDog?.imageUrl
                )

                val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(
                    LayoutInflater.from(it),
                    R.layout.send_sms_dialog,
                    null,
                    false
                )
                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS") {dialog, which ->
                        if (!dialogBinding.smsDestination.text.isNullOrEmpty()){
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setNegativeButton("Cancel") {dialog, which ->

                    }.show()

                dialogBinding.smsInfo = smsInfo // attaching the content of the model to the view of dialog xml
            }
        }
    }

    /**
     * sending an actual SMS through the device.
     */
    private fun sendSms(smsInfo: SmsInfo){
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(context, 0, intent,0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to, null,smsInfo.text, pi, null)
    }
}

/**
 * codes used in certain parts of the course
 */

//        buttonList.setOnClickListener {
//            val action = DetailFragmentDirections.actionListFragment()
//            Navigation.findNavController(it).navigate(action)
//        }

//                dog_name.text = dog.dogBreed
//                dog_purpose.text = dog.bredFor
//                dog_Temperament.text = dog.temperament
//                dog_lifespan.text = dog.LifeSpan
//                dog_image.loadImage(dog.imageUrl, getProgressDrawable(dog_image.context))