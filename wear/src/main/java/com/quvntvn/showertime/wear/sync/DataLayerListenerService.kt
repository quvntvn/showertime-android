package com.quvntvn.showertime.wear.sync

import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.quvntvn.showertime.common.data.RoutineProgram
import com.quvntvn.showertime.wear.data.WearProgramRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class DataLayerListenerService : WearableListenerService() {

    private lateinit var repository: WearProgramRepository
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        repository = WearProgramRepository(this)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/programs") {
                val jsonString = DataMapItem.fromDataItem(event.dataItem).dataMap.getString("programs_json")
                if (jsonString != null) {
                    scope.launch {
                        val programs = Json.decodeFromString<List<RoutineProgram>>(jsonString)
                        repository.savePrograms(programs)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
