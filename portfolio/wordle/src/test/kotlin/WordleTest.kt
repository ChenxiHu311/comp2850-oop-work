import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.File

class WordleTest : StringSpec({

    // 1) isValid
    "isValid returns true only for exactly 5 letters" {
        isValid("apple") shouldBe true
        isValid("Apple") shouldBe true
        isValid("appl3") shouldBe false
        isValid("apps") shouldBe false
        isValid("bananas") shouldBe false
        isValid("") shouldBe false
    }

    // 2) readWordList
    "readWordList reads only 5-letter alphabetic words and lowercases them" {
        val tmp = File.createTempFile("words", ".txt").apply {
            writeText(
                """
                Apple
                bread
                12345
                pear
                CArgo
                SPACE 
                zzzzz
                """.trimIndent()
            )
            deleteOnExit()
        }
        val list = readWordList(tmp.absolutePath)
        // Expect "space" to be included after trimming and validating
        list shouldBe mutableListOf("apple", "bread", "cargo", "space", "zzzzz")
    }

    // 3) pickRandomWord — must return an element and remove it from the list
    "pickRandomWord returns an element and removes it from the list" {
        val words = mutableListOf("apple", "bread", "cargo")
        val picked = pickRandomWord(words)
        (picked in listOf("apple", "bread", "cargo")) shouldBe true
        words.size shouldBe 2
        words.contains(picked) shouldBe false
    }

    // 4) evaluateGuess — 1 means exact match, 0 means not matched
    "evaluateGuess returns 1 for exact matches and 0 otherwise" {
        evaluateGuess("apple", "apple") shouldBe listOf(1, 1, 1, 1, 1)
        evaluateGuess("apply", "apple") shouldBe listOf(1, 1, 1, 1, 0)
        evaluateGuess("zzzzz", "apple") shouldBe listOf(0, 0, 0, 0, 0)
        evaluateGuess("poppy", "apple") shouldBe listOf(0, 0, 1, 0, 0) // only the 3rd letter matches
    }
})
