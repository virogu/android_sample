package com.virogu.library.common.util

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.PermissionBuilder
import com.virogu.library.R

/**
 * @author Virogu
 * @since 2020/09/15
 *
 * 基于郭霖的PermissionX库封装了几个常用的方法，
 * 使用时不需要每次都写一堆重复的代码，一行代码即可实现权限请求
 * 可以自定义权限请求时的提示标题，权限组的图标颜色
 * 如需要更多自定义功能，比如自定义弹窗，可以参考
 * @link CustomDialog
 *
 * PermissionX项目地址
 * @see <a href="https://github.com/guolindev/PermissionX">PermissionX</a>
 */
private val DEFAULT_PERMISSION_ICON_LIGHT = Color.parseColor("#1972E8")
private val DEFAULT_PERMISSION_ICON_DARK = Color.parseColor("#8AB6F5")

/**
 * @param permissions List<String>,需要的权限
 * @param reason String 请求权限时弹窗的标题
 * @param lightColor @ColorInt 权限图标亮色模式下的颜色，默认是蓝色
 * @param darkColor @ColorInt 权限图标深色模式下的颜色，默认是浅蓝色
 * @return PermissionBuilder
 */
fun Fragment.requestPermission(
    permissions: List<String>,
    reason: String = getString(R.string.permission_permission_tips),
    positiveText: String = getString(R.string.permission_open),
    negativeText: String? = getString(R.string.permission_refuse),
    forwardToSettingTips: String = getString(R.string.permission_forward_to_setting_tips),
    @ColorInt lightColor: Int = DEFAULT_PERMISSION_ICON_LIGHT,
    @ColorInt darkColor: Int = DEFAULT_PERMISSION_ICON_DARK
): PermissionBuilder = PermissionX.init(this).permissions(permissions)
    .explainReasonBeforeRequest()
    .setDialogTintColor(lightColor, darkColor)
    .onExplainRequestReason { scope, deniedList ->
        scope.showRequestReasonDialog(deniedList, reason, positiveText, negativeText)
    }.onForwardToSettings { scope, deniedList ->
        scope.showForwardToSettingsDialog(
            deniedList,
            forwardToSettingTips,
            positiveText,
            negativeText
        )
    }


/**
 * @param permissions List<String>,需要的权限
 * @param reason String 请求权限时弹窗的标题
 * @param lightColor @ColorInt 权限图标亮色模式下的颜色，默认是蓝色
 * @param darkColor @ColorInt 权限图标深色模式下的颜色，默认是浅蓝色
 * @return PermissionBuilder
 */
fun FragmentActivity.requestPermission(
    permissions: List<String>,
    reason: String = getString(R.string.permission_permission_tips),
    positiveText: String = getString(R.string.permission_open),
    negativeText: String? = getString(R.string.permission_refuse),
    forwardToSettingTips: String = getString(R.string.permission_forward_to_setting_tips),
    @ColorInt lightColor: Int = DEFAULT_PERMISSION_ICON_LIGHT,
    @ColorInt darkColor: Int = DEFAULT_PERMISSION_ICON_DARK
): PermissionBuilder = PermissionX.init(this).permissions(permissions)
    .explainReasonBeforeRequest()
    .setDialogTintColor(lightColor, darkColor)
    .onExplainRequestReason { scope, deniedList ->
        scope.showRequestReasonDialog(deniedList, reason, positiveText, negativeText)
    }.onForwardToSettings { scope, deniedList ->
        scope.showForwardToSettingsDialog(
            deniedList,
            forwardToSettingTips,
            positiveText,
            negativeText
        )
    }

