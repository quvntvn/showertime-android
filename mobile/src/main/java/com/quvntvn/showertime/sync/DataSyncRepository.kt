package com.quvntvn.showertime.sync

import android.content.Context
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.quvntvn.showertime.common.data.RoutineProgram
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataSyncRepository(context: Context) {

    private val dataClient = Wearable.getDataClient(context)

    suspend fun syncPrograms(programs: List<RoutineProgram>) {
        try {
            val jsonString = Json.encodeToString(programs)
            val putDataMapReq = PutDataMapRequest.create("/programs").apply {
                dataMap.putString("programs_json", jsonString)
            }
            val putDataReq = putDataMapReq.asPutDataRequest().setUrgent()
            dataClient.putDataItem(putDataReq).await()
        } catch (e: Exception) {
            // In a real app, handle exceptions, e.g. by logging
        }
    }
}
