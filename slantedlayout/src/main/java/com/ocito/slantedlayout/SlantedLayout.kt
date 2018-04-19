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
 *
 *
 *
 *
 * The different possible configurations for the SlantedLayout are the following parallelograms :
 *
 * A
 *   ╱│
 *  ╱ │
 * │ ╱
 * │╱
 *
 * B
 * │╲
 * │ ╲
 *  ╲ │
 *   ╲│
 *
 *
 * C
 *   __
 *  ╱  ╱
 * ╱__╱
 *
 * D
 * __
 * ╲  ╲
 *  ╲__╲
 *
 * A and B are the vertical directions
 * C and D are the horizontal directions
 *
 * A and C are the positive slantedSize
 * B and D are the negative slantedSize
 *
 *
 *
 * @author Xavier Falempin
 */
class SlantedLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    /**
     * constants used to match the slant ignore flags
     */
    val NONE = 0
    val TOP = 1
    val LEFT = 2
    val RIGHT = 4
    val BOTTOM = 8
    val ALL = 15


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

    /**
     * The slantIgnoreFlags allows to remove the slant on one side (or both) of the SlantedLayout
     * Values can be :
     * "top"(1) or "bottom"(8) when the slantDirection is vertical
     * "left"(2) or "right"(4) when the slantDirection is horizontal
     * Because this is a flag "none"(0) and "all"(15) values also exists and work regardless of the slantDirection.
     */
    var slantIgnoreFlag: Int = 0
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

            slantIgnoreFlag = a.getInteger(R.styleable.SlantedLayout_slant_ignore, 0)

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

    /**
     * This methods update a Path that is used to clip the canvas of the layout and clicks
     */
    private fun updateClipPath() {
        clipPath.reset()



        // we check first the sign of the slantedSize to know if we are on A|C or in B|D configurations

        val slant1 : Float = if(slantedSize > 0) slantedSize else 0.0f
        val slant2 : Float = if(slantedSize < 0) -slantedSize else 0.0f

        when(slantDirection)//we use the slantDirection to determine the exact case
        {
            SlantDirection.VERTICAL -> {
                //We have to check the slantIgnore top and bottom
                val sl1 : Float = if((slantIgnoreFlag or TOP) == slantIgnoreFlag) 0.0f else slant1
                val sl2 : Float = if((slantIgnoreFlag or BOTTOM) == slantIgnoreFlag) 0.0f else slant2
                val sl3 : Float = if((slantIgnoreFlag or TOP) == slantIgnoreFlag) 0.0f else slant2
                val sl4 : Float = if((slantIgnoreFlag or BOTTOM) == slantIgnoreFlag) 0.0f else slant1

                clipPath.moveTo(paddingLeft.toFloat(), height.toFloat() - paddingBottom - sl2)
                clipPath.lineTo(paddingLeft.toFloat(), paddingTop + sl1)
                clipPath.lineTo(width.toFloat() - paddingRight, paddingTop.toFloat() + sl3)
                clipPath.lineTo(width.toFloat() - paddingRight, height - sl4 - paddingBottom)
            }

            SlantDirection.HORIZONTAL -> {
                //We have to check the slantIgnore left and right
                val sl1 : Float = if((slantIgnoreFlag or LEFT) == slantIgnoreFlag) 0.0f else slant1
                val sl2 : Float = if((slantIgnoreFlag or LEFT) == slantIgnoreFlag) 0.0f else slant2
                val sl3 : Float = if((slantIgnoreFlag or RIGHT) == slantIgnoreFlag) 0.0f else slant2
                val sl4 : Float = if((slantIgnoreFlag or RIGHT) == slantIgnoreFlag) 0.0f else slant1

                clipPath.moveTo(paddingLeft.toFloat() + sl2, height.toFloat() - paddingBottom)
                clipPath.lineTo(paddingLeft.toFloat() + sl1, paddingTop.toFloat())
                clipPath.lineTo(width.toFloat() - paddingRight - sl3, paddingTop.toFloat())
                clipPath.lineTo(width.toFloat() - paddingRight - sl4, height.toFloat() - paddingBottom)
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
