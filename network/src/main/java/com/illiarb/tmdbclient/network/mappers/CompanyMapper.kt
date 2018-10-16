package com.illiarb.tmdbclient.network.mappers

import com.illiarb.tmdbclient.network.responses.CompanyResponse
import com.illiarb.tmdblcient.core.entity.Company
import javax.inject.Inject

class CompanyMapper @Inject constructor() : Mapper<CompanyResponse, Company> {

    override fun map(from: CompanyResponse): Company = Company(from.id, from.name)
}