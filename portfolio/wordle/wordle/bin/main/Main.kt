import org.fusesource.jansi.AnsiConsole

private const val MAX_ATTEMPTS = 1
private const val WORDS_FILE = "data/words.txt"

fun main() {
    AnsiConsole.systemInstall()
    val words = Wordle.readWordList(WORDS_FILE)
    if (words.isEmpty()) {
        println("Error: Word list is empty or could not be read.")
        return
    }

    val target = Wordle.pickRandomWord(words)
    var success = false

    for (attempt in 1..MAX_ATTEMPTS) {
        val guess = Wordle.obtainGuess(attempt)
        val matches = Wordle.evaluateGuess(guess, target)
        Wordle.displayGuess(guess, matches)

        if (matches.all { it == 2 }) {
            println("Congratulations! You've guessed the word!")
            success = true
            break
        }
    }

    if (!success) {
        println("Sorry, you've run out of guesses. The word was '$target'.")
    }
    AnsiConsole.systemUninstall()
}
