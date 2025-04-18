package com.xorko.backlogmanager.core.data.configuration

import com.xorko.backlogmanager.core.data.configuration.BuildType.DEBUG
import com.xorko.backlogmanager.core.data.configuration.BuildType.RELEASE
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual val currentBuildType: BuildType = if (Platform.isDebugBinary) DEBUG else RELEASE
