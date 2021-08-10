package com.easy.wallet.data.error

/**
 * Created by Dougie
 * on 2020/5/31
 */
class UnSupportTokenException(symbol: String) : Exception("un-support token: {$symbol}")
