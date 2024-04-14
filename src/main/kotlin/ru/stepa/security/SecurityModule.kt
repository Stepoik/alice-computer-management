package ru.stepa.security

import org.koin.dsl.module

val securityModule = module {
    single { JwtConfig() }
}