package com.syleiman.mappingProcessor

/** Analog for ? : operator (can't find in Kotlin) */
inline fun <R> Boolean.iif(trueBlock: () -> R, falseBlock: () -> R): R {
    if(this)
        return trueBlock()
    else
        return falseBlock()
}
