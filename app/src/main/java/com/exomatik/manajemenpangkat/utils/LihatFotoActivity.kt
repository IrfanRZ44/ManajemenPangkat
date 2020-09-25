package com.exomatik.manajemenpangkat.utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import coil.request.CachePolicy
import com.exomatik.manajemenpangkat.R
import kotlinx.android.synthetic.main.activity_lihat_foto.*

class LihatFotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_lihat_foto)
        init()
    }

    private fun init(){
        val intent = Intent()
        val urlFoto = intent.getStringExtra("urlFoto")

        imgFoto.load(urlFoto) {
            crossfade(true)
            placeholder(R.drawable.ic_camera_white)
            error(R.drawable.ic_camera_white)
            fallback(R.drawable.ic_camera_white)
            memoryCachePolicy(CachePolicy.ENABLED)
        }
    }
}