package com.example.appmarvel.presenter.View.CharacterViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmarvel.data.marvelresponse.CharacterResponse
import com.example.appmarvel.repository.MarvelRepository
import com.example.appmarvel.util.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: MarvelRepository,
) : ViewModel() {
    private val _characterList =
        MutableStateFlow<ResourceState<CharacterResponse>>(ResourceState.Load())
    val characterList: StateFlow<ResourceState<CharacterResponse>> = _characterList

    init {
        getCharacters()
    }


    private fun getCharacters() = viewModelScope.launch {

        try {
            val response = repository.getCharacters()
            _characterList.value = callResponse(response)

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _characterList.value =
                    ResourceState.Error("ERRO DE CONEXÃO COM A INTERNET")

                else -> _characterList.value = ResourceState.Error("ERRO NA CONVERSÃO DE DADOS")
            }
        }
    }


    fun getSearchedCharacters(search: String) = viewModelScope.launch {
        try {
            val response = repository.getCharacters(search)
            _characterList.value = callResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _characterList.value =
                    ResourceState.Error("Erro de conexão com a internet")

                else -> _characterList.value = ResourceState.Error("Erro ao converter os dados")
            }
        }
    }

    private fun callResponse(response: Response<CharacterResponse>): ResourceState<CharacterResponse> {
        if (response.isSuccessful) {
            response.body().let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }
}