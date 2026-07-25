package com.xorko.backlogmanager.core.data.configuration

import com.xorko.backlogmanager.BuildKonfig
import com.xorko.backlogmanager.core.data.configuration.BuildType.DEBUG
import com.xorko.backlogmanager.core.data.configuration.BuildType.RELEASE

actual val currentBuildType: BuildType = if (BuildKonfig.IS_DEBUG) DEBUG else RELEASE
