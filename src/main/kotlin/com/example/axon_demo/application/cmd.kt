package com.example.axon_demo.application

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.bson.types.ObjectId

/**
 * ##1 Command
 */

// 发送
data class IssueCmd(@TargetAggregateIdentifier val id: ObjectId, val amount: Int)

// 回收
data class RedeemCmd(@TargetAggregateIdentifier val id: ObjectId, val amount: Int)
