package hu.kszi2.boti

import hu.kszi2.boti.parsing.DefaultAsyncParser
import hu.kszi2.boti.parsing.MachineAsyncParser
import hu.kszi2.boti.MosogepAsyncApi.UnreachableApiError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*

class MosogepApiV2(private val parser: MachineAsyncParser = DefaultAsyncParser()) : MosogepAsyncApi {
    private val webClient = HttpClient(CIO)

    override suspend fun loadMachines(): List<Machine> {
        try {
            val status = webClient.get("https://mosogep-ng.sch.bme.hu/api/v2") // do not add trailing /
            if (status.status != HttpStatusCode.OK) {
                throw UnreachableApiError("ApiV2")
            }

            val body: String = status.body()
            return parser.parse(body)
        } catch (ex: Exception) {
            throw UnreachableApiError("ApiV2").initCause(ex)
        }
    }
}
