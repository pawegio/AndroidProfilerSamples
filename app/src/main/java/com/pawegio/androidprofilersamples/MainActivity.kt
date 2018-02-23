package com.pawegio.androidprofilersamples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import com.elpassion.android.commons.recycler.adapters.basicAdapterWithLayoutAndBinder
import com.elpassion.android.commons.recycler.basic.ViewHolderBinder
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.simple_list_item.view.*
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private val items = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        recyclerView.adapter = basicAdapterWithLayoutAndBinder(
            items, R.layout.simple_list_item, ::bindItem
        )
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        refreshDataButton.setOnClickListener {
            items.run { clear(); addAll(createRandomItems()) }
            recyclerView.adapter.notifyDataSetChanged()
        }
    }
}

private fun createRandomItems(): List<Item> {
    val now = LocalDateTime.now()
    return List(1_000) { createItem(it + 1, now) }
}

private fun createItem(offset: Int, now: LocalDateTime): Item {
    val date = now.plusDays(offset.toLong()).toLocalDate().atStartOfDay()
    val duration = getDuration(now, date)
    return Item("${date.format(DateTimeFormatter.ISO_LOCAL_DATE)} - $duration")
}

private fun getDuration(from: LocalDateTime, to: LocalDateTime): String {
    val duration = Duration.between(from, to)
    val hours = duration.toHours()
    val minutes = duration.minusHours(hours).toMinutes()
    val seconds = duration.minusHours(hours).minusMinutes(minutes).seconds
    return buildString {
        if (hours > 0) append("$hours h")
        append(" $minutes min")
        if (seconds > 0) append(" $seconds sec")
    }.trim()
}

private fun bindItem(holder: ViewHolderBinder<Item>, item: Item) {
    holder.itemView.nameView.text = item.name
}

private data class Item(val name: String)
