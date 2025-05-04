package com.xorko.backlogmanager.core.di

import com.xorko.backlogmanager.core.data.HttpClientFactory
import com.xorko.backlogmanager.core.data.SupabaseClientFactory
import com.xorko.backlogmanager.core.data.SupabaseClientFactory.create
import com.xorko.backlogmanager.core.data.logging.KermitAppLogger
import com.xorko.backlogmanager.core.domain.logging.AppLogger
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule =
    module {
        single<AppLogger> { KermitAppLogger }
        singleOf(HttpClientFactory::create)
        singleOf(SupabaseClientFactory::create).bind(SupabaseClient::class)
        single<Auth> { get<SupabaseClient>().auth }
        single<Postgrest> { get<SupabaseClient>().postgrest }
    }
