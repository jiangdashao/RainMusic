package me.rerere.rainmusic.ui.screen.index

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.rerere.rainmusic.repo.MusicRepo
import me.rerere.rainmusic.repo.UserRepo
import me.rerere.rainmusic.retrofit.api.model.AccountDetail
import me.rerere.rainmusic.retrofit.weapi.model.PersonalizedPlaylist
import me.rerere.rainmusic.retrofit.weapi.model.NewSongs
import me.rerere.rainmusic.util.DataState
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val musicRepo: MusicRepo
): ViewModel() {
    val accountDetail: MutableStateFlow<DataState<AccountDetail>> = MutableStateFlow(DataState.Empty)

    // index page
    val personalizedPlaylist: MutableStateFlow<DataState<PersonalizedPlaylist>> = MutableStateFlow(DataState.Empty)
    val personalizedSongs: MutableStateFlow<DataState<NewSongs>> = MutableStateFlow(DataState.Empty)

    private fun refreshAccountDetail() {
        userRepo.getAccountDetail()
            .onEach {
                accountDetail.value = it
            }
            .launchIn(viewModelScope)
    }

    fun refreshIndexPage(){
        musicRepo.getPersonalizedPlaylist(10)
            .onEach {
                personalizedPlaylist.value = it
            }
            .launchIn(viewModelScope)
        musicRepo.getNewSongs()
            .onEach {
                personalizedSongs.value = it
            }
            .launchIn(viewModelScope)
    }

    init {
        refreshAccountDetail()
        refreshIndexPage()
    }
}