package moscht

import hu.kszi2.boti.parsing.MachineAsyncParser
import hu.kszi2.boti.parsing.DefaultAsyncParser
import moscht.MosogepAsyncApi.UnreachableApiError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*

class MosogepApiV1(private val parser: MachineAsyncParser = DefaultAsyncParser()) : MosogepAsyncApi {
    private val webClient = HttpClient(CIO)

    override suspend fun loadMachines(): List<Machine> {
        try {
            val status = webClient.get("https://mosogep.sch.bme.hu/api/v1/laundry-room/")
            if (status.status != HttpStatusCode.OK) {
                throw UnreachableApiError("ApiV1")
            }

            val body: String = status.body()
            return parser.parse(body)
        } catch (ex: Exception) {
            throw UnreachableApiError("ApiV1").initCause(ex)
        }
    }
}