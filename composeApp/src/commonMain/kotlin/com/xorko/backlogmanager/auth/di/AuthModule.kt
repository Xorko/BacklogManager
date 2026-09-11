package com.xorko.backlogmanager.auth.di

import com.xorko.backlogmanager.auth.data.SupabaseAuthRepository
import com.xorko.backlogmanager.auth.domain.AuthRepository
import com.xorko.backlogmanager.auth.presentation.login.LoginViewModel
import com.xorko.backlogmanager.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule =
    module {
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)

        singleOf(::SupabaseAuthRepository).bind(AuthRepository::class)
    }
