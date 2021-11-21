package com.example.axon_demo.domain

import com.example.axon_demo.application.IssueCmd
import com.example.axon_demo.application.IssuedEvt
import com.example.axon_demo.application.RedeemCmd
import com.example.axon_demo.application.RedeemedEvt
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.bson.types.ObjectId

class GiftCard2 {
    @AggregateIdentifier // (1)
    private var id: ObjectId? = null
    private var remainingValue = 0

    constructor() {
        // (2)
    }

    @CommandHandler // (3)
    constructor(cmd: IssueCmd) {
        require(cmd.amount > 0) { "amount <= 0" }
        AggregateLifecycle.apply(IssuedEvt(cmd.id, cmd.amount)) // (4)
    }

    @EventSourcingHandler // (5)
    fun on(evt: IssuedEvt) {
        id = evt.id
        remainingValue = evt.amount
    }

    @CommandHandler
    fun handle(cmd: RedeemCmd) {
        require(cmd.amount > 0) { "amount <= 0" }
        check(cmd.amount <= remainingValue) { "amount > remaining value" }
        AggregateLifecycle.apply(RedeemedEvt(id!!, cmd.amount))
    }

    @EventSourcingHandler
    fun on(evt: RedeemedEvt) {
        remainingValue -= evt.amount
    }
}