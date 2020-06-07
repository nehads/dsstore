package com.nehads.friends.model.remote

data class ResponseModel<T> (
    val successData: T? = null,
    val error: ErrorModel? = null
)