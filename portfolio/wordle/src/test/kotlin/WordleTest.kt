
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

class WordleTest : StringSpec({

    // ----- is_valid -----
    "is_valid returns true for exactly 5 alphabetic letters" {
        is_valid("HELLO") shouldBe true
        is_valid("world") shouldBe true
    }

    "is_valid returns false for non-letters or wrong length" {
        is_valid("abc") shouldBe false            // too short
        is_valid("longer") shouldBe false         // too long
        is_valid("he11o") shouldBe false          // digits not allowed
        is_valid("w.rld") shouldBe false          // punctuation not allowed
        is_valid("") shouldBe false               // empty
    }

    // ----- read_word_list -----
    "read_word_list reads all words from a file" {
        val tmp = kotlin.io.path.createTempFile(prefix = "words", suffix = ".txt").toFile()
        tmp.writeText(listOf("apple", "BANANA", "Crane").joinToString("\n"))
        val words = read_word_list(tmp.absolutePath)
        words shouldBe mutableListOf("apple", "BANANA", "Crane")
        tmp.delete()
    }

    "read_word_list throws if file does not exist (as implemented)" {
        shouldThrow<java.io.FileNotFoundException> {
            read_word_list("src/test/resources/non_existent_file.txt")
        }
    }

    // ----- pickRandomWord -----
    "pickRandomWord returns and removes a word from list" {
        val words = mutableListOf("apple", "banana", "crane")
        val beforeSize = words.size
        val picked = pickRandomWord(words)
        words shouldHaveSize (beforeSize - 1)
        // picked should be one of the originals
        listOf("apple", "banana", "crane") shouldContain picked
        // and should no longer be in the remaining list
        words shouldNotContain picked
    }

    "pickRandomWord works for single element list" {
        val words = mutableListOf("alone")
        val picked = pickRandomWord(words)
        picked shouldBe "alone"
        words shouldHaveSize 0
    }

    // ----- evaluateGuess -----
    "evaluateGuess perfect match gives five 1s" {
        val secret = "RULER"
        val guess = "RULER"
        evaluateGuess(guess, secret) shouldBe listOf(1,1,1,1,1)
    }

    "evaluateGuess mismatch yields 0/1 per position only" {
        val secret = "CRANE"
        val guess  = "BLOCK"
        // Only position 3 (index 3) is 'N' vs 'C' etc.; here none match except maybe at index 3 'C' vs 'N' no
        // Let's craft a clearer case:
        val s = "ABCDE"
        val g = "AXYZE"
        // positions: A==A ->1, X!=B ->0, Y!=C ->0, Z!=D ->0, E==E ->1
        evaluateGuess(g, s) shouldBe listOf(1,0,0,0,1)
    }

    "evaluateGuess throws for wrong-length inputs" {
        shouldThrow<IllegalArgumentException> {
            evaluateGuess("ABCD", "ABCDE")
        }
        shouldThrow<IllegalArgumentException> {
            evaluateGuess("ABCDEF", "ABCDE")
        }
    }

    // ----- is_correct_guess -----
    "is_correct_guess returns true only when all positions are 1" {
        is_correct_guess(listOf(1,1,1,1,1)) shouldBe true
        is_correct_guess(listOf(1,1,1,1,0)) shouldBe false
    }
})
