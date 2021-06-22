package com.example.test_mqtt_mosq

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*


class MainActivity : AppCompatActivity() {
    private lateinit var edtMessage: EditText
    private lateinit var txtIndicator: TextView
    private lateinit var btnPublish: Button
    private lateinit var btnConnect: Button
    private lateinit var btnDisconnect: Button

    //MQTT Android
    private lateinit var mqttAndroidClient: MqttAndroidClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtMessage = findViewById(R.id.edtInputMessage)
        txtIndicator = findViewById(R.id.txtIndicator)

        btnPublish = findViewById(R.id.btnPublish)
        btnConnect = findViewById(R.id.btnConnect)
        btnDisconnect = findViewById(R.id.btnDisconnect)

        var clientID = MqttClient.generateClientId()
        //Pakai broker hivemq
        mqttAndroidClient =
            MqttAndroidClient(this.applicationContext, "tcp://broker.hivemq.com:1883", clientID)

        connectionChecking(mqttAndroidClient)

        btnPublish.setOnClickListener {
            var messageToPublish = edtMessage.text.toString()
            testPublish(messageToPublish)
        }

        btnConnect.setOnClickListener {
            testConnect(mqttAndroidClient)
        }

        btnDisconnect.setOnClickListener {
            testDisconnect(mqttAndroidClient)
        }
    }

    private fun connectionChecking(client: MqttAndroidClient) {
        try {
            var tokens: IMqttToken? = client.connect()
            tokens?.setActionCallback(object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Toast.makeText(this@MainActivity, "connected!!", Toast.LENGTH_LONG).show()
                    setSubscription(client)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "connection failed!!" + exception.message,
                        Toast.LENGTH_LONG
                    )
                        .show()
                    println(exception.printStackTrace())
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }

        client.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {

            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                txtIndicator.text = message.toString()
                println(message)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }
        })
    }

    private fun setSubscription(client: MqttAndroidClient) {
        try {
            client.subscribe("testtopic45/1", 1)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun testDisconnect(client: MqttAndroidClient) {
        try {
            var tokens: IMqttToken? = client.connect()
            tokens?.setActionCallback(object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Toast.makeText(this@MainActivity, "connected!!", Toast.LENGTH_LONG).show()
                    setSubscription(client)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Toast.makeText(this@MainActivity, "connection failed!!", Toast.LENGTH_LONG)
                        .show()
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun testConnect(client: MqttAndroidClient) {
        try {
            var tokens: IMqttToken? = client.connect()
            tokens?.setActionCallback(object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Toast.makeText(this@MainActivity, "connected!!", Toast.LENGTH_LONG).show()
                    setSubscription(client)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Toast.makeText(this@MainActivity, "connection failed!!", Toast.LENGTH_LONG)
                        .show()
                    println(exception.printStackTrace())
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun testPublish(message: String) {
        var topics = "testtopic45/1"

        try {
            mqttAndroidClient.publish(topics, message.toByteArray(), 0, false)
            Toast.makeText(this, "Published Message", Toast.LENGTH_LONG).show()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}