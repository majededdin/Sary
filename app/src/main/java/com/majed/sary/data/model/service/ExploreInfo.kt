package com.majed.sary.data.model.service

data class ExploreInfo(
    val banners: ArrayList<Banner>,
    val catalogs: ArrayList<Catalog>
) {
    override fun toString(): String {
        return "Explore(banners=$banners, catalogs=$catalogs)"
    }
}