package meea.uid.subject3_muresansalomeea.api

import meea.uid.subject3_muresansalomeea.dto.BreedResponse
import meea.uid.subject3_muresansalomeea.dto.ImageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET("breeds/list/all")
    fun getDogBreeds(): Call<BreedResponse>

    @GET("breed/{breed}/images")
    fun getDogImages(@Path("breed") breed: String): Call<ImageResponse>
}