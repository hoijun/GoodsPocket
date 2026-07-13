package goods.pocket.app.data.local

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GoodsPocketSeedPolicyTest {

    @Test
    fun `seed is allowed only when every persisted area is empty`() {
        assertTrue(
            shouldSeedGoodsPocketDatabase(
                hasItems = false,
                hasPreorders = false,
                hasEvents = false,
                hasStorageLocations = false,
                hasPreferences = false,
            ),
        )

        assertFalse(
            shouldSeedGoodsPocketDatabase(
                hasItems = false,
                hasPreorders = true,
                hasEvents = false,
                hasStorageLocations = false,
                hasPreferences = false,
            ),
        )
        assertFalse(
            shouldSeedGoodsPocketDatabase(
                hasItems = true,
                hasPreorders = false,
                hasEvents = false,
                hasStorageLocations = false,
                hasPreferences = false,
            ),
        )
        assertFalse(
            shouldSeedGoodsPocketDatabase(
                hasItems = false,
                hasPreorders = false,
                hasEvents = false,
                hasStorageLocations = false,
                hasPreferences = true,
            ),
        )
    }
}
