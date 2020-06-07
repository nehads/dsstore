package com.nehads.friends.main.landing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nehads.friends.di.repository.DataRepository
import com.nehads.friends.model.DTO.HistoryModel
import com.nehads.friends.model.remote.EpisodesItem
import com.nehads.friends.model.remote.SeasonsItem
import com.nehads.friends.util.Constants
import com.nehads.friends.util.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LandingViewModel @Inject
constructor(
    var appDataRepository: DataRepository,
    private val mCompositeDisposable: CompositeDisposable
) : ViewModel() {
    var isLoading = MutableLiveData<Boolean>()
    var errorData = MutableLiveData<String>()

    var seasonsList = MutableLiveData<List<SeasonsItem>>()
    var specials = MutableLiveData<SeasonsItem>()
    var episodesList = MutableLiveData<Pair<List<EpisodesItem>, Int>>()
    var historyList = MutableLiveData<List<HistoryModel>>()

    private var episodeList = listOf<EpisodesItem>()
    private var filteredList = listOf<HistoryModel>()

    /* pass Tv ID which is static in Constants to API */
    fun getSeasons() {
        isLoading.value = true
        mCompositeDisposable += appDataRepository.getAllSeasons(Constants.TV_ID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isLoading.value = false
                    if (it.error != null) {
                        errorData.value = it.error.status_message
                    } else {
                        seasonsList.value = it.successData?.seasons?.drop(1)
                        specials.value = it.successData?.seasons?.get(0)
                    }
                }, { error ->
                    error?.printStackTrace()
                    errorData.value = error.message
                }
            )
    }

    fun getEpisodes(seasonNo: Int) {
        isLoading.value = true
        mCompositeDisposable += appDataRepository.getEpisodesInSeason(Constants.TV_ID, seasonNo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isLoading.value = false
                    if (it.error != null) {
                        errorData.value = it.error.status_message
                    } else {
                        episodeList = it.successData?.episodes ?: ArrayList()
                        episodesList.value = Pair(episodeList, 0)
                    }

                }, { error ->
                    error?.printStackTrace()
                    errorData.value = error.message
                }
            )
    }

    fun addHistoryItems() {
        val list = ArrayList<HistoryModel>()

        list.add(HistoryModel(1, "Nov 13, 2019", "Season1", "Episode-2"))
        list.add(HistoryModel(2, "Nov 18, 2019", "Season1", "Episode-4"))
        list.add(HistoryModel(3, "Oct 27, 2019", "Season1", "Episode-6"))
        list.add(HistoryModel(4, "Oct 15, 2019", "Season3", "Episode-7"))
        list.add(HistoryModel(5, "Oct 01, 2019", "Season3", "Episode-8"))
        list.add(HistoryModel(6, "Sept 03, 2019", "Season4", "Episode-9"))
        list.add(HistoryModel(7, "Oct 01, 2019", "Season7", "Episode-1"))
        list.add(HistoryModel(8, "Sept 03, 2019", "Season8", "Episode-4"))
        list.add(HistoryModel(9, "Oct 15, 2019", "Season8", "Episode-9"))

        filteredList = list
        historyList.value = list
    }

    /* Sort by vote_count/vote_average */
    fun setSortedList(sortSelection: Int) {
        when (sortSelection) {
            0 -> {
                episodesList.value = Pair(episodeList, 0)
            }
            1 -> {
                episodesList.value = Pair(episodeList.sortedBy { it.voteCount }, 1)
            }
            2 -> {
                episodesList.value = Pair(episodeList.sortedBy { it.voteAverage }, 2)
            }
        }
    }

    /* Filter by season name if present
    * Sub -1 coz array starts from 0 */
    fun setFilterSelected(filterSelection: Int) {
        if (filterSelection == 0)
            historyList.value = filteredList
        else
            historyList.value =
                this.filteredList.filter { it.season.equals(Constants.SEASON_ARRAY[filterSelection - 1]) }
    }
}