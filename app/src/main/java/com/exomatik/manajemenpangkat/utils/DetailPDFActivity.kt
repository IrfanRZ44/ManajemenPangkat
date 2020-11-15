package com.exomatik.manajemenpangkat.utils

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import es.voghdev.pdfviewpager.library.RemotePDFViewPager
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter
import es.voghdev.pdfviewpager.library.remote.DownloadFile
import es.voghdev.pdfviewpager.library.util.FileUtil
import kotlinx.android.synthetic.main.activity_detail_pdf.*

class DetailPDFActivity : AppCompatActivity(), DownloadFile.Listener {
    private lateinit var adapter: PDFPagerAdapter
    private lateinit var remotePDFViewPager: RemotePDFViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail_pdf)
        progress.visibility = View.VISIBLE

        val url = intent.getStringExtra("urlFile")

        remotePDFViewPager = RemotePDFViewPager(this, url, this)
    }

    override fun onDestroy() {
        super.onDestroy()

        adapter.close()
    }

    override fun onSuccess(url: String?, destinationPath: String?) {
        adapter = PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url))
        remotePDFViewPager.adapter = adapter
        root.removeAllViewsInLayout()
        root.addView(remotePDFViewPager, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        progress.visibility = View.GONE
    }

    override fun onFailure(e: Exception) {
        e.printStackTrace()
        Toast.makeText(this, "Gagal Membuka File", Toast.LENGTH_SHORT).show()
        progress.visibility = View.GONE
    }

    override fun onProgressUpdate(progress: Int, total: Int) {

    }
}