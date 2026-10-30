package uz.ekranoquvchi.mvp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import uz.ekranoquvchi.mvp.R
import uz.ekranoquvchi.mvp.core.GestureAction
import uz.ekranoquvchi.mvp.core.GlobalMenu
import uz.ekranoquvchi.mvp.util.Prefs

/**
 * O'zbegim menyusini sozlash: menyuda (pastga, so'ng o'ngga surish) qaysi bandlar ko'rinishini tanlash. Belgilangan
 * bandlar menyuda chiqadi; belgisi olib tashlansa, band menyudan yo'qoladi. Har bir o'zgarish darhol saqlanadi.
 * "Standart ro'yxatga qaytarish" hamma narsani dastlabki holatga keltiradi.
 */
class MenuActivity : AppCompatActivity() {
    private lateinit var container: LinearLayout
    private val boxes = LinkedHashMap<GestureAction, CheckBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        title = getString(R.string.menu_editor_title)
        container = findViewById(R.id.container)
        render()
    }

    private fun render() {
        container.removeAllViews()
        boxes.clear()

        val intro = TextView(this)
        intro.text = getString(R.string.menu_editor_intro)
        intro.textSize = 17f
        intro.setPadding(0, 16, 0, 8)
        intro.isFocusable = true
        container.addView(intro)

        val reset = Button(this)
        reset.setText(R.string.menu_editor_reset)
        reset.setOnClickListener {
            Prefs.saveMenuItems(this, null)
            render()
        }
        container.addView(reset)

        val selected = GlobalMenu.effective(this).toSet()
        for (a in GlobalMenu.pool) {
            val box = CheckBox(this)
            box.text = a.label
            // Holat avval o'rnatiladi, tinglovchi keyin: render paytida keraksiz saqlash bo'lmasin
            box.isChecked = a in selected
            box.setOnCheckedChangeListener { _, _ -> save() }
            boxes[a] = box
            container.addView(box)
        }
    }

    private fun save() {
        val items = GlobalMenu.pool.filter { boxes[it]?.isChecked == true }
        Prefs.saveMenuItems(this, items)
    }
}
