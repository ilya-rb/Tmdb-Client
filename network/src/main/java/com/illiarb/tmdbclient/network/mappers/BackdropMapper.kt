package com.illiarb.tmdbclient.network.mappers

import com.illiarb.tmdbclient.network.responses.BackdropResponse
import com.illiarb.tmdblcient.core.entity.Backdrop
import javax.inject.Inject

class BackdropMapper @Inject constructor() : Mapper<BackdropResponse, Backdrop> {

    override fun map(from: BackdropResponse): Backdrop = Backdrop(from.filePath)
}