import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

@Suppress("unused")
class WordleTest : StringSpec({

    "isValid should return true for valid 5-letter words" {
        Wordle.isValid("hello") shouldBe true
    }

    "isValid should return false for words less than 5 letters" {
        Wordle.isValid("four") shouldBe false
    }

    "isValid should return false for words greater than 5 letters" {
        Wordle.isValid("longer") shouldBe false
    }

    "isValid should return true for 5-char words containing numbers" {
        Wordle.isValid("w0rld") shouldBe true
    }

    "isValid should return true for 5-char words containing punctuation" {
        Wordle.isValid("w.rld") shouldBe true
    }

    "isValid should return false for an empty string" {
        Wordle.isValid("") shouldBe false
    }

    "readWordList should read all words from the given file" {
        val words = Wordle.readWordList("src/test/resources/test_words.txt")
        words shouldBe listOf(
            "apple",
            "banana",
            "crane",
        )
    }

    "readWordList should return an empty list for a non-existent file" {
        val words = Wordle.readWordList("src/test/resources/non_existent_file.txt")
        words shouldBe emptyList()
    }

    "pickRandomWord should return a word from a non-empty list" {
        val words = mutableListOf("apple", "banana", "crane")
        val word = Wordle.pickRandomWord(words)
        word shouldNotBe null
        withClue("The picked word '$word' should be one of the original words") {
            word!! shouldBeIn listOf(
                "apple",
                "banana",
                "crane",
            )
        }
    }

    "pickRandomWord should return the word from a single-element list" {
        val words = mutableListOf("single")
        Wordle.pickRandomWord(words) shouldBe "single"
    }

    "evaluateGuess - perfect match" {
        val secret = "RULER"
        val guess = "RULER"
        val expected = listOf(2, 2, 2, 2, 2)
        Wordle.evaluateGuess(guess, secret).toList() shouldBe expected
    }

    "evaluateGuess - case-insensitive perfect match" {
        val secret = "RULER"
        val guess = "ruler"
        val expected = listOf(2, 2, 2, 2, 2)
        Wordle.evaluateGuess(guess, secret).toList() shouldBe expected
    }

    "evaluateGuess - complete mismatch" {
        val secret = "CRANE"
        val guess = "BLOCK"
        val expected = listOf(0, 0, 0, 1, 0)
        Wordle.evaluateGuess(guess, secret).toList() shouldBe expected
    }

    "evaluateGuess - partial match with correct and incorrect positions" {
        val secret = "SOLVE"
        val guess = "SMILE"
        val expected = listOf(2, 0, 0, 1, 2)
        Wordle.evaluateGuess(guess, secret).toList() shouldBe expected
    }

    "evaluateGuess - duplicate letters in guess, not in secret" {
        val secret = "CRANE"
        val guess = "APPLE"
        val expected = listOf(1, 0, 0, 0, 2)
        Wordle.evaluateGuess(guess, secret).toList() shouldBe expected
    }

    "evaluateGuess - duplicate letters in secret" {
        val secret = "SPEED"
        val guess = "EERIE"
        val expected = listOf(1, 1, 0, 0, 0)
        Wordle.evaluateGuess(guess, secret).toList() shouldBe expected
    }

    "evaluateGuess - duplicate letters in guess and secret" {
        val secret = "BROOK"
        val guess = "BOOKS"
        val expected = listOf(2, 1, 2, 1, 0)
        Wordle.evaluateGuess(guess, secret).toList() shouldBe expected
    }

    "evaluateGuess - another duplicate letters case from old tests" {
        val secret = "abase"
        val guess = "erase"
        val expected = listOf(0, 0, 2, 2, 2)
        Wordle.evaluateGuess(guess, secret).toList() shouldBe expected
    }
})
