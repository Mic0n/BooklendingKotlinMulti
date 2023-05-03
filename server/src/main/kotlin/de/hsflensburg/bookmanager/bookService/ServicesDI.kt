package de.hsflensburg.bookmanager.bookService

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

fun bindBooks(): DI {
    return DI {
        bind<BookService>() with singleton { BookService() }
    }
}

fun bindUsers(): DI {
    return DI {
        bind<UserService>() with singleton { UserService() }
    }
}

