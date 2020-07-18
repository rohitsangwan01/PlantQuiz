package com.example.plantquiz.Controller

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.plantquiz.Model.DownloadingObject
import com.example.plantquiz.Model.ParsePlantUtility
import com.example.plantquiz.Model.Plant
import com.example.plantquiz.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private var cameraButton: Button? = null
    private var photoGalleryButton : Button? = null
    private var imgTakenv: ImageView? = null
    private var myTexName: TextView? = null
    var correctAnswerIndex : Int = 0
    var correctPlant : Plant? = null
    var rightAnswerv = 0
    var wrongAnswerv = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setProgressBar(false)
        displayUIWidget(false)

        YoYo.with(Techniques.Pulse)
            .duration(700)
            .repeat(5)
            .playOn(btnNextPlant)





            Toast.makeText(this, "Created By Rohit Sangwan", Toast.LENGTH_SHORT).show()
        var myPlant : Plant =
            Plant("", "", "", "", "", "", 0, 0)


        cameraButton = findViewById<Button>(R.id.btnOpenCamera)
        photoGalleryButton = findViewById(R.id.btnOpenPhotoGallery)
        imgTakenv = findViewById<ImageView>(R.id.imgTaken)
        myTexName = findViewById<TextView>(R.id.MyNameText)

        cameraButton?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Camera Button Is Clicked", Toast.LENGTH_SHORT).show()
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent,1)
        })

        photoGalleryButton?.setOnClickListener(View.OnClickListener{
            Toast.makeText(this, "Phot Gallery Button Is Clicked", Toast.LENGTH_SHORT).show()

            var photoIntent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(photoIntent,2)
        })
        myTexName?.setOnClickListener(View.OnClickListener{
            Toast.makeText(this, "Its Just Name Dude, Why You Clicked On it!!", Toast.LENGTH_LONG).show()
        })


        imgTakenv?.setOnClickListener(View.OnClickListener {
            //           var random : Int = ((Math.random()) * 6).toInt() + 1
            Toast.makeText(this, "Click ON STAR Button", Toast.LENGTH_SHORT).show()
//            when(random){
//                1 -> btnOpenCamera.setBackgroundColor(Color.BLUE)
//                2 -> btnOpenPhotoGallery.setBackgroundColor(Color.YELLOW)
//                3,4 -> linearLayout.setBackgroundColor(Color.RED)
//                5,6 -> MyNameText.setBackgroundColor(Color.GREEN)
                //}
        })


        btnNextPlant.setOnClickListener(View.OnClickListener {
            if (chekForInternetConnection()) {
                setProgressBar(true)
                try {

                    var innerClassObject = DownloadingPlantTask()
                    innerClassObject.execute()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                button1.setBackgroundColor(Color.BLACK)
                button2.setBackgroundColor(Color.BLACK)
                button3.setBackgroundColor(Color.BLACK)
                button4.setBackgroundColor(Color.BLACK)



            }

        })


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                val imagedata = data?.getExtras()?.get("data") as Bitmap
                imgTaken?.setImageBitmap(imagedata)
            }
        }

        if(requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
                var contentUri = data?.getData()
                var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,contentUri)
                imgTaken?.setImageBitmap(bitmap)

            }

        }
    }




    fun button1isClicked(buttonView: View) {
       // Toast.makeText(this, "Button 1 Is Clicked", Toast.LENGTH_SHORT).show()
        SpecifyTheRightAndWrongAnswer(0)

    }

    fun button2isClicked(buttonView: View) {
        //Toast.makeText(this, "Button 2 Is Clicked", Toast.LENGTH_SHORT).show()
        SpecifyTheRightAndWrongAnswer(1)

    }

    fun button3isClicked(buttonView: View) {
        //Toast.makeText(this, "Button 3 Is Clicked", Toast.LENGTH_SHORT).show()
        SpecifyTheRightAndWrongAnswer(2)
    }

    fun button4isClicked(buttonView: View) {
        //Toast.makeText(this, "Button 4 Is Clicked", Toast.LENGTH_SHORT).show()
        SpecifyTheRightAndWrongAnswer(3)
    }

    inner class DownloadingPlantTask : AsyncTask<String, Int, List<Plant>>() {
        override fun doInBackground(vararg params: String?): List<Plant>? {
            var parsePlant = ParsePlantUtility()
            return parsePlant.parsePlantObjectFromJSONData()
        }

        override fun onPostExecute(result: List<Plant>?) {
            super.onPostExecute(result)
            var numberOfPlants = result?.size ?: 0

            if (numberOfPlants > 0){

                var randomPlantIndexForButton1 = (Math.random() * result!!.size).toInt()
                var randomPlantIndexForButton2 = (Math.random() * result!!.size).toInt()
                var randomPlantIndexForButton3 = (Math.random() * result!!.size).toInt()
                var randomPlantIndexForButton4 = (Math.random() * result!!.size).toInt()

                var allRandomPlants = ArrayList<Plant>()
                allRandomPlants.add(result.get(randomPlantIndexForButton1))
                allRandomPlants.add(result.get(randomPlantIndexForButton2))
                allRandomPlants.add(result.get(randomPlantIndexForButton3))
                allRandomPlants.add(result.get(randomPlantIndexForButton4))

                button1.text = result.get(randomPlantIndexForButton1).toString()
                button2.text = result.get(randomPlantIndexForButton2).toString()
                button3.text = result.get(randomPlantIndexForButton3).toString()
                button4.text = result.get(randomPlantIndexForButton4).toString()

                correctAnswerIndex = (Math.random() * allRandomPlants.size).toInt()
                correctPlant = allRandomPlants.get(correctAnswerIndex)

                val downloadingImageTask = DownloadImageTask()
                downloadingImageTask.execute(allRandomPlants.get(correctAnswerIndex).pictureName)
            }
        }
    }

    override fun onResume() {
        super.onResume()
       // Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show()
        chekForInternetConnection()
    }

    override fun onStart() {
        super.onStart()
        //Toast.makeText(this, "onSatrt", Toast.LENGTH_SHORT).show()
        chekForInternetConnection()
    }


    override fun onPause() {
        super.onPause()
        //Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show()
        //chekForInternetConnection()
    }


