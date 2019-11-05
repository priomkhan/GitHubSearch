package com.priomkhan.githubsearch.utilities

import android.content.Context
import java.io.File
import java.io.IOException

/*
Step:3
Create a FileHelper class that read the data from local json asset file.
 */
class FileHelper {
    //Static Method like Java
    companion object{

        //Reading data from assets file
        //context: To Read Content from resources we need a reference to the application context,
        // resourceId: Reference to the file that store resources
        // Here return type is String
        fun getTextFromAssets(context: Context, fileName: String): String{
            //To read from text file here we use some Kotlin Syntax
            //It is like the Java text file reading using BufferReader
            // here 'use' is extension function and it does all the hard work of closing a input stream
            return context.assets.open(fileName).use {
                it.bufferedReader().use{
                    it.readText()
                }
            }
        }

//        fun getTextFromAssets(context: Context, fileName: String): String? {
//            var json: String? = null
//            try {
//                val inputStream = context.assets.open(fileName)
//                val size = inputStream.available()
//                val buffer = ByteArray(size)
//                inputStream.read(buffer)
//                inputStream.close()
//
//                json = String(buffer, Charsets.UTF_8)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//            return json
//        }

    }


}