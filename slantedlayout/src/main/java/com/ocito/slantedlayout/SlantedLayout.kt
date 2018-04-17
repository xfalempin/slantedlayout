package com.ocito.slantedlayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Point
import android.graphics.Region
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * This Layout extends a constraint layout and only draws a parallelogram of the content
 * The clipping to a parallelogram shape does not affect the background set to the SlantedLayout itself
 * @author Xavier Falempin
 */
class SlantedLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {


    enum class SlantDirection
    {
        VERTICAL, HORIZONTAL
    }

    private val clipPath = Path()

    /**
     * The slanted size is the height or width (depending on vertical or horizontal mode) drop
     * for the parallelogram on :
     * top left & bottom right corner for positive values
     * top right & bottom left corner for negative values
     */
    var slantedSize: Float = context.resources.getDimension(R.dimen.slanted_layout_default_size_2)
        get() = field
        set(value) {
            field = value
            updateClipPath()
        }

    var slantDirection: SlantDirection = SlantDirection.VERTICAL
        set(value) {
            field = value
            updateClipPath()
        }

    init {
        attrs?.let {
            // Load attributes
            val a = context.obtainStyledAttributes(it, R.styleable.SlantedLayout, defStyleAttr, 0)

            // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
            // values that should fall on pixel boundaries.
            slantedSize = a.getDimension(
                    R.styleable.SlantedLayout_slant_size,
                    slantedSize)

            slantDirection = if(a.getInteger(R.styleable.SlantedLayout_slant_direction, 0) == 1) SlantDirection.HORIZONTAL else SlantDirection.VERTICAL

            a.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if(changed)
        {
            updateClipPath()
        }
    }

    private fun updateClipPath() {
        clipPath.reset()

        var slant1 : Float = if(slantedSize > 0) slantedSize else 0.0f
        var slant2 : Float = if(slantedSize < 0) -slantedSize else 0.0f

        when(slantDirection)
        {
            SlantDirection.VERTICAL -> {
                clipPath.moveTo(paddingLeft.toFloat(), height.toFloat() - paddingBottom - slant2)
                clipPath.lineTo(paddingLeft.toFloat(), paddingTop + slant1)
                clipPath.lineTo(width.toFloat() - paddingRight, paddingTop.toFloat() + slant2)
                clipPath.lineTo(width.toFloat() - paddingRight, height - slant1 - paddingBottom)
            }

            SlantDirection.HORIZONTAL -> {
                clipPath.moveTo(paddingLeft.toFloat() + slant2, height.toFloat() - paddingBottom)
                clipPath.lineTo(paddingLeft.toFloat() + slant1, paddingTop.toFloat())
                clipPath.lineTo(width.toFloat() - paddingRight - slant2, paddingTop.toFloat())
                clipPath.lineTo(width.toFloat() - paddingRight - slant1, height.toFloat() - paddingBottom)
            }
        }



        clipPath.close()
        postInvalidate()
    }



    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.clipPath(clipPath)
        super.dispatchDraw(canvas)
    }


    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                val clickRegion = Region()
                clickRegion.setPath(clipPath, Region(0, 0, width, height))

                val point = Point()
                point.x = event.x.toInt()
                point.y = event.y.toInt()

                if (!clickRegion.contains(point.x, point.y)) {
                    return false
                }
            }
        }


        return super.onTouchEvent(event)
    }
}
