package com.dougie.wallet.data.error

import java.lang.Exception

class NotEthereumException : Exception("you have not enough ETH for fee")
