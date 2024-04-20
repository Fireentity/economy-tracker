package it.unipd.dei.music_application

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest

class AppClient {
    private var httpClient = HttpClient(OkHttp) {
        defaultRequest {
            url("https://staging.contabilita.casa")
        }
    }
}