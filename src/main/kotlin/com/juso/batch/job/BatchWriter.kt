package com.juso.batch.job

import com.juso.batch.domain.MatchBuild
import com.juso.batch.infra.repository.MatchBuildRepository
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class MatchBuildWriter(
        private val matchBuildRepository: MatchBuildRepository,
        private val convert: ConvertLocationInfo
) : ItemWriter<MatchBuild> {
    override fun write(items: MutableList<out MatchBuild>) {
        items.forEach {
            if (it.entLat != null && it.entLon != null) {
                convert.CRSConverter(it.entLat!!, it.entLon!!).let { location ->
                    it.ofEnterLocation(BigDecimal.valueOf(location.x), BigDecimal.valueOf(location.y))
                }
            }
            if (it.centerLat != null && it.centerLon != null) {
                convert.CRSConverter(it.centerLat!!, it.centerLon!!).let { location ->
                    it.ofCenterLocation(BigDecimal.valueOf(location.x), BigDecimal.valueOf(location.y))
                }
            }
            matchBuildRepository.save(it)
        }
    }
}