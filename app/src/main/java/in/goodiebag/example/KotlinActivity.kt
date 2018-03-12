package `in`.goodiebag.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        Log.d("slim", sum(1, 2).toString())
    }

    fun sum(a: Int, b: Int): Int {
        return a + b
    }
}
