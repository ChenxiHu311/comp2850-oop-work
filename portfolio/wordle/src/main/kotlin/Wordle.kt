import java.io.File
import java.io.IOException

object Wordle {
    const val WORD_LENGTH = 5

    fun isValid(word: String): Boolean = word.length == WORD_LENGTH

    fun readWordList(filename: String): MutableList<String> = try {
        File(filename).readLines().toMutableList()
    } catch (e: IOException) {
        System.err.println("Error reading file: ${e.message}")
        mutableListOf()
    }

    fun pickRandomWord(words: MutableList<String>): String {
        val word = words.random()
        words.remove(word)
        return word
    }

    fun obtainGuess(attempt: Int): String {
        while (true) {
            print("Attempt $attempt: ")
            val guess = readLine() ?: ""
            if (isValid(guess)) {
                return guess.lowercase()
            } else {
                println("无效输入，请输入一个5个字母的单词。")
            }
        }
    }

    fun evaluateGuess(guess: String, secret: String): IntArray {
        val upperGuess = guess.uppercase()
        val upperSecret = secret.uppercase()
        val result = IntArray(WORD_LENGTH) { 0 }
        val secretCharCounts = upperSecret.groupingBy { it }.eachCount().toMutableMap()

        // First pass: check for correct letters in correct positions (green matches)
        for (i in upperGuess.indices) {
            if (upperGuess[i] == upperSecret[i]) {
                result[i] = 2
                secretCharCounts[upperGuess[i]] = secretCharCounts.getOrDefault(upperGuess[i], 0) - 1
            }
        }

        // Second pass: check for correct letters in wrong positions (yellow matches)
        for (i in upperGuess.indices) {
            if (result[i] == 0) { // Only check letters that were not a perfect match
                if (secretCharCounts.getOrDefault(upperGuess[i], 0) > 0) {
                    result[i] = 1
                    secretCharCounts[upperGuess[i]] = secretCharCounts.getOrDefault(upperGuess[i], 0) - 1
                }
            }
        }

        return result
    }

    fun displayGuess(guess: String, matches: IntArray) {
        val sb = StringBuilder()
        for (i in matches.indices) {
            val letter = guess[i]
            when (matches[i]) {
                2 -> sb.append("\u001B[32m$letter\u001B[0m")
                1 -> sb.append("\u001B[33m$letter\u001B[0m")
                0 -> sb.append("\u001B[31m$letter\u001B[0m")
            }
        }
        println(sb.toString())
        println(matches.joinToString(" "))
    }
}
