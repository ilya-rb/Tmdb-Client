package com.illiarb.tmdbclient.storage.mappers

import com.illiarb.tmdbclient.storage.model.BackdropModel
import com.illiarb.tmdblcient.core.entity.Backdrop
import javax.inject.Inject

class BackdropMapper @Inject constructor() : Mapper<BackdropModel, Backdrop> {
    override fun map(from: BackdropModel): Backdrop = Backdrop(from.filePath)
}