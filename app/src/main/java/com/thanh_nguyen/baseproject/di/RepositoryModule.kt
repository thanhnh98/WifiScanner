package com.thanh_nguyen.baseproject.di

import com.thanh_nguyen.baseproject.repo.LoginRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


/**
 * module for repository dependencies
 */

const val REPO_MODULE = "repo_module"

val repositoryModule = Kodein.Module(REPO_MODULE, false){
    bind() from singleton {
        LoginRepository(instance())
    }
}