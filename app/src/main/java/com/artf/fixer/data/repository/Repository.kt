package com.artf.fixer.data.repository

import com.artf.fixer.data.model.Rate

interface Repository {
    fun getRates(date: String): Listing<Rate>
}