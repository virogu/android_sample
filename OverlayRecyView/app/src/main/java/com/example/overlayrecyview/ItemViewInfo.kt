package com.example.overlayrecyview

/**
 * Created by 钉某人
 * github: https://github.com/DingMouRen
 * email: naildingmouren@gmail.com
 */
class ItemViewInfo(
    var top: Int,
    var scaleXY: Float,
    var positionOffset: Float,
    var layoutPercent: Float
) {
    private var mIsBottom = false
    fun setIsBottom(): ItemViewInfo {
        mIsBottom = true
        return this
    }

}