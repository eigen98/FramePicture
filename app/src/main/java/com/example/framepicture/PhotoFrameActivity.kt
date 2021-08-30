package com.example.framepicture

import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {

    private val photoList = mutableListOf<Uri>()

    private var currentPosition = 0

    private var timer : Timer? = null

    private val photoImageView : ImageView by lazy {
        findViewById<ImageView>(R.id.photoImageView)
    }
    private val backgroundPhotoImageView : ImageView by lazy {
        findViewById<ImageView>(R.id.backgroundPhotoImageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoframe)

        getPhotoUriFromIntent()

        startTimer()
    }
    //uri에서 데이터 가져오기
    private fun getPhotoUriFromIntent(){
        val size = intent.getIntExtra("photoListSize", 0)
        for(i in 0..size){
            intent.getStringExtra("photo$i")?.let{//let으로 null이 아닐 때만 실행
                photoList.add(Uri.parse(it))
            }
        }
    }

    //넘기기 위해서 타이머 객체 사용
    private fun startTimer(){
        //5초에 한 번씩
        timer = timer(period = 5*1000){
            //main쓰레드가 아니기에 main으로 바꿔줄 필요있음
            runOnUiThread{

                val current = currentPosition
                val next = if(photoList.size <= currentPosition +1) 0 else currentPosition+1

                backgroundPhotoImageView.setImageURI(photoList[current])

                photoImageView.alpha = 0f //투명도는 0f (완전 투명)
                photoImageView.setImageURI(photoList[next])

                photoImageView.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentPosition = next

            }
        }
    }

    override fun onStop() {
        super.onStop()

        timer?.cancel() //닫혔을 때 멈춤
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

}