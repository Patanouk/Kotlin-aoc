package day7

import readInput

object Aoc2023Day7 {

    private val cardOrderFirstStar = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').reversed()
    private val cardOrderSecondStar = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').reversed()

    fun solveFirstStar(): Int {
        return readInput("/day7/input.txt")
            .map { it.split(" ") }
            .map { Hand(it[0], it[1].toInt(), cardOrderFirstStar) }
            .sorted()
            .reversed()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
    }

    fun solveSecondStar(): Int {
        val temp = readInput("/day7/input.txt")
            .map { it.split(" ") }
            .map { Hand(it[0], it[1].toInt(), cardOrderFirstStar) }
            .sortedWith(compareBy<Hand> { mapToBestHand(it).handType }.thenComparator { h1, h2 ->
                handCardComparator(
                    cardOrderSecondStar
                ).compare(h1.hand.toCharArray(), h2.hand.toCharArray())
            })
            .reversed()

        return readInput("/day7/input.txt")
            .map { it.split(" ") }
            .map { Hand(it[0], it[1].toInt(), cardOrderFirstStar) }
            .sortedWith(compareBy<Hand> { mapToBestHand(it).handType }.thenComparator { h1, h2 ->
                handCardComparator(
                    cardOrderSecondStar
                ).compare(h1.hand.toCharArray(), h2.hand.toCharArray())
            })
            .reversed()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
    }

    private fun mapToBestHand(hand: Hand): Hand {
        if (hand.hand == "JJJJJ") {
            return Hand("AAAAA", hand.bid, cardOrderSecondStar)
        }

        val bestCharToReplace = hand.hand.toCharArray().toList()
            .filter { it != 'J' }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedWith(compareBy<Map.Entry<Char, Int>> { it.value }.reversed().thenComparator { e1, e2 ->
                handCardComparator(
                    cardOrderSecondStar
                ).compare(e1.key.toString().toCharArray(), e2.key.toString().toCharArray())
            })
            .first()
            .key

        return Hand(hand.hand.replace('J', bestCharToReplace), hand.bid, cardOrderSecondStar)
    }

    private class Hand(
        val hand: String,
        val bid: Int,
        private val cardOrder: List<Char>
    ) : Comparable<Hand> {
        val handType: HandType = getHandType(hand)

        override fun compareTo(other: Hand): Int {
            if (this.handType != other.handType) {
                return this.handType.compareTo(other.handType)
            }

            return handCardComparator(this.cardOrder)
                .compare(hand.toCharArray(), other.hand.toCharArray())
        }

        override fun toString() = hand
    }

    private fun getHandType(hand: String): HandType {
        val cardsGroupedByValue = hand.toCharArray().toList().groupingBy { it }.eachCount()
        return if (cardsGroupedByValue.any { it.value == 5 }) {
            HandType.Five_of_a_kind
        } else if (cardsGroupedByValue.any { it.value == 4 }) {
            HandType.Four_of_a_kind
        } else if (cardsGroupedByValue.any { it.value == 3 } && cardsGroupedByValue.any { it.value == 2 }) {
            HandType.Full_house
        } else if (cardsGroupedByValue.any { it.value == 3 }) {
            HandType.Three_of_a_kind
        } else if (cardsGroupedByValue.filter { it.value == 2 }.count() == 2) {
            HandType.Two_pair
        } else if (cardsGroupedByValue.filter { it.value == 2 }.count() == 1) {
            HandType.One_pair
        } else {
            HandType.High_card
        }
    }

    private fun handCardComparator(cardOrder: List<Char>) = Comparator<CharArray> { a, b ->
        a.zip(b)
            .filter { it.first != it.second }
            .map { cardOrder.indexOf(it.second).compareTo(cardOrder.indexOf(it.first)) }
            .first()
    }

    private enum class HandType {
        Five_of_a_kind,
        Four_of_a_kind,
        Full_house,
        Three_of_a_kind,
        Two_pair,
        One_pair,
        High_card,
    }
}