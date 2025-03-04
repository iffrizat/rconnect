package io.iffrizat.rconnect.model

import java.io.Serializable

data class RconConfig (
    val name: String,
    val connectionString: String,
    val password: String,
) : Serializable