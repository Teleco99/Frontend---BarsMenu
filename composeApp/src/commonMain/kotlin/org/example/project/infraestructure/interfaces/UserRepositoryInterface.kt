package org.example.project.infraestructure.interfaces

import org.example.project.domain.model.UserModel

interface UserRepositoryInterface {
    suspend fun insertUser(userModel: UserModel): UserModel?
    suspend fun updateUser(userId: Int, updatedUser: UserModel): Result<Unit>
    suspend fun deleteUser(userId: Int): Result<Unit>
}