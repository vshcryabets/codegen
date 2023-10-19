package enums
import enums.CryptoCurrency
data class Money(
    val sum: Int,
    val currency: CryptoCurrency,
    val name: String?,
    val attached: Boolean = false
)
