package com.example.axon_demo.domain

import com.example.axon_demo.application.IssueCmd
import com.example.axon_demo.application.IssuedEvt
import com.example.axon_demo.application.RedeemCmd
import com.example.axon_demo.application.RedeemedEvt
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import org.bson.types.ObjectId

/**
 * ##3 Aggregate
 */

///
@Aggregate
class GiftCard {
    // ##3.1 务必为聚合根添加Id, 进行标识
    // 如果都是非基础类型, 可以都用 lateinit 关键字
    @AggregateIdentifier
    lateinit var id: ObjectId
    var remainingValue: Int = 0

    // ##3.2b 基于Axon4
    // 使用 `@CreationPolicy` 注解, 替代构造函数
    @CreationPolicy(AggregateCreationPolicy.ALWAYS)
    @CommandHandler
    fun handle(cmd: IssueCmd) {
        AggregateLifecycle.apply(IssuedEvt(cmd.id, cmd.amount))
    }

//    // ##3.2a 无参构造
//    constructor()
//
//    // ##3.3 构造-命令处理器
//    // 发送礼品卡, 当礼品卡不存在, 就调用此方法新建一个礼品卡实例
//    @CommandHandler
//    constructor(cmd: IssueCmd) {
//        require(cmd.amount > 0) { throw IllegalArgumentException("Amount <= 0") }
//        // ##3.4 AggregateLifecycle
//        AggregateLifecycle.apply(IssuedEvt(cmd.id, cmd.amount))
//    }

    // ##3.5 源事件 处理器
    @EventSourcingHandler
    fun on(evt: IssuedEvt) {
        id = evt.id
        remainingValue = evt.amount
    }


    // 消费礼品卡额度, 先检查消费金额,必须大于0, 然后检查礼品卡余额,大于要消费的金额
    // 检查通过后才发出事件, 进行扣费(消费礼品卡余额)
    @CommandHandler
    fun handle(cmd: RedeemCmd) {
        require(cmd.amount > 0) { throw IllegalArgumentException("amount <= 0") }
        require(cmd.amount <= remainingValue) { throw IllegalArgumentException("amount > remainingValue") }
        AggregateLifecycle.apply(RedeemedEvt(cmd.id, cmd.amount))
    }


    // 事件处理器
    // 更新礼品卡的余额
    @EventSourcingHandler
    fun on(evt: RedeemedEvt) {
        remainingValue -= evt.amount
    }


}
