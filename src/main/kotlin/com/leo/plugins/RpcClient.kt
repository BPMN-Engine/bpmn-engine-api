import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rabbitmq.client.*
import java.util.*
import java.util.concurrent.CompletableFuture

class RPCClient : AutoCloseable {
    private val connection: Connection
    private val channel: Channel
    private val requestQueueName = "rpc_queue"

    init {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        connection = factory.newConnection()
        channel = connection.createChannel()
    }


    fun call(msg: Map<Any,Any>): String {
        val message = jacksonObjectMapper().writeValueAsString(msg)
        val corrId = UUID.randomUUID().toString()
        val replyQueueName = channel.queueDeclare().queue
        val props = AMQP.BasicProperties.Builder()
            .correlationId(corrId)
            .replyTo(replyQueueName)
            .build()
        channel.basicPublish("", requestQueueName, props, message.toByteArray(charset("UTF-8")))
        val response = CompletableFuture<String>()
        val ctag = channel.basicConsume(replyQueueName, true,
            { consumerTag: String?, delivery: Delivery ->
                if (delivery.properties.correlationId == corrId) {
                    response.complete(delivery.body.decodeToString())
                }
            }
        ) { consumerTag: String? -> }
        val result = response.get()
        channel.basicCancel(ctag)
        return result
    }


    override fun close() {
        connection.close()
    }


}