package com.fhanafi.cerdikia.data.remote.retrofit

import com.fhanafi.cerdikia.data.remote.request.LoginRequest
import com.fhanafi.cerdikia.data.remote.request.RegisterRequest
import com.fhanafi.cerdikia.data.remote.response.LoginResponse
import com.fhanafi.cerdikia.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("register/siswa")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse
}
