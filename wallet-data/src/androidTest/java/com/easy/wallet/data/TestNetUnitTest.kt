package com.easy.wallet.data

import com.easy.wallet.data.constant.ChainId
import com.easy.wallet.data.data.remote.testnet.DataInfo
import com.easy.wallet.data.data.remote.testnet.StateResponse
import com.easy.wallet.data.etx.toHexByteArray
import com.easy.wallet.data.network.testnet.TestNetService
import com.easy.wallet.data.provider.EthereumProvider
import com.google.protobuf.ByteString
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.web3j.utils.Numeric
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.EthereumAbi
import wallet.core.jni.PrivateKey
import wallet.core.jni.proto.Ethereum

class TestNetUnitTest {
    init {
        System.loadLibrary("TrustWalletCore")
    }

    @MockK
    lateinit var mockTestNetService: TestNetService

    @Before
    fun initWallet() {
        DeFiWalletSDK.initWallet(
            "ripple scissors kick mammal hire column oak again sun offer wealth tomorrow wagon turn fatal",
            ""
        )
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun mockTest() {
        val address = "123456"
        val response = StateResponse(
            address = address,
            info = DataInfo(
                address = address,
                balance = 100L,
                callCount = 1,
                firstReceivedTxBlock = 1,
                internalCount = 1,
                invalidTxCount = 1,
                isContract = true,
                largestTxAmount = 1L,
                largestTxBlock = 1,
                lastReceivedTxBlock = 1,
                minedBlocksAmount = 1,
                minedBlocksCount = 1,
                minedUnclesAmount = 1,
                minedUnclesCount = 1,
                pendingReceivedTxCount = 1,
                pendingSentTxCount = 1,
                receivedAmount = 1L,
                receivedTxCount = 1,
                sentAmount = 1,
                sentTxCount = 1,
                transferCount = 1
            ),
            time = 123.toDouble()
        )
        coEvery { mockTestNetService.ethBalance(address) } returns response

        val ethProvider = EthereumProvider(ChainId.MAINNET)
        ethProvider.testNetService = mockTestNetService

        val flow = ethProvider.getBalance(address)
        runBlocking {
            val result = flow.singleOrNull()
            assert(result == 100.toBigInteger())
        }
    }

    @Test
    fun testApprove() {
        val call =
            "0x095ea7b30000000000000000000000007a250d5630b4cf539739df2c5dacb4c659f2488dffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff".toHexByteArray()
        val abiFunction = """ 
            {
                "095ea7b3":{
                    "constant": false,
                    "inputs": [
                      {
                        "name": "_spender",
                        "type": "address"
                      },
                      {
                        "name": "_value",
                        "type": "uint256"
                      }
                    ],
                    "name": "approve",
                    "outputs": [
                      {
                        "name": "success",
                        "type": "bool"
                      }
                    ],
                    "payable": false,
                    "type": "function"
                }
            }
        """.trimIndent()
        val decoded = EthereumAbi.decodeCall(call, abiFunction)
        println(decoded)
    }

    @Test
    fun testABI() {
        val call =
            "f305d7190000000000000000000000006b175474e89094c44da98b954eedeac495271d0f0000000000000000000000000000000000000000000000001bc16d674ec800000000000000000000000000000000000000000000000000001bba526a053b000000000000000000000000000000000000000000000000000000150994c3710ab900000000000000000000000095703ea3622e3cc5bd295ea9904414a90e3e51e7000000000000000000000000000000000000000000000000000000005f55ef93".toHexByteArray()
        val abiFunction = """ 
{
            "f305d719": {
    "inputs": [
      {
        "internalType": "address",
        "name": "token",
        "type": "address"
      },
      {
        "internalType": "uint256",
        "name": "amountTokenDesired",
        "type": "uint256"
      },
      {
        "internalType": "uint256",
        "name": "amountTokenMin",
        "type": "uint256"
      },
      {
        "internalType": "uint256",
        "name": "amountETHMin",
        "type": "uint256"
      },
      {
        "internalType": "address",
        "name": "to",
        "type": "address"
      },
      {
        "internalType": "uint256",
        "name": "deadline",
        "type": "uint256"
      }
    ],
    "name": "addLiquidityETH",
    "outputs": [
      {
        "internalType": "uint256",
        "name": "amountToken",
        "type": "uint256"
      },
      {
        "internalType": "uint256",
        "name": "amountETH",
        "type": "uint256"
      },
      {
        "internalType": "uint256",
        "name": "liquidity",
        "type": "uint256"
      }
    ],
    "stateMutability": "payable",
    "type": "function"
  }
            }
        """.trimIndent()
        val decoded = EthereumAbi.decodeCall(call, abiFunction)
        println(decoded)
    }

    @Test
    fun buildERC1155() {
        val signingInput = Ethereum.SigningInput.newBuilder()
        signingInput.apply {
            privateKey = ByteString.copyFrom(PrivateKey("0x608dcb1742bb3fb7aec002074e3420e4fab7d00cced79ccdac53ed5b27138151".toHexByteArray()).data())
            toAddress = "0x4e45e92ed38f885d39a733c14f1817217a89d425" // contract
            chainId = ByteString.copyFrom("0x01".toHexByteArray())
            nonce = ByteString.copyFrom("0x00".toHexByteArray())
            gasPrice = ByteString.copyFrom("0x09C7652400".toHexByteArray()) // 42000000000
            gasLimit = ByteString.copyFrom("0x0130b9".toHexByteArray()) // 78009
            transaction = Ethereum.Transaction.newBuilder().apply {
                erc1155Transfer = Ethereum.Transaction.ERC1155Transfer.newBuilder().apply {
                    from = "0x718046867b5b1782379a14eA4fc0c9b724DA94Fc"
                    to = "0x5322b34c88ed0691971bf52a7047448f0f4efc84"
                    tokenId = ByteString.copyFrom("0x23c47ee5".toHexByteArray())
                    value = ByteString.copyFrom("0x1BC16D674EC80000".toHexByteArray()) // 2000000000000000000
                }.build()
            }.build()
        }

        val output = AnySigner.sign(signingInput.build(), CoinType.ETHEREUM, Ethereum.SigningOutput.parser())
        val encoded = AnySigner.encode(signingInput.build(), CoinType.ETHEREUM)

        println(Numeric.toHexString(encoded))
    }
}
