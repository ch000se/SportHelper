package com.example.sporthelper.data.repository

import com.example.sporthelper.data.mapper.BodyPartDto
import com.example.sporthelper.data.mapper.BodyPartValueDto
import com.example.sporthelper.data.mapper.UserDto
import com.example.sporthelper.data.mapper.toBodyPart
import com.example.sporthelper.data.mapper.toBodyPartDto
import com.example.sporthelper.data.mapper.toBodyPartValue
import com.example.sporthelper.data.mapper.toBodyPartValueDto
import com.example.sporthelper.data.mapper.toUser
import com.example.sporthelper.data.util.Constants.BODY_PART_COLLECTION
import com.example.sporthelper.data.util.Constants.BODY_PART_NAME_FIELD
import com.example.sporthelper.data.util.Constants.BODY_PART_VALUE_COLLECTION
import com.example.sporthelper.data.util.Constants.BODY_PART_VALUE_DATE_FIELD
import com.example.sporthelper.data.util.Constants.USER_COLLECTION
import com.example.sporthelper.domain.model.BodyPart
import com.example.sporthelper.domain.model.BodyPartValue
import com.example.sporthelper.domain.model.User
import com.example.sporthelper.domain.repository.DatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DatabaseRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
) : DatabaseRepository {

    private fun userCollection(): CollectionReference {
        return firebaseFirestore
            .collection(USER_COLLECTION)
    }

    private fun bodyPartCollection(
        userId: String = firebaseAuth.currentUser?.uid.orEmpty(),
    ): CollectionReference {
        return firebaseFirestore
            .collection(USER_COLLECTION)
            .document(userId)
            .collection(BODY_PART_COLLECTION)
    }

    private fun bodyPartValueCollection(
        bodyPartId: String,
        userId: String = firebaseAuth.currentUser?.uid.orEmpty(),
    ): CollectionReference {
        return firebaseFirestore
            .collection(USER_COLLECTION)
            .document(userId)
            .collection(BODY_PART_COLLECTION)
            .document(bodyPartId)
            .collection(BODY_PART_VALUE_COLLECTION)
    }

    override suspend fun addUser(): Result<Boolean> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: throw IllegalArgumentException("User is not authenticated")
            var userDto = UserDto(
                anonymous = currentUser.isAnonymous,
                userId = firebaseAuth.uid
            )
            currentUser.providerData.forEach { profile ->
                userDto = userDto.copy(
                    name = profile.displayName ?: userDto.name,
                    email = profile.email ?: userDto.email,
                    profilePictureUrl = profile.photoUrl?.toString() ?: userDto.profilePictureUrl
                )
            }

            userCollection()
                .document(currentUser.uid)
                .set(userDto)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSignedInUser(): Flow<User?> {
        return flow {
            try {
                val userId = firebaseAuth.currentUser?.uid.orEmpty()
                userCollection()
                    .document(userId)
                    .snapshots()
                    .collect { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val userDto = documentSnapshot.toObject(UserDto::class.java)
                            emit(userDto?.toUser())
                        } else {
                            emit(null)
                        }
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun upsertBodyPart(bodyPart: BodyPart): Result<Boolean> {
        return try {
            val documentId = bodyPart.bodyPart ?: bodyPartCollection().document().id
            val bodyPartDto = bodyPart.toBodyPartDto().copy(bodyPart = documentId)
            bodyPartCollection()
                .document(documentId)
                .set(bodyPartDto)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBodyPart(bodyPart: String): Result<Boolean> {
        return try {
            bodyPartCollection()
                .document(bodyPart)
                .delete()
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun upsertBodyPartValue(bodyPartValue: BodyPartValue): Result<Boolean> {
        return try {
            val bodyPartValueCollection =
                bodyPartValueCollection(bodyPartValue.bodyPartId.orEmpty())
            val documentId = bodyPartValue.bodyPartValueId ?: bodyPartValueCollection.document().id
            val bodyPartValueDto =
                bodyPartValue.toBodyPartValueDto().copy(bodyPartValueId = documentId)
            bodyPartValueCollection
                .document(documentId)
                .set(bodyPartValueDto)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getBodyPart(bodyPart: String): Flow<BodyPart?> {
        return flow {
            try {
                val userId = firebaseAuth.currentUser?.uid.orEmpty()
                bodyPartCollection(userId)
                    .document(bodyPart)
                    .snapshots()
                    .collect { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val bodyPartDto = documentSnapshot.toObject(BodyPartDto::class.java)
                            emit(bodyPartDto?.toBodyPart())
                        } else {
                            emit(null)
                        }
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getAllBodyParts(): Flow<List<BodyPart>> {
        return flow {
            try {
                val userId = firebaseAuth.currentUser?.uid.orEmpty()
                bodyPartCollection(userId)
                    .orderBy(BODY_PART_NAME_FIELD)
                    .snapshots()
                    .collect { querySnapshot ->
                        val bodyParts = querySnapshot.documents.mapNotNull { documentSnapshot ->
                            documentSnapshot.toObject(BodyPartDto::class.java)
                                ?.toBodyPart()
                        }
                        emit(bodyParts)
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getAllBodyPartValues(bodyPartId: String): Flow<List<BodyPartValue>> {
        return flow {
            try {
                val userId = firebaseAuth.currentUser?.uid.orEmpty()
                bodyPartValueCollection(bodyPartId, userId)
                    .orderBy(BODY_PART_VALUE_DATE_FIELD, Query.Direction.DESCENDING)
                    .snapshots()
                    .collect { querySnapshot ->
                        val bodyPartValues =
                            querySnapshot.documents.mapNotNull { documentSnapshot ->
                                documentSnapshot.toObject(BodyPartValueDto::class.java)
                                    ?.toBodyPartValue()
                            }
                        emit(bodyPartValues)
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun deleteBodyPartValue(bodyPartValue: BodyPartValue): Result<Boolean> {
        return try {
            val bodyPartId = bodyPartValue.bodyPartId
                ?: throw IllegalArgumentException("Body part ID is null")
            val bodyPartValueId = bodyPartValue.bodyPartValueId
                ?: throw IllegalArgumentException("Body part value ID is null")
            bodyPartValueCollection(bodyPartId)
                .document(bodyPartValueId)
                .delete()
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllBodyPartsWithLatestValue(): Flow<List<BodyPart>> {
        return flow {
            try {
                val userId = firebaseAuth.currentUser?.uid.orEmpty()
                bodyPartCollection(userId)
                    .orderBy(BODY_PART_NAME_FIELD)
                    .snapshots()
                    .collect { querySnapshot ->
                        val bodyParts = querySnapshot.documents.mapNotNull { documentSnapshot ->
                            documentSnapshot.toObject(BodyPartDto::class.java)
                                ?.toBodyPart()
                        }

                        val bodyPartsWithLatestValues = bodyParts.map { bodyPart ->
                            val latestValue = bodyPartValueCollection(
                                bodyPartId = bodyPart.bodyPart.orEmpty(),
                                userId = userId
                            )
                                .orderBy(BODY_PART_VALUE_DATE_FIELD, Query.Direction.DESCENDING)
                                .limit(1)
                                .get()
                                .await()
                                .documents
                                .firstOrNull()
                                ?.toObject(BodyPartValueDto::class.java)
                                ?.toBodyPartValue()
                                ?.value
                            bodyPart.copy(latestValue = latestValue)
                        }
                        emit(bodyPartsWithLatestValues)
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }
}