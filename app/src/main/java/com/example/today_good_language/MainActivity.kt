package com.example.today_good_language

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private val viewPager : ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }
    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progressBar)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initData()
    }
    private fun initViews(){
        viewPager.setPageTransformer { page, position ->
            when{
                position.absoluteValue >= 1.0F ->{// absoluteValue <- 절대값
                    page.alpha = 0F
                }
                position == 0F -> {
                    page.alpha = 1F
                }
                else -> {
                    page.alpha = 1F - 2 * position.absoluteValue
                }
            }
        }
    }
    private fun initData(){
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync( // 비동기작동
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            progressBar.visibility = View.GONE
            if (it.isSuccessful){
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))// key 값은 firebase안에 등록한 key값
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed") // 이하 동문
                displayQuotesPager(quotes,isNameRevealed)

            }
        }
    }
    private fun parseQuotesJson(json:String) : List<Quote>{
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()

        for (index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }
        return  jsonList.map {
            Quote(
                quote = it.getString("quote"),
                name = it.getString("name")
            )
        }
    }
    private fun displayQuotesPager(quotes:List<Quote>, isNameRevealed :Boolean){
        val adapter = QuotePagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameRevealed
        )
        viewPager.adapter = adapter
        viewPager.setCurrentItem(adapter.itemCount/2 ,false)
    }
}