package com.xorko.backlogmanager.core.presentation

import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.error_disk_full
import backlogmanager.composeapp.generated.resources.error_no_internet
import backlogmanager.composeapp.generated.resources.error_request_timeout
import backlogmanager.composeapp.generated.resources.error_serialization
import backlogmanager.composeapp.generated.resources.error_too_many_request
import backlogmanager.composeapp.generated.resources.error_unknown
import com.xorko.backlogmanager.core.domain.DataError

fun DataError.toUiText() =
    UiText.StringResourceId(
        when (this) {
            DataError.Local.DISK_FULL -> Res.string.error_disk_full
            DataError.Local.UNKNOWN -> Res.string.error_unknown
            DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
            DataError.Remote.TOO_MANY_REQUEST -> Res.string.error_too_many_request
            DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
            DataError.Remote.SERIALIZATION -> Res.string.error_serialization
            DataError.Remote.SERVER -> Res.string.error_unknown
            DataError.Remote.UNKNOWN -> Res.string.error_unknown
        },
    )
