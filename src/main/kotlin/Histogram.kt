import java.io.File
import kotlin.collections.component1
import kotlin.collections.component2

fun getUniqueWordsCount(all_words: List<String>): MutableMap<String, Int> {
    val result = mutableMapOf<String, Int>()
    for (word in all_words) {
        val count = result[word] ?: 0
        result[word] = count + 1
    }
    return result
}

fun getUniqueCharsCount(all_chars: List<String>): MutableMap<Char, Int> {
    val result = mutableMapOf<Char, Int>()
    for (charStr in all_chars) {
        if (charStr.isNotEmpty()) {
            val c = charStr[0]
            val count = result[c] ?: 0
            result[c] = count + 1
        }
    }
    return result
}

fun SortByValue(lines: MutableMap<Char, Int>, how: Boolean): MutableMap<Int, Char> {
    val result = mutableMapOf<Int, Char>()

    val sortedEntries = if (how) {
        lines.entries.sortedBy { it.value }
    } else {
        lines.entries.sortedByDescending { it.value }
    }

    for (entry in sortedEntries) {
        result[entry.value] = entry.key
    }
    return result
}

fun main(args: Array<String>) {
    val text = File("src/Fisier.txt").readText()

    val delimiters = arrayOf(" ", "\n", "\t", ",", ".", "!", "?", ";", ":")
    val words = text.split(*delimiters).filter { it.isNotBlank() }

    val trim_words = mutableListOf<String>()
    words.forEach { word ->
        trim_words.add(word.trim().lowercase())
    }

    val chars = mutableListOf<String>()
    trim_words.forEach { word ->
        for (c in word) {
            if (c in 'a'..'z' || c in 'A'..'Z') {
                chars.add(c.uppercase())
            }
        }
    }

    val wordCounts = getUniqueWordsCount(trim_words)
    wordCounts.forEach { (word, count) -> println("$word : $count") }

    val charCounts = getUniqueCharsCount(chars)
    charCounts.forEach { (charStr, count) -> println("$charStr : $count") }

    val sortedCharsAsc = SortByValue(charCounts, true)
    sortedCharsAsc.forEach { (freq, char) -> println("Frecventa $freq -> Caracterul $char") }

    val sortedCharsDesc = SortByValue(charCounts, false)
    sortedCharsDesc.forEach { (freq, char) -> println("Frecventa $freq -> Caracterul $char") }
}