import kotlin.math.absoluteValue

fun main() {

    data class Directory(val name: String, val subdirectories: MutableList<String>, var size: Long)

    fun addChildrenSizeRec(directories: List<Directory>, directory: Directory): Long {
        val size = directory.size
        if (directory.subdirectories.isEmpty()) {
            return size
        }
        val sizeWithSubdirs = directory.subdirectories
            .map { subdir -> directories.filter { dir -> dir.name == subdir }[0] }
            .sumOf { addChildrenSizeRec(directories, it) } + size
        directory.size = sizeWithSubdirs
        return sizeWithSubdirs
    }

    fun addChildrenSize(directories: List<Directory>): Long {
        val root = directories[0];
        return addChildrenSizeRec(directories, root);
    }

    fun buildFileTree(input: List<String>): MutableList<Directory> {
        val path = ArrayDeque<String>()
        val directories = mutableListOf<Directory>()
        var current: Directory? = null
        input.forEach {
            when {
                it == "\$ cd .." -> {
                    path.removeLast()
                }
                it.startsWith("\$ cd ") -> {
                    path.add(it.substringAfter("\$ cd "))
                }
                it == "\$ ls" -> {
                    val temp = Directory(path.joinToString(separator = "/"), mutableListOf(), 0)
                    directories.add(temp)
                    current = temp
                }
                it.startsWith("dir ") -> {
                    val subdirectory = it.substringAfter("dir ")
                    current?.subdirectories?.add(path.joinToString(separator = "/") + "/" + subdirectory)
                }
                it.split(" ")[0].toLongOrNull() != null -> {
                    current?.size = current?.size!! + (it.split(" ")[0].toLongOrNull() ?: 0)
                }
            }
        }

        addChildrenSize(directories)
        return directories
    }

    fun part1(input: List<String>): Long {
        val directories = buildFileTree(input)
        return directories.filter { it.size <= 100000 }.sumOf { it.size }
    }

    fun part2(input: List<String>): Long {
        val totalDiskSize = 70000000
        val requiredUpdateSpace = 30000000

        val directories = buildFileTree(input)
        val occupiedSpace = directories[0].size
        val spaceToFreeUp = (totalDiskSize - occupiedSpace - requiredUpdateSpace).absoluteValue
        val dirToRemove = directories.filter { it.size >= spaceToFreeUp }.minByOrNull { it.size }!!
        return dirToRemove.size
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day07_test")
//    val testResult = part2(testInput)
//    println(testResult)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
