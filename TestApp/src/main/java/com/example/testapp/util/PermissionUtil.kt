package com.example.testapp.util

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.testapp.R
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.PermissionBuilder

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
class PermissionUtil {

    companion object {

        private const val PERMISSION_ICON_LIGHT = "#1972E8"
        private const val PERMISSION_ICON_DARK = "#8AB6F5"

        /**
         * @param fragment Fragment
         * @param permissions List<String>,需要的权限
         * @param lightColor @ColorInt 权限图标亮色模式下的颜色，默认是蓝色(#1972E8)
         * @param darkColor @ColorInt 权限图标深色模式下的颜色，默认是浅蓝色(#8AB6F5)
         * @return PermissionBuilder
         */
        @JvmStatic
        fun requestPermission(
            fragment: Fragment,
            permissions: List<String>,
            @ColorInt lightColor: Int = Color.parseColor(PERMISSION_ICON_LIGHT),
            @ColorInt darkColor: Int = Color.parseColor(PERMISSION_ICON_DARK)
        ): PermissionBuilder = PermissionX
            .init(fragment)
            .permissions(permissions)
            .explainReasonBeforeRequest()
            .setDialogTintColor(lightColor, darkColor)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    fragment.getString(R.string.permission_tips),
                    fragment.getString(R.string.open),
                    fragment.getString(R.string.refuse)
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    fragment.getString(R.string.forward_to_setting_tips),
                    fragment.getString(R.string.open),
                    fragment.getString(R.string.refuse)
                )
            }


        /**
         * @param fragment Fragment
         * @param permissions List<String>,需要的权限
         * @param reason String 请求权限时弹窗的标题
         * @param lightColor @ColorInt 权限图标亮色模式下的颜色，默认是蓝色
         * @param darkColor @ColorInt 权限图标深色模式下的颜色，默认是浅蓝色
         * @return PermissionBuilder
         */
        @JvmStatic
        fun requestPermission(
            fragment: Fragment,
            reason: String, permissions: List<String>,
            @ColorInt lightColor: Int = Color.parseColor(PERMISSION_ICON_LIGHT),
            @ColorInt darkColor: Int = Color.parseColor(PERMISSION_ICON_DARK)
        ): PermissionBuilder = PermissionX
            .init(fragment)
            .permissions(permissions)
            .explainReasonBeforeRequest()
            .setDialogTintColor(lightColor, darkColor)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    reason,
                    fragment.getString(R.string.open),
                    fragment.getString(R.string.refuse)
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    fragment.getString(R.string.forward_to_setting_tips),
                    fragment.getString(R.string.open),
                    fragment.getString(R.string.refuse)
                )
            }


        /**
         * @param activity FragmentActivity
         * @param permissions List<String>,需要的权限
         * @param lightColor @ColorInt 权限图标亮色模式下的颜色，默认是蓝色
         * @param darkColor @ColorInt 权限图标深色模式下的颜色，默认是浅蓝色
         * @return PermissionBuilder
         */
        @JvmStatic
        fun requestPermission(
            activity: FragmentActivity,
            permissions: List<String>,
            @ColorInt lightColor: Int = Color.parseColor(PERMISSION_ICON_LIGHT),
            @ColorInt darkColor: Int = Color.parseColor(PERMISSION_ICON_DARK)
        ): PermissionBuilder = PermissionX
            .init(activity)
            .permissions(permissions)
            .explainReasonBeforeRequest()
            .setDialogTintColor(lightColor, darkColor)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    activity.getString(R.string.permission_tips),
                    activity.getString(R.string.open),
                    activity.getString(R.string.refuse)
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    activity.getString(R.string.forward_to_setting_tips),
                    activity.getString(R.string.open),
                    activity.getString(R.string.refuse)
                )
            }

        /**
         * @param activity FragmentActivity
         * @param permissions List<String>,需要的权限
         * @param reason String 请求权限时弹窗的标题
         * @param lightColor @ColorInt 权限图标亮色模式下的颜色，默认是蓝色
         * @param darkColor @ColorInt 权限图标深色模式下的颜色，默认是浅蓝色
         * @return PermissionBuilder
         */
        @JvmStatic
        fun requestPermission(
            activity: FragmentActivity,
            reason: String, permissions: List<String>,
            @ColorInt lightColor: Int = Color.parseColor(PERMISSION_ICON_LIGHT),
            @ColorInt darkColor: Int = Color.parseColor(PERMISSION_ICON_DARK)
        ): PermissionBuilder = PermissionX
            .init(activity)
            .permissions(permissions)
            .explainReasonBeforeRequest()
            .setDialogTintColor(lightColor, darkColor)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList, reason,
                    activity.getString(R.string.open),
                    activity.getString(R.string.refuse)
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    activity.getString(R.string.forward_to_setting_tips),
                    activity.getString(R.string.open),
                    activity.getString(R.string.refuse)
                )
            }

    }
}
