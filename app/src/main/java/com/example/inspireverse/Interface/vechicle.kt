package com.example.inspireverse.Interface

class vechicle() :CanGo,CanStop {
    override val name: String
        get()="Honda"
    override fun stop() {
        println("Car can stop")
    }
}