package com.tech51.concat_me

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    process(finshCount = {
                        runOnUiThread {
                            finish.text = it
                        }
                    }, totalFile = {
                        runOnUiThread {
                            show.text = it
                        }
                    }, errorCount = {
                        runOnUiThread {
                            tvCount.text = it
                        }
                    }, error = {
                        runOnUiThread {
                            tvMessage.text = it
                        }
                    } , finish = {
                        runOnUiThread {
                            finish.text = "You've done con con !!"
                        }
                    })
                }


        }
    }

       suspend fun process (finshCount : (String) -> Unit ,finish :() -> Unit , totalFile:  (String) -> Unit ,errorCount :(String)->Unit, error : (String) -> Unit ) {
        val myanmar = "/sdcard/Android/obb/myanmar/"
        val arbic = "/sdcard/Android/obb/arbic/"
        val mFile = File(myanmar)
        val aFile = File(arbic)
        val mList = mFile.listFiles()!!
        val aList = aFile.listFiles()!!
           mList.sort()
           aList.sort()
        var holder = ""
           finshCount.invoke("Processing start please wait zzzzz :D")
        for (i in 0..mList.size - 1){
            holder += "${mList.get(i).name } , ${aList.get(i).name} \n\n\n"
        }
         totalFile.invoke(holder)

        var e1 = 0
        for(i in 0..mList.size - 1) {
            try {
                val fis1 = FileInputStream("/sdcard/Android/obb/myanmar/${mList.get(i).name}")
                val fis2 = FileInputStream("/sdcard/Android/obb/arbic/${aList.get(i).name}")
                val sis = SequenceInputStream(fis1, fis2)
                val fos = FileOutputStream(File("/sdcard/Android/obb/concat/am${mList.get(i).name.substring(1)}"))

                var temp: Int
                try {
                    temp = sis.read()
                    while ((temp) != -1) {

                        fos.write(temp)
                        temp = sis.read()
                    }
                    //increment ------
                    finshCount.invoke("You finish ${i+1}")
                } catch (e: IOException) {
                    Log.d("io", e.message)
                    e.printStackTrace()
                    error.invoke(e.message!!)
                    e1++
                    errorCount.invoke("At file number ${i} found error")

                }
        } catch (e: FileNotFoundException) {
                Log.d("file not found", e.message)
                e.printStackTrace()
                error.invoke(e.message!!)
                e1++
                errorCount.invoke("At file number ${i} found error")
            }

        }
           finish.invoke()


    }
}

//Android/obb/myanmar
//Android/obb/arbic
//Android/obb/concat
