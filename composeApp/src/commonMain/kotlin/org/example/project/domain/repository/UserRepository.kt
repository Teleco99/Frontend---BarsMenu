package org.example.project.domain.repository

import org.example.project.domain.model.UserModel
import org.example.project.domain.service.UserService
import org.example.project.infraestructure.interfaces.UserRepositoryInterface

class UserRepository(private val userService: UserService) : UserRepositoryInterface {
    override suspend fun insertUser(userModel: UserModel): UserModel? {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(userId: Int, updatedUser: UserModel): Result<Unit> {
        return if (userService.updateUser(userId, updatedUser)) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Error updating user"))
        }
    }

    override suspend fun deleteUser(userId: Int): Result<Unit> {
        return if (userService.deleteUser(userId)) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Error deleting user"))
        }
    }
}