package me.danielpf.rmkt.core

data class Product(val id: String, val name: String, val price: Double)

data class ProductExchange(val product: Product, val localPrices: Map<String, Double>)