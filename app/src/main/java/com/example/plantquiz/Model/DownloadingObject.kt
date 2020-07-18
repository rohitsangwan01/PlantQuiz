package com.example.plantquiz.Model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DownloadingObject {

    @Throws(IOException::class)
    fun downloadJSONDataFromLink(Link:String):String{
        var stringBuilder : StringBuilder = StringBuilder()

        var url = URL(Link)
        var urlConnecttion = url.openConnection() as HttpURLConnection
        try{
            var bufferedInputString = BufferedInputStream(urlConnecttion.inputStream)
            var  bufferedReader = BufferedReader(InputStreamReader(bufferedInputString))
            var inputLinestring = bufferedReader.readLine()

            while(inputLinestring != null){
                stringBuilder.append(inputLinestring)
                inputLinestring = bufferedReader.readLine()
            }

        }finally {
            urlConnecttion.disconnect()
        }

        return stringBuilder.toString()
    }

    fun downloadPicture(pictureName : String?): Bitmap?{
        var bitmap : Bitmap? = null
        val pictureLink = "https://www.plantplaces.com/photos/$pictureName"
        val pictureURL = URL(pictureLink)
        val inputStream = pictureURL.openConnection().getInputStream()
        if(inputStream != null){
            bitmap = BitmapFactory.decodeStream(inputStream)
        }
        return bitmap
    }

    companion object{
        val PLANTPLACES_COM = "http://www.plantplaces.com"
    }



}