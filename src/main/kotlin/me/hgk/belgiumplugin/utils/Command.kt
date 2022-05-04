package me.hgk.belgiumplugin.utils

fun <T: Iterable<String>> matchArgument(partialString: String, options: T) : List<String> {
    val r = mutableListOf<String>()
    for (opt in options) {
        if (opt.lowercase().startsWith(partialString)) {
            r.add(opt)
        }
    }
    return r
}