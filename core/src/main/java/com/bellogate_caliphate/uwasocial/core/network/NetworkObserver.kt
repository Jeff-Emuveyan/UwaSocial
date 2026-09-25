package com.bellogate_caliphate.uwasocial.core.network

import kotlinx.coroutines.flow.Flow

interface NetworkObserver {
    fun observeNetworkConnectivity(): Flow<Boolean>
    fun isNetworkAvailable(): Boolean
}
