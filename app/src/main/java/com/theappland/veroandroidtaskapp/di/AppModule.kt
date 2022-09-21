package com.theappland.veroandroidtaskapp.di

import android.content.Context
import androidx.room.Room
import com.theappland.veroandroidtaskapp.api.RefreshTokenApi
import com.theappland.veroandroidtaskapp.api.TaskApi
import com.theappland.veroandroidtaskapp.api.TokenAuthenticator
import com.theappland.veroandroidtaskapp.db.TaskDao
import com.theappland.veroandroidtaskapp.db.TaskDatabase
import com.theappland.veroandroidtaskapp.repository.TaskRepository
import com.theappland.veroandroidtaskapp.repository.TaskRepositoryInterface
import com.theappland.veroandroidtaskapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTaskRepository(taskApi: TaskApi, taskDao: TaskDao) = TaskRepository(taskApi, taskDao) as TaskRepositoryInterface

    @Singleton
    @Provides
    fun provideTaskApi(okHttpClient: OkHttpClient) : TaskApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
            .create(TaskApi::class.java)
    }

    @Singleton
    @Provides
    fun okHttpClient(tokenAuthenticator: TokenAuthenticator) : OkHttpClient {
        return OkHttpClient().newBuilder()
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Singleton
    @Provides
    fun provideRefreshTokenApi() : RefreshTokenApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.baubuddy.de/index.php/")
            .build()
            .create(RefreshTokenApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTaskDao(taskDatabase: TaskDatabase) = taskDatabase.taskDao()
    
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        TaskDatabase::class.java,
        "TaskDatabase"
    ).build()
}