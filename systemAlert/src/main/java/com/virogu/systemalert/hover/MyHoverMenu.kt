package com.virogu.systemalert.hover

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import com.virogu.systemalert.R
import io.mattcarroll.hover.Content
import io.mattcarroll.hover.HoverMenu


/**
 * @author Virogu
 * @since 2022-08-19 11:23
 **/
class MyHoverMenu(private val context: Context) : HoverMenu() {
    private val sections = listOf(
        Section(
            SectionId("1"),
            createTabView(R.drawable.ic_baseline_bubble_chart),
            createScreen("Screen 1")
        ),
        Section(
            SectionId("2"),
            createTabView(R.drawable.ic_baseline_bubble_chart),
            createScreen("Screen 2")
        ),
        Section(
            SectionId("3"),
            createTabView(R.drawable.ic_baseline_bubble_chart),
            createScreen("Screen 3")
        ),
    )

    private fun createTabView(
        @DrawableRes
        resId: Int,
    ): View {
        val v = LayoutInflater.from(context).inflate(R.layout.single_image, null)

        val imageView = v.findViewById<ImageView>(R.id.icon)
        imageView.setBackgroundColor(Color.RED)
        imageView.setImageResource(resId)
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        return imageView
    }

    private fun createScreen(text: String): Content {
        return MyContent(context, text)
    }

    override fun getId(): String {
        return "singlesectionmenu"
    }

    override fun getSectionCount(): Int {
        return sections.size
    }

    override fun getSection(index: Int): Section? {
        return sections.getOrNull(index)
    }

    override fun getSection(sectionId: SectionId): Section? {
        return sections.firstOrNull {
            it.id == sectionId
        }
    }

    @NonNull
    override fun getSections(): List<Section> = sections

}