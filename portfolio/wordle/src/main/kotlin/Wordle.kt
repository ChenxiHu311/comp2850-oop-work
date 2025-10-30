import java.io.File             // Allows reading and writing files from the local file system
import java.nio.charset.Charset // Used to specify character encoding (e.g., UTF-8) when reading text files
import kotlin.random.Random     // Provides random number generation for selecting random words

// 1) isValid: check whether a word is a valid Wordle guess.
// A valid word must be exactly 5 characters long and all characters must be letters.
fun isValid(word: String): Boolean =
    word.length == 5 && word.all { it.isLetter() }

// 2) readWordList: read target words from a file and return them as a mutable list.
// Each line is trimmed, validated via isValid, then lowercased for consistency.
fun readWordList(filename: String): MutableList<String> {
    val file = File(filename)
    require(file.exists()) { "Word list not found: $filename" }
    return file.readLines(Charset.forName("UTF-8"))
        .map { it.trim() }                // remove leading/trailing whitespace
        .filter { isValid(it) }           // only keep 5-letter alphabetic words
        .map { it.lowercase() }           // normalize to lowercase
        .toMutableList()
}

// 3) pickRandomWord: choose one word at random from the list,
// remove it from the list (so it won’t be chosen again), and return it.
fun pickRandomWord(words: MutableList<String>): String {
    require(words.isNotEmpty()) { "Word list is empty" }
    val idx = Random.nextInt(words.size)  // random index in [0, size)
    return words.removeAt(idx)            // also mutates the list by removing the chosen word
}

// 4) obtainGuess: prompt the user (showing the current attempt number),
// read a line from stdin, trim it, and keep asking until a valid 5-letter word is entered.
fun obtainGuess(attempt: Int): String {
    while (true) {
        print("Attempt $attempt: ")
        val guess = readLine()?.trim().orEmpty().lowercase()

        // Only accept guesses that pass the same validity rules we use elsewhere.
        if (isValid(guess)) return guess

        // If invalid, tell the user why and prompt again.
        println("Invalid guess. Please enter exactly 5 letters.")
    }
}

// 5) evaluateGuess (basic version used in this assignment):
// Compare 'guess' with 'target' position by position and return a list of 5 integers.
// For each index i: 1 means the letter matches exactly at position i; 0 means it does not.
// Note: this is the simple 0/1 scoring (no 'present but misplaced' handling).
fun evaluateGuess(guess: String, target: String): List<Int> {
    require(guess.length == 5 && target.length == 5) {
        "Guess/target must be 5 letters"
    }
    // Build a 5-element list where each item indicates whether that position matches.
    return guess.mapIndexed { i, c -> if (c == target[i]) 1 else 0 }
}

// 6) displayGuess: print the guess in a masked form based on the match results.
// For every position i: show the actual letter if matches[i] == 1; otherwise print '?'.
// This gives the user visual feedback while hiding incorrect positions.
fun displayGuess(guess: String, matches: List<Int>) {
    require(guess.length == 5 && matches.size == 5) {
        "Guess/matches must be length 5"
    }
    val line = buildString {
        for (i in 0 until 5) {
            append(if (matches[i] == 1) guess[i] else '?')
        }
    }
    println(line)
}
