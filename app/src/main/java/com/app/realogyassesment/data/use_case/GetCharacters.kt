package com.app.realogyassesment.data.use_case

import com.app.realogyassesment.common.Constants.ERROR_HTTP_EXCEPTION
import com.app.realogyassesment.common.Constants.ERROR_IO_EXCEPTION
import com.app.realogyassesment.common.Constants.ERROR_UNKNOWN_EXCEPTION
import com.app.realogyassesment.common.Resource
import com.app.realogyassesment.data.remote.dto.toCharacter
import com.app.realogyassesment.domain.model.CharacterObject
import com.app.realogyassesment.domain.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCharacters @Inject constructor(
    private val repo: CharactersRepository
) {

    operator fun invoke()
            : Flow<Resource<List<CharacterObject>>> = flow {
        try {
            emit(Resource.Loading<List<CharacterObject>>())
            val characters = repo.getCharacters().map { it.toCharacter() }

            emit(Resource.Success<List<CharacterObject>>(characters))
        } catch (e: HttpException) {
            emit(Resource.Error<List<CharacterObject>>(e.localizedMessage ?: ERROR_HTTP_EXCEPTION))
        } catch (e: IOException) {
            emit(Resource.Error<List<CharacterObject>>(ERROR_IO_EXCEPTION))
        } catch (e: Exception) {
            emit(
                Resource.Error<List<CharacterObject>>(e.localizedMessage ?: ERROR_UNKNOWN_EXCEPTION)
            )
        }
    }
}