package com.fhanafi.cerdikia.data.remote.retrofit

import com.fhanafi.cerdikia.data.remote.request.LoginRequest
import com.fhanafi.cerdikia.data.remote.request.RegisterRequest
import com.fhanafi.cerdikia.data.remote.request.UpdatePointRequest
import com.fhanafi.cerdikia.data.remote.request.UpdateProfileRequest
import com.fhanafi.cerdikia.data.remote.response.GetPointResponse
import com.fhanafi.cerdikia.data.remote.response.LoginResponse
import com.fhanafi.cerdikia.data.remote.response.RegisterResponse
import com.fhanafi.cerdikia.data.remote.response.UpdatePointResponse
import com.fhanafi.cerdikia.data.remote.response.UpdateProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("register/siswa")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @PUT("editDataUser/siswa")
    suspend fun updateProfile(
        @Body updateProfileRequest: UpdateProfileRequest
    ): UpdateProfileResponse

    @GET("point")
    suspend fun getPoint(

    ): GetPointResponse

    @PUT("point")
    suspend fun updatePoint(
        @Body updatePointRequest: UpdatePointRequest
    ): UpdatePointResponse
}
