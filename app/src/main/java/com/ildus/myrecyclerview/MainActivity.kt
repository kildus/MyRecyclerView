package com.ildus.myrecyclerview

import android.os.Bundle
import android.telecom.Call
import android.text.Html
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.net.URL


const val TAG = "myTag"

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var putTextButton: Button

    private lateinit var textViewFound: TextView
    private lateinit var textViewNotFound: TextView
    private lateinit var textViewSurplus: TextView

    private lateinit var recyclerView: RecyclerView
    val p1 = Pack().apply {
        isFound = true
    }

//    private val packViewModel by lazy { ViewModelProviders.of(this).get(PackViewModel::class.java) }

    private val packViewModel by lazy { ViewModelProvider(this).get(PackViewModel::class.java) }

//    var packViewModel:PackViewModel? = null

    var myLiveData:MutableLiveData<Pack> = MutableLiveData()

    private val client = OkHttpClient()

    val URL = "https://api.icndb.com/jokes/random"
    var okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        Log.d(TAG, "onCreate")


        lifecycle.addObserver(MyLifecycle())


        editText = findViewById(R.id.editTextView)
        putTextButton = findViewById(R.id.putTextButton)

        textViewFound = findViewById(R.id.textViewFound)
        textViewNotFound = findViewById(R.id.textViewNotFound)
        textViewSurplus = findViewById(R.id.textViewSurplus)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

//        if (savedInstanceState != null) {
//            savedInstanceState.getSerializable("packs")?.let {
//                packs = it as MutableList<Pack>
//            }
//        }

        if (packViewModel.packs.isNullOrEmpty()) fillPacks()

        recyclerView.adapter = PackRecyclerAdapter(packViewModel.packs)

        showMainInfo()

//        packViewModel.myData.value = "ildus"
        packViewModel.myData.observe(this, Observer {
            it?.let {
                Log.d(TAG, "observe")
            }
        })

//        myLiveData = DataController().myLiveData

        myLiveData.observe(this, Observer {
            it?.let {
                Log.d(TAG, "DataController observe")
            }
        })

        myLiveData.value = Pack()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

//        outState.putSerializable("packs", packs as Serializable)
    }

    private fun fillPacks() {
        for (i in 1..50) {
            packViewModel.packs.add(Pack("$i", false, false))
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d(TAG, "onStart")

        editText.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    handleEditText()
//                    Log.d(TAG, "IME_ACTION_SEND")
                    true
                }
                else -> false
            }
        }

        putTextButton.setOnClickListener {
            handleEditText()
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    fun handleEditText() {
        if (editText.text.isNotEmpty()) {

            val text = editText.text.toString().trim()
            editText.setText("")

            for (pack in packViewModel.packs) {
                if (pack.code == text) {

                    if (pack.isSurplus || pack.isFound) {
                        Toast.makeText(this, getString(R.string.already_added), Toast.LENGTH_SHORT)
                            .show()
                        showMainInfo()
                        return
                    }

                    pack.isFound = true
//                        recyclerView.adapter?.notifyItemChanged(packs.size - 1)
                    recyclerView.adapter?.notifyDataSetChanged()

                    Toast.makeText(this, getString(R.string.is_found), Toast.LENGTH_SHORT).show()
                    showMainInfo()
                    return
                }
            }

            packViewModel.packs.add(Pack(text, false, true))
            recyclerView.adapter?.notifyItemChanged(packViewModel.packs.size - 1)
            Toast.makeText(this, getString(R.string.is_surplus), Toast.LENGTH_SHORT).show()
            showMainInfo()
        }
    }

    fun showMainInfo() {
        var found = 0
        var notFound = 0
        var surplus = 0

        for (pack in packViewModel.packs) {
            if (pack.isSurplus) surplus++ else if (pack.isFound) found++ else notFound++
        }

        textViewFound.text = found.toString()
        textViewNotFound.text = notFound.toString()
        textViewSurplus.text = surplus.toString()

    }

    fun onClickNotFound(view: View) {

//        runTest()

        loadRandomFact()

//        liveData?.setValue("new value")
        packViewModel.myData.value = "ild"

//        myLiveData.value?.isFound = true



        myLiveData.value = myLiveData.value?.apply {
            isFound = true
        }
    }

    fun runTest() {
        val request = Request.Builder()
            .url("https://publicobject.com/helloworld.txt")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            for ((name, value) in response.headers) {
                println("$name: $value")
            }

            println(response.body!!.string())
        }
    }

    private fun loadRandomFact() {
        runOnUiThread {
//            progressBar.visibility = View.VISIBLE
        }

        val request: Request = Request.Builder().url(URL).build()
        okHttpClient.newCall(request).enqueue(object: Callback {

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e(TAG, "Failed to fetch photos", e)
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                val json = response.body?.string()

                Log.d(TAG, "body: $json")

                val txt = (JSONObject(json).getJSONObject("value").get("joke")).toString()


                runOnUiThread {
                    Toast.makeText(this@MainActivity, "" + txt, Toast.LENGTH_SHORT).show()
                }

                Log.d(TAG, "onResponse: $txt")
            }
        })

    }

}
