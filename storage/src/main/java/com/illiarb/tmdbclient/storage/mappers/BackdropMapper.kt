package com.illiarb.tmdbclient.storage.mappers

import com.illiarb.tmdbclient.storage.dto.BackdropDto
import com.illiarb.tmdblcient.core.entity.Backdrop
import javax.inject.Inject

class BackdropMapper @Inject constructor() : Mapper<BackdropDto, Backdrop> {

    override fun map(from: BackdropDto): Backdrop = Backdrop(from.filePath)
}