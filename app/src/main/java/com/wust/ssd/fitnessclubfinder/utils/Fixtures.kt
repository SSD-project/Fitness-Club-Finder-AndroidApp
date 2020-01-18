package com.wust.ssd.fitnessclubfinder.utils

import com.wust.ssd.fitnessclubfinder.common.model.*

class Fixtures {
    fun prepareAPIResult(): NearbySearchAPIResult =
        NearbySearchAPIResult(
            emptyList(),
            listOf(
                Club(
                    Geometry(
                        Location(51.1042226, 17.0514579),
                        Viewport(
                            Location(51.1055200802915, 17.0527073302915),
                            Location(51.1028221197085, 17.05000936970849)
                        )
                    ),
                    icon = "https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png",
                    id = "f3de83cfefaed04315aecd946fb5922e930e97de",
                    name = "Marta Tymoszewicz - Atelier",
                    opening_hours = OpeningHours(false),
                    photos = arrayListOf(
                        Photo(
                            height = 1536,
                            html_attributions = arrayListOf("\u003ca href=\"https://maps.google.com/maps/contrib/113048811990249203119\"\u003eMarta Tymoszewicz - Atelier\u003c/a\u003e"),
                            photo_reference = "CmRaAAAAdko2rn-wASGpFwN5wC0cBe-xsnWPGzY_vcY_zrxVcO2EBSj4D__aMxn6ERqWj6Og6SxDzhQJjn01AgFuMnKWdi-L7VKv6oX8qYpXirGNkuNkGtfu1rqSQGk--4hxtiXFEhDesRwCI_fSGkOIW2cvgRQfGhQujrMtOAw7qq7d6yKg1h0a6ouIhA",
                            width = 2729
                        )
                    ),
                    place_id = "ChIJpS7t8NfpD0cRmVlYpGIIJ6g",
                    plus_code = PlusCode(
                        compound_code = "4332+MH Wrocław, Poland",
                        global_code = "9F3V4332+MH"
                    ),
                    rating = 5.0,
                    reference = "ChIJpS7t8NfpD0cRmVlYpGIIJ6g",
                    scope = "GOOGLE",
                    types = arrayListOf("gym", "health", "point_of_interest", "establishment"),
                    user_ratings_total = 2,
                    vicinity = "Walońska 9/153, Wrocław"
                ),
                Club(
                    geometry = Geometry(
                        Location(51.0997729, 17.0541068),
                        Viewport(
                            Location(51.1011389302915, 17.0554974302915),
                            Location(51.09844096970851, 17.0527994697085)
                        )
                    ),
                    icon = "https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png",
                    id = "e50712bfd0d6a9d2059c97761af0d18a4811b50e",
                    name = "Fenix. Gołębiowski M.",
                    opening_hours = OpeningHours(false),
                    photos = arrayListOf(
                        Photo(
                            height = 1536,
                            html_attributions = arrayListOf("\u003ca href=\"https://maps.google.com/maps/contrib/113048811990249203119\"\u003eMarta Tymoszewicz - Atelier\u003c/a\u003e"),
                            photo_reference = "CmRaAAAAdko2rn-wASGpFwN5wC0cBe-xsnWPGzY_vcY_zrxVcO2EBSj4D__aMxn6ERqWj6Og6SxDzhQJjn01AgFuMnKWdi-L7VKv6oX8qYpXirGNkuNkGtfu1rqSQGk--4hxtiXFEhDesRwCI_fSGkOIW2cvgRQfGhQujrMtOAw7qq7d6yKg1h0a6ouIhA",
                            width = 2729
                        )
                    ),
                    place_id = "ChIJhxtGl4fCD0cRlKB3PrWAc70",
                    plus_code = PlusCode(
                        compound_code = "33X3+WJ Wrocław, Poland",
                        global_code = "9F3V33X3+WJ"
                    ),
                    rating = 4.8,
                    reference = "ChIJhxtGl4fCD0cRlKB3PrWAc70",
                    scope = "GOOGLE",
                    types = arrayListOf("gym", "health", "point_of_interest", "establishment"),
                    user_ratings_total = 4,
                    vicinity = "Generała Romualda Traugutta 119, Wrocław"
                )
            )
        )
}

