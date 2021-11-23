package com.example.axon_demo.application

import org.bson.types.ObjectId

/**
 * ##2 Event
 */

// 已发送
data class IssuedEvt(val id: ObjectId, val amount: Int)

data class RedeemedEvt(val id: ObjectId, val amount: Int)
