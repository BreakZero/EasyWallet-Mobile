package com.easy.wallet.data.repositories

import com.easy.wallet.data.network.solana.SolanaClient
import com.easy.wallet.data.provider.SolanaProvider

internal class SolanaRepository(
  private val client: SolanaClient,
  private val provider: SolanaProvider
) {
  fun voteAccounts(
    pubKey: List<String> = emptyList()
  ) {

  }
}