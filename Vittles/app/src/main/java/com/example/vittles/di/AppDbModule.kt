package com.example.vittles.di

import android.content.Context
import com.example.data.createProductDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppDbModule {

    @Singleton
    @Provides
    fun provideNoteDao(context: Context) = createProductDao(context)

//    @Singleton
//    @Provides
//    fun provideNoteModelMapper() = NoteModelMapperImpl()
//
//    @Singleton
//    @Provides
//    fun provideNoteRepository(noteDao: NoteDaoImpl, mapper: NoteModelMapperImpl): NoteRepository = NoteRepositoryImpl(noteDao, mapper)
}