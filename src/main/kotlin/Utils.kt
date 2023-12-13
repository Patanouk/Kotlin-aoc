import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

fun readInputRaw(name: String) = Path("src/main/kotlin/$name").readText()
fun readInput(name: String) = Path("src/main/kotlin/$name").readLines()
