package com.xorko.backlogmanager.modules.igdb.proxy.di

import com.xorko.backlogmanager.di.Qualifiers.IGDB_HTTP_CLIENT
import com.xorko.backlogmanager.modules.igdb.proxy.services.IgdbProxyService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val igdbProxyModule =
    module {
        single { IgdbProxyService(get(named(IGDB_HTTP_CLIENT))) }
    }
