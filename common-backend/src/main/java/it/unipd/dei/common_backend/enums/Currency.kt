package it.unipd.dei.common_backend.enums

enum class Currency(val value: Int) {
    EURO(0),
    DOLLAR(1),
    POUND(2);

    companion object {
        fun fromInt(value: Int): Currency? {
            return entries.find { it.value == value }
        }
    }
}
