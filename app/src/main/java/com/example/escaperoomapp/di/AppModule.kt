package com.example.escaperoomapp.di

import com.example.escaperoomapp.data.repository.FakePuzzleRepository
import com.example.escaperoomapp.data.repository.FakeRoomRepository
import com.example.escaperoomapp.data.repository.GameResultRepository
import com.example.escaperoomapp.ui.viewmodel.GameViewModel
import com.example.escaperoomapp.ui.viewmodel.HomeViewModel
import com.example.escaperoomapp.ui.viewmodel.ResultViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FakeRoomRepository() }
    single { FakePuzzleRepository() }
    single { GameResultRepository(androidContext()) }
    viewModel { HomeViewModel(get()) }
    viewModel { GameViewModel(get()) }
    viewModel { ResultViewModel(get()) }
}
