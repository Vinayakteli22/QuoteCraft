package com.example.inspireverse

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inspireverse.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        //for geting quote
        getQuote()

        binding.next.setOnClickListener {
            getQuote()
        }
        binding.share.setOnClickListener {
            val quote = binding.quote.text.toString()
            val author = binding.author.text.toString()

            if (quote.isNotBlank() && author.isNotBlank()) {
                val shareText = "\"$quote\"\n- $author"
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                startActivity(Intent.createChooser(shareIntent, "Share Quote Via"))
            } else {
                Toast.makeText(this, "No quote to share!", Toast.LENGTH_SHORT).show()
            }
        }


    }
    //GetQuote function
    private fun getQuote() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Network error! Please check your internet connection.", Toast.LENGTH_SHORT).show()
            setInProgress(false) // Ensure UI components reflect the state properly
            return
        }


        setInProgress(true)
        GlobalScope.launch {
            try {
                val response = RetrofitInstance.quoteApi.getRandomQuote()

                runOnUiThread {
                    setInProgress(false)
                    response.body()?.first()?.let{
                        displayQuote(it)
                    }
                }


            } catch (E: Exception) {
            }

        }
    }
    // for displaying the quote in UI
    private fun displayQuote(quote:QuoteModel){
        binding.quote.text=quote.q
        binding.author.text=quote.a
    }
//for setting progress bar
    private fun setInProgress(inProgress:Boolean){
        if(inProgress){
            binding.fetchprogress.visibility= View.VISIBLE
            binding.next.visibility=View.GONE
        }else{
            binding.fetchprogress.visibility= View.GONE
            binding.next.visibility=View.VISIBLE


        }
    }

    //fun for checking the internet

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}
