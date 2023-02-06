package com.leo.plugins

import RPCClient
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.leo.service.RabbitPublisherService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val rabbitPublisherService by inject<RabbitPublisherService>()
    val rpc by inject<RPCClient>()

    routing {
        get() {
            call.respondText("Hello World!")

        }
        get("/tasks/") {
//            repeat(1000){
//
//            }
            val response: Map<String, Any> =
                jacksonObjectMapper().readValue(rpc.call(mapOf("type" to "runningUserTasks")))










            call.respond(response)
        }
        post("/completeTask/") {


            val body = call.receive<Map<Any, Any>>()

            val newReq = body + mapOf("type" to "completeTask")
            println(newReq)
            val response: Map<Any, Any> =
                jacksonObjectMapper().readValue(rpc.call(newReq))


            call.respond(mapOf("status" to "ok"))
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
