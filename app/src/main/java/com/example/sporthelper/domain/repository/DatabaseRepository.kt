package com.example.sporthelper.domain.repository

import com.example.sporthelper.domain.model.BodyPart
import com.example.sporthelper.domain.model.BodyPartValue
import com.example.sporthelper.domain.model.User
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getSignedInUser(): Flow<User?>
    fun getAllBodyParts(): Flow<List<BodyPart>>
    fun getAllBodyPartsWithLatestValue(): Flow<List<BodyPart>>
    fun getAllBodyPartValues(bodyPartId: String): Flow<List<BodyPartValue>>
    fun getBodyPart(bodyPart: String): Flow<BodyPart?>
    suspend fun addUser(): Result<Boolean>
    suspend fun upsertBodyPart(bodyPart: BodyPart): Result<Boolean>
    suspend fun deleteBodyPart(bodyPart: String): Result<Boolean>
    suspend fun upsertBodyPartValue(bodyPartValue: BodyPartValue): Result<Boolean>
    suspend fun deleteBodyPartValue(bodyPartValue: BodyPartValue): Result<Boolean>
}