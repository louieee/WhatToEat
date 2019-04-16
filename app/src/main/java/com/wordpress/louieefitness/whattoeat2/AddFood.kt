package com.wordpress.louieefitness.whattoeat2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.wordpress.louieefitness.whattoeat2.database.CircleTransform
import com.wordpress.louieefitness.whattoeat2.database.DataBaseHelper
import com.wordpress.louieefitness.whattoeat2.foodObject.Food
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class AddFood : AppCompatActivity() {
    lateinit var name:TextView
    lateinit var desc :TextView
    private lateinit var picture: ImageView
    private val gallery = 123
    private val camera = 245
    lateinit var context:Context
    private var imagePath = ""

    val food= Food()


    @SuppressLint("StaticFieldLeak")
    inner class Add2FoodList: AsyncTask<String,Void,Unit>(){
        override fun doInBackground(vararg params: String?) {
            val value = params[0]
            return add2food(value,context)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__food)
        name = this.findViewById(R.id.editText)
        desc = this.findViewById(R.id.editText2)
        picture = this.findViewById(R.id.my_food_pic)
        context = this
    }
    private fun add2food(myPath:String?, c:Context) {
        val database = DataBaseHelper(c)
        val n = food
        val fName = name.text.toString()
        val fDesc = desc.text.toString()
        if (fName.isNotEmpty() ) {
            n.name = fName
            n.desc = fDesc
            n.image = myPath!!
            database.insertData(n.image, n.name, n.desc)
        }
    }
    fun addFood(v: View){
        this.Add2FoodList().execute(imagePath)
        Toast.makeText(context,"Food Added",Toast.LENGTH_SHORT).show()
        Toast.makeText(context, " directory: $imagePath",Toast.LENGTH_LONG).show()
        this.finish()
    }
    fun showPictureDialog(v:View) {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }
    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, gallery)
    }
    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, camera)
    }
    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == gallery && resultCode == Activity.RESULT_OK )
        {
            val contentURI = data?.data
            try
            {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                val resized = bitmap.resizeByWidth(700)
                imagePath = saveImage(resized)

                Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show()
                picture.setImageBitmap(resized)

            }
            catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
            }

        }
        else if (requestCode == camera && resultCode == Activity.RESULT_OK)
        {
            val thumbnail = data?.extras!!.get("data") as Bitmap
            val resized = thumbnail.resizeByWidth(700)
            picture.setImageBitmap(resized)
            saveImage(resized)
            Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show()
        }else if (requestCode != Activity.RESULT_OK){
            Toast.makeText(context, "No image was selected", Toast.LENGTH_SHORT).show()
            return
        }
    }

    private fun saveImage(myBitmap: Bitmap):String {
        val root = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString()
        val myDir = File(root + IMAGE_DIRECTORY)
        if (!myDir.exists()) {
            myDir.mkdirs()
        }
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val imageName = "Image-$n.jpg"
        val file = File(myDir, imageName)
        if (file.exists()){
            file.delete()
        }
        try{
            val out = FileOutputStream(file)
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        }catch(e:Exception){
            e.printStackTrace()
        }
        imagePath  = Environment.getExternalStorageDirectory().toString()+"/Pictures"+ IMAGE_DIRECTORY +"/"+
                imageName
        return file.absolutePath
    }

    private fun Bitmap.resizeByWidth(width:Int):Bitmap{
        val ratio:Float = this.width.toFloat() / this.height.toFloat()
        val height:Int = Math.round(width / ratio)
        return Bitmap.createScaledBitmap(this, width, height, false)
    }

    fun Bitmap.resizeByHeight(height:Int):Bitmap{
        val ratio:Float = this.width.toFloat() / this.height.toFloat()
        val width:Int = Math.round(height / ratio)
        return Bitmap.createScaledBitmap(this, width, height, false)
    }
    companion object {
        private const val IMAGE_DIRECTORY = "/WhatToEat"
    }

}


