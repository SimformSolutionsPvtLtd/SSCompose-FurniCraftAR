object BuildConfigFields {

    // SketchFab
    val SKETCHFAB_API_KEY = Field(
        Type.STRING,
        "SKETCHFAB_API_KEY",
        "SKETCHFAB_API_KEY"
    )

    private object Type {
        const val STRING = "String"
    }

    data class Field(val type: String, val title: String, val value: String)
}