override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun chekForInternetConnection():Boolean {
        var result2 : Boolean = false

        var connectivityManager: ConnectivityManager = this.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw: Network? = connectivityManager.activeNetwork
            val actNw: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(nw)
            val actPk =
                actNw != null && actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            if (actPk) {
                result2 = true
            } else {
                createAlert()
                Toast.makeText(this, "Network is Off", Toast.LENGTH_SHORT).show()
                result2 = false
            }
        }
        return result2

    }

//first trial code
           /* val actNT : Boolean =   actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) return true
            val actNC : Boolean =   actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            val acNew = actNw?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            var actRR : Boolean = actNw != null && acNew
            if(acNew){
                result = true
                Toast.makeText(this, "Network is On", Toast.LENGTH_SHORT).show()
            }else{
                createAlert()
                Toast.makeText(this, "else is Executed", Toast.LENGTH_SHORT).show()
                result = false
            }
        }else{
            createAlert()
            Toast.makeText(this, "else2 is Executed", Toast.LENGTH_SHORT).show()
            result = false
        }
// second trial code
    var networkInfo = connectivityManager.activeNetworkInfo
    var networkState = networkInfo !=null && networkInfo.isConnectinOrConnected
    if (networkState){
        return true
    }else{
        //createAlert()
        return false*/
    private fun createAlert(){
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Network Error")
        alertDialog.setMessage("Please Check For Internet Connection")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK",{
            dialog: DialogInterface?, which: Int ->
            startActivity(Intent(Settings.ACTION_SETTINGS))
        })
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel",{
            dialog: DialogInterface?, which: Int ->
            Toast.makeText(this,"You Must Be Connected To The Internet", Toast.LENGTH_SHORT).show()
            finish()
        })
        alertDialog.show()
    }
    private  fun SpecifyTheRightAndWrongAnswer(userGuess : Int){

        when(correctAnswerIndex){
            0 -> button1.setBackgroundColor(Color.RED)
            1 -> button2.setBackgroundColor(Color.RED)
            2 -> button3.setBackgroundColor(Color.RED)
            3 -> button4.setBackgroundColor(Color.RED)
        }
        if(userGuess == correctAnswerIndex){
            rightAnswerv++
            rightAnswer.setText(rightAnswerv.toString())

            txtState.setText("Right")
        }else{
            txtState.setText("Wrong")
            wrongAnswerv++
            wrongAnswer.setText(wrongAnswerv.toString())
        }

    }
    inner class DownloadImageTask():AsyncTask<String,Int,Bitmap?>(){
        override fun doInBackground(vararg pictureName: String?): Bitmap? {
            try{
                var downloadObject = DownloadingObject()
                var plantBitmap : Bitmap?= downloadObject.downloadPicture(pictureName[0])
                return plantBitmap
            }catch(e:Exception){
                e.printStackTrace()
            }
            return null

        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            setProgressBar(false)
            displayUIWidget(true)
            imgTaken?.setImageBitmap(result)

            playAnimationOnView(imgTaken,Techniques.Tada)
            playAnimationOnView(button1,Techniques.RollIn)
            playAnimationOnView(button2,Techniques.RollIn)
            playAnimationOnView(button3,Techniques.RollIn)
            playAnimationOnView(button4,Techniques.RollIn)
            playAnimationOnView(txtState,Techniques.Swing)

        }


    }

    fun setProgressBar(show:Boolean){
        if(show){
            layoutProgressBar.setVisibility(View.VISIBLE)
            progressBar.setVisibility(View.VISIBLE)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }else if(!show){
            layoutProgressBar.setVisibility(View.GONE)
            progressBar.setVisibility(View.GONE)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun displayUIWidget(display:Boolean){
        if (display){

            imgTaken.setVisibility(View.VISIBLE)
            button1.setVisibility(View.VISIBLE)
            button2?.setVisibility(View.VISIBLE)
            button3?.setVisibility(View.VISIBLE)
            button4?.setVisibility(View.VISIBLE)
            wrongAnswer?.setVisibility(View.VISIBLE)
            rightAnswer?.setVisibility(View.VISIBLE)
            txtState.setVisibility(View.VISIBLE)
        }else if(!display){

            imgTaken.setVisibility(View.INVISIBLE)
            button1.setVisibility(View.INVISIBLE)
            button2?.setVisibility(View.INVISIBLE)
            button3?.setVisibility(View.INVISIBLE)
            button4?.setVisibility(View.INVISIBLE)
            wrongAnswer?.setVisibility(View.INVISIBLE)
            rightAnswer?.setVisibility(View.INVISIBLE)
            txtState.setVisibility(View.INVISIBLE)


        }





    }

    private  fun playAnimationOnView(view:View , technique:Techniques){
        YoYo.with(technique)
            .duration(700)
            .repeat(0)
            .playOn(view)
    }




}

