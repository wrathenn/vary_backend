package dao

import dto.VersionTemplate
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import models.Version


class AppStore {
    var curVersion: Version? = null
    var devVersion: Version? = null

    fun loadData() {
        val curVersionT: VersionTemplate
        var devVersionT: VersionTemplate? = null
        runBlocking {
            curVersionT = Connection.client.get("${HOST}/application/current").body()
            val httpResponse = Connection.client.get("${HOST}/application/dev")
            if (httpResponse.contentLength() != 0L) {
                devVersionT = httpResponse.body()
            }
        }
        curVersion = curVersionT.toModel()
        devVersion = devVersionT?.toModel()
    }

    fun startDev(code: String): Version {
        runBlocking {
            val devVersionTemplate: VersionTemplate =
                Connection.client.post("${HOST}/application/start") {
                    url {
                        parameters.append("devVersionCode", code)
                    }
                }.body()
            devVersion = devVersionTemplate.toModel()
        }
        return devVersion!!
    }

    fun endDev() {
        runBlocking {
            Connection.client.post("${HOST}/application/release")
        }
        loadData()
    }
}
