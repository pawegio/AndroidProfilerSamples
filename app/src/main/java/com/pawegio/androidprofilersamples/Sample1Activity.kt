package com.pawegio.androidprofilersamples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

        swipeRefreshLayout.setOnRefreshListener(::refreshData)
    }

    private fun refreshData() {
        items.run { clear(); addAll(generateItems()) }
        recyclerView.adapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false
    }
}

private fun generateItems(): List<Item> {
    val now = LocalDateTime.now()
    return List(1_000) { Item(now, it + 1) }
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
    val date = item.now.plusDays(item.offset.toLong()).toLocalDate().atStartOfDay()
    val remainingTime = getRemainingTime(item.now, date)
    dateView.text = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
    remainingTimeView.text = resources.getString(R.string.remaining, remainingTime)
}

private data class Item(val now: LocalDateTime, val offset: Int)
