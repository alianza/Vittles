package com.example.vittles.extension

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.SerialDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.functions.Function


fun <T> Observable<T>.subscribeOnIoObserveOnMain(): Observable<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun Completable.subscribeOnIoObserveOnMain(): Completable =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.subscribeOnIoObserveOnMain(): Single<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun Disposable.setTo(serialDisposable: SerialDisposable) = serialDisposable.set(this)

fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable =
    apply { compositeDisposable.add(this) }

/**
 * Map list items by combining the reactive map method with the kotlin map.
 */
inline fun <T, R> Observable<List<T>>.mapListItems(crossinline transform: (T) -> R): Observable<List<R>> =
    map(mapList(transform))

/**
 * Function to map list items in a reactive stream
 */
inline fun <T, R> mapList(crossinline transform: (T) -> R): io.reactivex.functions.Function<List<T>, List<R>> =
    Function { list ->
        list.map { item -> transform(item) }
    }