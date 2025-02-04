package com.aashutosh.desimall_pro.models.makeOrder

data class OrderPlace(
    val billing: Billing,
    val line_items: List<LineItem>,
    val payment_method: String,
    val payment_method_title: String,
    val set_paid: Boolean,
    val shipping: Shipping
)