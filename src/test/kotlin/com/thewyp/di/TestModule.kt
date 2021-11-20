package com.thewyp.di

import com.thewyp.data.repository.user.FakeUserRepository
import org.koin.dsl.module

internal val testModule = module {
    single {
        FakeUserRepository()
    }
}