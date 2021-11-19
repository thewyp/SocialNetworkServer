package com.thewyp

import com.thewyp.di.mainModule
import io.ktor.application.*
import com.thewyp.plugins.*
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(Koin) {
        modules(mainModule)
    }
    configureSecurity()
    configureRouting()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
}
