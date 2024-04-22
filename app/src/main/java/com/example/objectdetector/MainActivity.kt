package com.example.objectdetector

import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.example.objectdetector.ml.SsdMobilenetV11Metadata1
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

class MainActivity : AppCompatActivity() {

    private val paint = Paint()
    private lateinit var selectButton: Button
    private lateinit var imageView: ImageView
    private lateinit var bitmap: Bitmap
    private var colorlList = listOf(Color.BLUE, Color.GREEN, Color.RED, Color.CYAN, Color.GRAY, Color.BLACK, Color.DKGRAY, Color.MAGENTA, Color.YELLOW, Color.LTGRAY)
    private lateinit var labels: List<String>
    private lateinit var model: SsdMobilenetV11Metadata1
    private val imageProcessor = ImageProcessor.Builder().add(ResizeOp(300, 300, ResizeOp.ResizeMethod.BILINEAR)).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        labels = FileUtil.loadLabels(this, "mobilenet_objectdetection_labels.txt")
        model = SsdMobilenetV11Metadata1.newInstance(this)

        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5.0f

        Log.d("labels", labels.toString())

        val intent = Intent()
        intent.setAction(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")

        selectButton = findViewById(R.id.selectButton)
        imageView = findViewById(R.id.background)

        selectButton.setOnClickListener {
            startActivityForResult(intent, 101)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101){
            var imageUri = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            get_predictions()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        model.close()
    }

    fun get_predictions(){

        var image = TensorImage.fromBitmap(bitmap)
        image = imageProcessor.process(image)
        val outputs = model.process(image)
        val locations = outputs.locationsAsTensorBuffer.floatArray
        val classes = outputs.classesAsTensorBuffer.floatArray
        val scores = outputs.scoresAsTensorBuffer.floatArray
        outputs.numberOfDetectionsAsTensorBuffer.floatArray



        val mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        var canvas = Canvas(mutable)
        var imgHeight = mutable.height
        var imgWidth = mutable.width


        paint.textSize = imgHeight/15f
        paint.strokeWidth = imgHeight/85f
        scores.forEachIndexed { index, fl -> if(fl > 0.5){
                var idx = index
                idx *= 4
                paint.color = colorlList[index]
                paint.style = Paint.Style.STROKE
                canvas.drawRect(RectF(locations[idx+1] *imgWidth, locations[idx] *imgHeight, locations[idx+3] *imgWidth, locations[idx+2] *imgHeight), paint)
                paint.style = Paint.Style.FILL
                canvas.drawText(labels[classes[index].toInt()] + " " + fl.toString(), locations[idx+1] *imgWidth, locations[idx] *imgHeight, paint)
            }
        }

        imageView.setImageBitmap(mutable)

    }
}