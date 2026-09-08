package com.ai.ecommerce.presentation.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.ecommerce.data.remote.ApiConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

@Serializable
data class AuthUser(
    val id: Long,
    val name: String,
    val email: String
)

@Serializable
private data class AuthRequest(
    val name: String? = null,
    val email: String,
    val password: String
)

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Error(val message: String) : AuthUiState
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val client: HttpClient
) : ViewModel() {

    private val jsonParser = Json { ignoreUnknownKeys = true }

    private val _currentUser = mutableStateOf<AuthUser?>(null)
    val currentUser: State<AuthUser?> = _currentUser

    private val _uiState = mutableStateOf<AuthUiState>(AuthUiState.Idle)
    val uiState: State<AuthUiState> = _uiState

    fun login(email: String, password: String) {
        authenticate(
            endpoint = "auth/login",
            request = AuthRequest(email = email.trim(), password = password)
        )
    }

    fun register(name: String, email: String, password: String) {
        authenticate(
            endpoint = "auth/register",
            request = AuthRequest(name = name.trim(), email = email.trim(), password = password)
        )
    }

    fun logout() {
        _currentUser.value = null
        _uiState.value = AuthUiState.Idle
    }

    private fun authenticate(endpoint: String, request: AuthRequest) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val response = client.post("${ApiConfig.BASE_URL}$endpoint") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }

                if (response.status.isSuccess()) {
                    val user = response.body<AuthUser>()
                    _currentUser.value = user
                    _uiState.value = AuthUiState.Idle
                } else {
                    val rawBody = response.bodyAsText()
                    val errorMessage = try {
                        val jsonObj = jsonParser.parseToJsonElement(rawBody).jsonObject
                        jsonObj["error"]?.jsonPrimitive?.content ?: "Authentication failed."
                    } catch (_: Exception) {
                        "Authentication failed: ${response.status.description}"
                    }
                    _uiState.value = AuthUiState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Connection error.")
            }
        }
    }
}
