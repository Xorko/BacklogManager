package com.xorko.backlogmanager.core.data.configuration

import com.xorko.backlogmanager.BuildConfig
import com.xorko.backlogmanager.core.data.configuration.BuildType.DEBUG
import com.xorko.backlogmanager.core.data.configuration.BuildType.RELEASE

actual val currentBuildType: BuildType = if (BuildConfig.DEBUG) DEBUG else RELEASE
