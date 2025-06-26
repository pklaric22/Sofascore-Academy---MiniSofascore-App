package com.example.minisofascoreapp.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

private const val INITIAL_PAGE = 0

interface PaginatedSource<out T> {
    val items: Flow<List<T>>
    fun nextPage()
    fun reset()
}

open class PaginatedEndpoint<T, R>(
    private val pageSource: suspend (page: Int) -> List<T>,
    private val mapper: suspend (List<T>) -> List<R>
) : PaginatedSource<R> {

    private val isLastPage = AtomicBoolean(false)
    private val resetTrigger = MutableSharedFlow<Unit>(replay = 1)
    private val nextPageTrigger = MutableSharedFlow<Unit>()

    private val scope = CoroutineScope(Dispatchers.IO)

    protected val _items: Flow<List<R>> by lazy {
        resetTrigger.tryEmit(Unit)

        val mergedTriggers = merge(
            resetTrigger.map { true },
            nextPageTrigger.map { false }
        )

        mergedTriggers
            .scan(Pair(emptyList<R>(), INITIAL_PAGE)) { (acc, currentPage), isReset ->
                val pageToLoad = if (isReset) INITIAL_PAGE else currentPage + 1
                val raw = try {
                    pageSource(pageToLoad)
                } catch (e: Exception) {
                    emptyList()
                }

                if (raw.isEmpty()) {
                    isLastPage.set(true)
                    if (isReset) Pair(emptyList(), INITIAL_PAGE) else Pair(acc, currentPage)
                } else {
                    val mapped = mapper(raw)
                    if (isReset) Pair(mapped, pageToLoad)
                    else Pair(acc + mapped, pageToLoad)
                }
            }
            .map { it.first }
            .distinctUntilChanged()
    }

    override val items: Flow<List<R>> get() = _items

    override fun nextPage() {
        if (!isLastPage.get()) {
            scope.launch {
                nextPageTrigger.emit(Unit)
            }
        }
    }

    override fun reset() {
        isLastPage.set(false)
        scope.launch {
            resetTrigger.emit(Unit)
        }
    }
}