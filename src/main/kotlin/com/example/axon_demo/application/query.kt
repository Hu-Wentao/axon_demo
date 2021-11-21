package com.example.axon_demo.application

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import java.util.concurrent.CopyOnWriteArrayList
import java.util.stream.Collectors

/**
 * ##4 QueryModel 查询模型 (投影 Projection)
 */

// 查询模型: 礼品卡概览信息
data class CardSummary(
    val id: ObjectId,
    val initAmount: Int,
    val remainingAmount: Int,
) {
    // 扣费
    fun deductAmount(toBeDeducted: Int): CardSummary = CardSummary(id, initAmount, remainingAmount - toBeDeducted)
}

// 查询: 获取礼品卡概览
data class FetchCardSummariesQuery(
    val size: Int,
    val offset: Int,
)

// 投影: 礼品卡概览投影
@Component
class CardSummaryProjection {
    private val cardSummaries: MutableList<CardSummary> = CopyOnWriteArrayList()

    // ##4.1
    // 处理发送事件: 将事件添加到集合中, 只是用于更新Projection(查询模型)
    @EventHandler
    fun on(evt: IssuedEvt) {
        val summary = CardSummary(evt.id, evt.amount, evt.amount)
        cardSummaries.add(summary)
    }

    // 例如消费事件:
    // 更新礼品卡概览, 删除老数据,插入新数据
    @EventHandler
    fun on(evt: RedeemedEvt) {
        cardSummaries.firstOrNull { it.id == evt.id }?.let {
            val updated = it.deductAmount(evt.amount)
            cardSummaries.remove(it)
            cardSummaries.add(updated)
        }
    }

    // ##4.2
    // 标记QueryHandler,
    @QueryHandler
    fun fetch(query: FetchCardSummariesQuery): MutableList<CardSummary> =
        cardSummaries.stream()
            .skip(query.offset.toLong())
            .limit(query.size.toLong())
            .collect(Collectors.toList())

}
