package it.unipd.dei.music_application.network

class ResponsePeriodDetails(balance: Double) {

    class Movement(amountSum: Int, type: String)

    class Details(income: Movement, outcome: Movement);
}