package com.example.test.data.models


interface IDestination {
    fun GetAll(): List<Destination>
    fun DeleteById(id: Int)
    fun Update(destination: Destination)
    fun Create(destination: Destination)
}