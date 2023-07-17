package com.panassevich.panassevich.component.loans.commonclasses.presentation.state

sealed class ScreenState<out T> {

    object Initial : ScreenState<Nothing>()

    object Loading : ScreenState<Nothing>()

    data class Content<T>(val content: T) : ScreenState<T>()

    object Error : ScreenState<Nothing>()

    object Success : ScreenState<Nothing>()

}
