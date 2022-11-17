package com.leo.plugins

import com.leo.service.RabbitPublisherService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val rabbitPublisherService by inject<RabbitPublisherService>()

    routing {
        get() {
            call.respondText("Hello World!")

        }
        get("/instance") {
//            repeat(1000){
//
//            }

            rabbitPublisherService.dispatch()







            call.respondText("Hello World!")
        }
        get("/form") {
//            channel.basicPublish(
//                RabbitService.EXCHANGE,
//                "mykey", null, "myship".toString().toByteArray()
//            )
            call.respondText("Hello World!")
        }
    }
}
