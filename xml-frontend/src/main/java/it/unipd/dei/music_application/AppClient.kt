package it.unipd.dei.music_application

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import it.unipd.dei.music_application.network.RequestPeriodDetails
import it.unipd.dei.music_application.network.ResponsePeriodDetails

class AppClient {
    private var httpClient = HttpClient(OkHttp) {
        defaultRequest {
            url("https://staging.contabilita.casa")
        }
    }

    suspend fun requestPeriodDetails(
        year: Int,
        month: Int?
    ): ResponsePeriodDetails = httpClient.get {
        setBody(RequestPeriodDetails(year, month))
    }.body<ResponsePeriodDetails>()
}
