package com.thewyp.di

import com.thewyp.data.repository.follow.FollowRepository
import com.thewyp.data.repository.follow.FollowRepositoryImpl
import com.thewyp.data.repository.post.PostRepository
import com.thewyp.data.repository.post.PostRepositoryImpl
import com.thewyp.data.repository.user.UserRepository
import com.thewyp.data.repository.user.UserRepositoryImpl
import com.thewyp.service.FollowService
import com.thewyp.service.PostService
import com.thewyp.service.UserService
import com.thewyp.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        val db = client.getDatabase(Constants.DATABASE_NAME)
        db
    }
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single {
        UserService(get())
    }
    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }
    single {
        FollowService(get())
    }
    single<PostRepository> {
        PostRepositoryImpl(get())
    }
    single {
        PostService(get())
    }
}