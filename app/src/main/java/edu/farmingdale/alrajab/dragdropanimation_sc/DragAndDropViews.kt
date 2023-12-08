package edu.farmingdale.alrajab.dragdropanimation_sc

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import edu.farmingdale.alrajab.dragdropanimation_sc.databinding.ActivityDragAndDropViewsBinding

class DragAndDropViews : AppCompatActivity() {
    lateinit var binding: ActivityDragAndDropViewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDragAndDropViewsBinding.inflate(layoutInflater)



        setContentView(binding.root)
        setBorderColor(binding.holder01, Color.RED)
        setBorderColor(binding.holder02, Color.RED)
        setBorderColor(binding.holder03, Color.RED)
        setBorderColor(binding.holder04, Color.RED)
        binding.holder01.setOnDragListener(arrowDragListener)
        binding.holder02.setOnDragListener(arrowDragListener)
        binding.holder03.setOnDragListener(arrowDragListener)
        binding.holder04.setOnDragListener(arrowDragListener)

        binding.upMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.downMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.forwardMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.backMoveBtn.setOnLongClickListener(onLongClickListener)

        val rocketButton = binding.rocketButton
        val rocketAnimation = rocketButton.drawable as AnimationDrawable

        // Set OnClickListener to start the animation when the ImageButton is clicked
        rocketButton.setOnClickListener {
            if (!rocketAnimation.isRunning) {
                rocketButton.post { rocketAnimation.start() }
            }
        }
    }



    private val onLongClickListener = View.OnLongClickListener { view: View ->
        (view as? Button)?.let {

            val item = ClipData.Item(it.tag as? CharSequence)

            val dragData = ClipData( it.tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)
            val myShadow = ArrowDragShadowBuilder(it)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(dragData, myShadow, null, 0)
            } else {
                it.startDrag(dragData, myShadow, null, 0)
            }

            true
        }
        false
    }



    private val arrowDragListener = View.OnDragListener { view, dragEvent ->
        (view as? ImageView)?.let {
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    // Change the border color to yellow when the drag enters the view.
                    setBorderColor(view, Color.YELLOW)
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    // Change the border color back to red when the drag exits the view.
                    setBorderColor(view, Color.RED)
                    return@OnDragListener true
                }
                // No need to handle this for our use case.
                DragEvent.ACTION_DRAG_LOCATION -> {
                    return@OnDragListener true
                }
                DragEvent.ACTION_DROP -> {
                    // Read color data from the clip data and apply it to the card view background.
                    val item: ClipData.Item = dragEvent.clipData.getItemAt(0)
                    val lbl = item.text.toString()
                    Log.d("BCCCCCCCCCCC", "NOTHING > >  " + lbl)

                    // Apply the appropriate arrow image based on the label.
                    when (lbl.toString()) {
                        "UP" -> view.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
                        "DOWN" -> view.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
                        "FORWARD" -> view.setImageResource(R.drawable.ic_baseline_arrow_forward_24)
                        "BACK" -> view.setImageResource(R.drawable.ic_baseline_arrow_back_24)
                    }

                    // Change the border color back to red after dropping.
                    setBorderColor(view, Color.RED)

                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    // Change the border color back to red when the drag ends.
                    setBorderColor(view, Color.RED)
                    return@OnDragListener true
                }
                else -> return@OnDragListener false
            }
        }
        false
    }

    private fun setBorderColor(imageView: ImageView, color: Int) {
        val border = GradientDrawable()
        border.setColor(Color.TRANSPARENT)
        border.setStroke(2, color)
        imageView.background = border
    }



    private class ArrowDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
        private val shadow = view.background
        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            val width: Int = view.width
            val height: Int = view.height
            shadow?.setBounds(0, 0, width, height)
            size.set(width, height)
            touch.set(width / 2, height / 2)
        }
        override fun onDrawShadow(canvas: Canvas) {
            shadow?.draw(canvas)
        }
    }
}