package goods.pocket.app.domain.service

interface IdGenerator {
    fun generate(prefix: String): String
}
