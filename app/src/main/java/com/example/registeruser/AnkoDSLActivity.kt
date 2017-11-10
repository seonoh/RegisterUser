package com.example.registeruser

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.button
import org.jetbrains.anko.editText
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import org.jetbrains.anko.verticalLayout

/**
 * Created by 선오 on 2017-11-10.
 */
class AnkoDSLActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            val name = editText{
                hint = "Please input your name"
                textSize=20f
            }

            button("AnkoButton"){
                onClick { toast("Hello Anko user, ${name.text}!!!") }
            }
        }

        //  setContentView(R.layout.activity_main) //Anko를 사용하는 경우 xml을 따로 생성하지 않아도 되기때문
        //  layout -> activity_main 필요없다 !!
    }
}

