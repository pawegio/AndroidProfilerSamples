package com.pawegio.androidprofilersamples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import com.elpassion.android.commons.recycler.adapters.basicAdapterWithLayoutAndBinder
import com.elpassion.android.commons.recycler.basic.ViewHolderBinder
import kotlinx.android.synthetic.main.sample_1_activity.*
import kotlinx.android.synthetic.main.simple_list_item.view.*
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class Sample1Activity : AppCompatActivity() {

    private val items = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_1_activity)

        recyclerView.adapter = basicAdapterWithLayoutAndBinder(
            items, R.layout.simple_list_item, ::bindItem
        )
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        swipeRefreshLayout.setOnRefreshListener(::refreshData)
    }

    private fun refreshData() {
        items.run { clear(); addAll(createRandomItems()) }
        recyclerView.adapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false
    }
}

private fun createRandomItems(): List<Item> {
    val now = LocalDateTime.now()
    return List(1_000) { createItem(now, it + 1) }
}

private fun createItem(now: LocalDateTime, offset: Int): Item {
    val date = now.plusDays(offset.toLong()).toLocalDate().atStartOfDay()
    return Item(
        formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE),
        remainingTime = getRemainingTime(now, date)
    )
}

private fun getRemainingTime(start: LocalDateTime, end: LocalDateTime): String {
    val duration = Duration.between(start, end)
    val days = duration.toDays()
    val hours = duration.minusDays(days).toHours()
    val minutes = duration.minusDays(days).minusHours(hours).toMinutes()
    val seconds = duration.minusDays(days).minusHours(hours).minusMinutes(minutes).seconds
    return buildString {
        if (days > 0) append("$days d")
        if (hours > 0) append(" $hours h")
        if (minutes > 0) append(" $minutes min")
        if (seconds > 0) append(" $seconds s")
    }.trim()
}

private fun bindItem(holder: ViewHolderBinder<Item>, item: Item) = with(holder.itemView) {
    dateView.text = item.formattedDate
    remainingTimeView.text = resources.getString(R.string.remaining, item.remainingTime)
}

private data class Item(val formattedDate: String, val remainingTime: String)
