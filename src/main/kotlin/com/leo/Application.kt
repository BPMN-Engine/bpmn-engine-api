package com.leo

import com.leo.plugins.configureHTTP
import com.leo.plugins.configureRouting
import com.leo.plugins.configureSerialization
import com.leo.service.RabbitPublisherService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin


fun main() {


    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}


val prodModule = module {
    single(createdAtStart = true) { RabbitPublisherService().setupModelExchange() }
}

fun Application.module() {
    install(Koin) {
        modules(prodModule)
    }



    configureHTTP()

    configureSerialization()
    configureRouting()


}
