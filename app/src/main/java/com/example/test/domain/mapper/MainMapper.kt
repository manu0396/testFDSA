package com.example.test.domain.mapper

import com.example.test.data.models.UserModel
import com.example.test.domain.models.DomainModel

object MainMapper {
    fun convertMainModelToDomainModel(input: UserModel): List<DomainModel> {
        return input.results.map {
            DomainModel(
                email = it.email,
                name = "${it.name.first} ${it.name.last}",
                picture = it.picture.medium,
                location = it.location.city
            )
        }
    }
}