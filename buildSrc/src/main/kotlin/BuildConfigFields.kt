object BuildConfigFields {
    // AppCenter
    val APPCENTER_SECRET = Field(
        Type.STRING,
        "APPCENTER_SECRET",
        "\"APPCENTER_KEY\""
    )

    private object Type {
        const val STRING = "String"
    }

    data class Field(val type: String, val title: String, val value: String)
}
