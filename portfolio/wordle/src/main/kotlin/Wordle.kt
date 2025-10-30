@Suppress("MagicNumber")
private const val M = 5
fun generate_random_word(): String {
    val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    val word = StringBuilder()

    // Generate a random 5-letter word
    for (i in 1..M) {
        val randomIndex = (0 until letters.length).random()
        word.append(letters[randomIndex])
    }

    return word.toString()
}

fun generate_wordles(num: Int, filename: String) {
    // generate `num` wordles, result will be written to `filename`
    // if `filename` exists, original content will be overwriteen
    println("generating wordle, need " + num + " words, saving to " + filename)

    val wordList = mutableListOf<String>()

    // Generate random 5-letter words
    for (i in 1..num) {
        val randomWord = generate_random_word()
        wordList.add(randomWord)
    }

//    println(wordList)

    java.io.File(filename).writeText(wordList.joinToString("\n"))
    println("Successfully generated $num words and saved to $filename")
    return
}

fun read_word_list(filename: String): MutableList<String> {
    // Reads Wordle target words from the specified file, returning them as a list of strings.
    return java.io.File(filename).readLines().toMutableList()
}

fun is_all_letter(word: String): Boolean {
    for (char in word) {
        // if (!char.isLetter()) {
        //     return false
        // }

        if (char.isLetter()) {
            continue
        } else {
            return false
        }

        // check if this char is a letter, if this is a letter, continue, else, return false
    }

    return true
}

fun is_valid(word: String): Boolean {
    // Returns true if the given word is valid in Wordle (i.e., if it consists of exactly 5 letters)
    if (word.length != M) {
        return false
    }

    return is_all_letter(word)
}

fun pickRandomWord(words: MutableList<String>): String {
    // Chooses a random word from the given list, removes that word from the list, then returns it.

    // generate a random number from [0, n-1] first
    var random_index = (0 until words.size).random()
    var chosen = words[random_index]
    words.removeAt(random_index)
    return chosen

}

fun obtainGuess(attempt: Int): String {
    // Prints a prompt using the given attempt number (e.g. "Attempt 1: "),
    // then reads a word from stdin. The word should be returned if valid,
    // otherwise the user should be prompted to try again.

    while (true) {
        print("Attempt $attempt: ")

        val input = readLine()

        val guess = input?.trim() ?: ""

        if (is_valid(guess)) {
            return guess
        } else {
            println("Please enter exactly 5 letters!")
        }
    }

}

fun evaluateGuess(guess: String, target: String): List<Int> {
    // Compares a guess with the target word. Returns a list containing 5 integers,
    // representing the result of comparison at each letter position.
    // 0 indicates no match, 1 indicates a match.

    if (guess.length != M || target.length != M) {
        throw IllegalArgumentException("guess and target should both contains exactly 5 letters")
    }

    val ans = mutableListOf<Int>()
    for (i in 0 until M) {
        if (guess[i] == target[i]) {
            ans.add(1)
        } else {
            ans.add(0)
        }
    }

    return ans
}

fun displayGuess(guess: String, matches: List<Int>) {
    // Displays the letters of a guess that match target word,
    // or a ‘?’ character where there is no match.
    var display = ""

    for (i in 0 until M) {
        if (matches[i] == 1) {
            display += guess[i]
        } else {
            display += "?"
        }
    }

    println(display)
}
