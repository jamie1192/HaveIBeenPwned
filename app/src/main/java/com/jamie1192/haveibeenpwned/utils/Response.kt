package com.jamie1192.haveibeenpwned.utils

import androidx.annotation.NonNull

/**
 * Created by jamie1192 on 9/1/19.
 */
data class Response<T>(val status: Int, val data: T?, val errorType: Int?) {

    fun isError() = status == STATUS_ERROR

    companion object {
        const val STATUS_LOADING = 0
        const val STATUS_SUCCESS = 1
        const val STATUS_ERROR = -1

        /**
         * Helper method to create fresh state resource
         */
        fun <T> success(@NonNull data: T): Response<T> {
            return Response(STATUS_SUCCESS, data, null)
        }

        /**
         * Helper method to create error state Resources. Error state might also have the current data, if any
         */
        fun <T> error(item: T? = null): Response<T> {
            return Response(STATUS_ERROR, item, null)
        }

        /**
         * Helper methos to create loading state Resources.
         */
        fun <T> loading( data: T? = null): Response<T> {
            return Response(STATUS_LOADING, data, null)
        }
    }
}