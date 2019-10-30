package com.xdd.cloudinteraction2019.data

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit

/**
 * Last in first out
 * */
class LinkedBlockingLifo<E> : LinkedBlockingDeque<E>() {
    override fun offer(e: E): Boolean {
        return super.offerFirst(e)
    }

    override fun offer(e: E, timeout: Long, unit: TimeUnit): Boolean {
        return super.offerFirst(e, timeout, unit)
    }

    override fun add(element: E): Boolean {
        super.addFirst(element)
        return true
    }

    override fun put(e: E) {
        super.putFirst(e)
    }
}