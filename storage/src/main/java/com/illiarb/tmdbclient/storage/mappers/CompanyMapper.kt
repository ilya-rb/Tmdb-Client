package com.illiarb.tmdbclient.storage.mappers

import com.illiarb.tmdbclient.storage.dto.CompanyDto
import com.illiarb.tmdblcient.core.entity.Company
import javax.inject.Inject

class CompanyMapper @Inject constructor() : Mapper<CompanyDto, Company> {

    override fun map(from: CompanyDto): Company = Company(from.id, from.name)
}