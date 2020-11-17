package com.exomatik.manajemenpangkat.services.notification

import com.exomatik.manajemenpangkat.services.notification.model.MyResponse
import com.exomatik.manajemenpangkat.services.notification.model.Sender
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers("Content-Type:application/json", "Authorization:key=AAAA-sRpp9g:APA91bEfx3P8cnXPMsS-vt_X9meWzcXferoEwj2nMcOWYQaB7xy04w1xEjl3XlTjZGQjaABtEVDgYABHtG9Bv9zycj4KQ3jnSpbBDgKGJs6bRAfj7vK41N8SIfZ1h0QNFSMhHLS2ZQGu")
    @POST("fcm/send")
    fun sendNotification(@Body body: Sender?): Call<MyResponse?>?
}