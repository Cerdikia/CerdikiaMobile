package com.fhanafi.cerdikia.data.remote.retrofit

import com.fhanafi.cerdikia.data.remote.request.LoginRequest
import com.fhanafi.cerdikia.data.remote.request.LogsSiswaRequest
import com.fhanafi.cerdikia.data.remote.request.RegisterRequest
import com.fhanafi.cerdikia.data.remote.request.TokenRequest
import com.fhanafi.cerdikia.data.remote.request.UpdatePointRequest
import com.fhanafi.cerdikia.data.remote.request.UpdateProfileRequest
import com.fhanafi.cerdikia.data.remote.response.GetMapelResponse
import com.fhanafi.cerdikia.data.remote.response.GetMateriResponse
import com.fhanafi.cerdikia.data.remote.response.GetPointResponse
import com.fhanafi.cerdikia.data.remote.response.LoginResponse
import com.fhanafi.cerdikia.data.remote.response.LogsSiswaResponse
import com.fhanafi.cerdikia.data.remote.response.RangkingResponse
import com.fhanafi.cerdikia.data.remote.response.RegisterResponse
import com.fhanafi.cerdikia.data.remote.response.SoalResponse
import com.fhanafi.cerdikia.data.remote.response.TokenResponse
import com.fhanafi.cerdikia.data.remote.response.UpdatePointResponse
import com.fhanafi.cerdikia.data.remote.response.UpdateProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @POST("refresh")
    suspend fun refreshToken(
        @Body tokenRequest: TokenRequest
    ): Response<TokenResponse>

    @GET("ranking")
    suspend fun getRangking(
        @Query("id_kelas") idKelas: Int
    ): RangkingResponse

    @GET("genericMapels")
    suspend fun getMapel(
        @Query("id_kelas") idKelas: Int,
        @Query("finished") idMapel: Boolean
    ): GetMapelResponse

    @GET("genericModules")
    suspend fun getMateri(
        @Query("id_mapel") idMapel: Int,
        @Query("id_kelas") idKelas: Int,
        @Query("finished") finished: Boolean,
        @Query("email") email: String
    ): GetMateriResponse

    @POST("logs")
    suspend fun postLogSiswa(
        @Body logsSiswaRequest: LogsSiswaRequest
    ): LogsSiswaResponse

    @GET("genericSoal/{id_module}")
    suspend fun getSoal(
        @Path("id_module") idModule: Int
    ): SoalResponse
}
