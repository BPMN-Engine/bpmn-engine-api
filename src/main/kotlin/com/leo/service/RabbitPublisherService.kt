package com.leo.service

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.*


class RabbitPublisherService {
    private var connectionFactory: ConnectionFactory = ConnectionFactory()

    private lateinit var channel: Channel
    private lateinit var connection: Connection


    companion object {

        const val EXCHANGE = "modelexchange"

    }


    init {

        connectionFactory.host = "localhost"
        connectionFactory.port = 5672
        connectionFactory.username = "guest"
        connectionFactory.password = "guest"
        connectionFactory.isAutomaticRecoveryEnabled = true


    }

    fun getChannel(): Channel {
        if (!channel.isOpen()) {
            channel = connection.createChannel()
        }
        return channel
    }


    @OptIn(DelicateCoroutinesApi::class)
    suspend fun dispatch() = getChannel().basicPublish(
        EXCHANGE,
        "instance#1", null, "myship".toString().toByteArray()
    )

    fun setupModelExchange(): RabbitPublisherService {


        connection = connectionFactory.newConnection()
        channel = connection.createChannel()


        try {

            getChannel().exchangeDeclarePassive(EXCHANGE)
            println("initialzied")

        } catch (e: Exception) {
            println(e)

            getChannel().exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT, true)
        }




        return this
    }


}