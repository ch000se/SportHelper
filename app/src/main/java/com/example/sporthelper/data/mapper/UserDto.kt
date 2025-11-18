package com.example.sporthelper.data.mapper

import com.example.sporthelper.domain.model.User

data class UserDto(
    val name: String = "Anonymous",
    val email: String = "anonymous@sporthelper.com",
    val profilePictureUrl: String = "",
    val anonymous: Boolean = true,
    val userId: String? = null,
)

fun UserDto.toUser(): User {
    return User(
        name = name,
        email = email,
        profilePictureUrl = profilePictureUrl,
        isAnonymous = anonymous,
        userId = userId
    )
}