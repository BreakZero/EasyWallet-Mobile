package com.easy.wallet.data.defi

import com.easy.wallet.data.constant.ChainId
import com.easy.wallet.data.data.model.SendPlanModel
import com.easy.wallet.data.data.model.nft.NFTAssetDataModel
import com.easy.wallet.data.data.model.nft.NFTCollectionDataModel
import com.easy.wallet.data.data.model.nft.NFTType
import com.easy.wallet.data.data.model.nft.NTFAttr
import com.easy.wallet.data.etx.toHexByteArray
import com.easy.wallet.data.network.opensea.OpenseaClient
import com.easy.wallet.data.network.opensea.OpenseaService
import com.easy.wallet.data.network.web3j.Web3JService
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.utils.Numeric
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import wallet.core.jni.proto.Ethereum
import java.math.BigDecimal
import java.math.BigInteger

class NFTManager(private val hdWallet: HDWallet, chainId: ChainId) {

    companion object {
        private const val DEFAULT_GAS_LIMIT = "600000"
    }

    private val web3JService: Web3j = Web3JService.web3jClient(chainId)
    private var openseaService = OpenseaClient.client(chainId).create(OpenseaService::class.java)

    private val owner = hdWallet.getAddressForCoin(CoinType.ETHEREUM)

    fun loadCollections(): Flow<List<NFTCollectionDataModel>> {
        return flow {
            emit(
                openseaService.getOwnerCollections(0, 300, owner)
                    .map {
                        NFTCollectionDataModel(
                            slug = it.slug,
                            name = it.name,
                            contractAddress = it.assetContracts.firstOrNull()?.address.orEmpty(),
                            imageUrl = it.imageUrl,
                            description = it.description,
                            ownedAssetCount = it.ownedAssetCount
                        )
                    }
            )
        }
    }

    fun getAssetDetail(
        contractAddress: String,
        tokenId: String
    ): Flow<NFTAssetDataModel> {
        return flow {
            val result = openseaService.getSingleAsset(
                contractAddress = contractAddress,
                tokenId = tokenId,
                owner = owner
            )

            emit(
                NFTAssetDataModel(
                    tokenId = result.tokenId,
                    name = result.name,
                    description = result.description,
                    contractAddress = result.assetContract.address,
                    quantity = result.ownership.quantity.toIntOrNull() ?: 1,
                    imageUrl = result.imageUrl,
                    imagePreviewUrl = result.imagePreviewUrl,
                    animationUrl = result.animationUrl,
                    permalink = result.permalink,
                    nftType = NFTType.valueOf(result.assetContract.schemaName),
                    attrs = result.traits.map { trait ->
                        NTFAttr(
                            type = trait.traitType,
                            value = trait.value
                        )
                    }
                )
            )
        }
    }

    fun loadAssets(
        contract: String,
        collection: String
    ): Flow<List<NFTAssetDataModel>> {
        return flow {
            val result = openseaService.getOwnerAssets(
                owner = owner,
                contractAddress = contract,
                collection = collection,
                limit = 300,
                offset = 0
            )
            emit(
                result.assets.map {
                    NFTAssetDataModel(
                        tokenId = it.tokenId,
                        name = it.name,
                        description = it.description,
                        contractAddress = it.assetContract.address,
                        quantity = 0,
                        imageUrl = it.imageUrl,
                        imagePreviewUrl = it.imagePreviewUrl,
                        animationUrl = it.animationUrl,
                        permalink = it.permalink,
                        nftType = NFTType.valueOf(it.assetContract.schemaName),
                        attrs = it.traits.map { trait ->
                            NTFAttr(
                                type = trait.traitType,
                                value = trait.value
                            )
                        }
                    )
                }
            )
        }
    }

    fun buildSendModel(
        nftAsset: NFTAssetDataModel,
        dstAddress: String,
        gasPrice: BigInteger
    ): Flow<SendPlanModel> {
        val chainIdValue = ChainId.RINKEBY.id
        val prvKey = ByteString.copyFrom(hdWallet.getKeyForCoin(CoinType.ETHEREUM).data())
        val gweiPrice = gasPrice.toBigDecimal().movePointRight(9).toBigInteger()
        val e15Transfer =
            if (nftAsset.nftType == NFTType.ERC1155) Ethereum.Transaction.ERC1155Transfer.newBuilder()
                .apply {
                    from = owner
                    to = dstAddress
                    tokenId = ByteString.copyFrom(nftAsset.tokenId.toBigInteger().toHexByteArray())
                    value = ByteString.copyFrom("0x01".toHexByteArray()) // 1
                }.build() else null
        val e721Transfer =
            if (nftAsset.nftType == NFTType.ERC721) Ethereum.Transaction.ERC721Transfer.newBuilder()
                .apply {
                    from = owner
                    to = dstAddress
                    tokenId = ByteString.copyFrom(nftAsset.tokenId.toBigInteger().toHexByteArray())
                }.build() else null

        return flow {
            val nonce = web3JService.ethGetTransactionCount(
                owner,
                DefaultBlockParameterName.LATEST
            ).sendAsync().get().transactionCount
            val signingInput = Ethereum.SigningInput.newBuilder().apply {
                this.privateKey = prvKey
                this.toAddress = nftAsset.contractAddress // contract
                this.chainId = ByteString.copyFrom(chainIdValue.toString(16).toHexByteArray())
                this.nonce = ByteString.copyFrom(nonce.toHexByteArray())
                this.gasPrice = ByteString.copyFrom(gweiPrice.toHexByteArray())
                this.gasLimit =
                    ByteString.copyFrom(DEFAULT_GAS_LIMIT.toBigInteger().toHexByteArray())
                this.transaction = Ethereum.Transaction.newBuilder().apply {
                    e15Transfer?.let {
                        this.erc1155Transfer = it
                    }
                    e721Transfer?.let {
                        this.erc721Transfer = it
                    }
                }.build()
            }
            val encoded = AnySigner.encode(signingInput.build(), CoinType.ETHEREUM)
            emit(
                SendPlanModel(
                    amount = BigDecimal.ZERO,
                    fromAddress = owner,
                    toAddress = dstAddress,
                    gasLimit = DEFAULT_GAS_LIMIT.toBigInteger(),
                    gas = gasPrice.toBigDecimal(),
                    feeDecimals = 9,
                    rawData = Numeric.toHexString(encoded)
                )
            )
        }
    }

    fun broadcastTransaction(rawData: String): Flow<String> {
        return flow {
            val result = web3JService.ethSendRawTransaction(rawData)
                .sendAsync().get().transactionHash
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun estimateGasLimitNeed(
        eoaAddress: String,
        gasPrice: BigInteger,
        ethValue: BigInteger,
        payload: String
    ): BigInteger {
        return web3JService.ethEstimateGas(
            Transaction(owner, null, gasPrice, null, eoaAddress, ethValue, payload)
        ).sendAsync().get().amountUsed
    }
}
