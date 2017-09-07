//      else null
//    }
  }

  constructor(): this(ContentValues())

  constructor(id: String, link: String): this() {
    this.id = id
    this.link = link
  }
//  constructor(aFile: File): this() {
//    path = aFile.absolutePath
//    // read then set the ifId
    operator fun get(story: Story): T = get(story.contentValues)
    operator fun set(story: Story, value: T) = set(story.contentValues, value)

    abstract operator fun get(conValues: ContentValues): T
    abstract operator fun set(conValues: ContentValues, value: T)

    abstract class Double: Column<kotlin.Double?>() {
      override operator fun get(conValues: ContentValues) = conValues.getAsDouble(name)
      override operator fun set(conValues: ContentValues, value: kotlin.Double?) = conValues.put(name, value)
    }

//    abstract class Float: Column<kotlin.Float?>() {
//      override operator fun get(conValues: ContentValues) = conValues.getAsFloat(name)
//      override operator fun set(conValues: ContentValues, value: kotlin.Float?) = conValues.put(name, value)
//    }

    abstract class Int: Column<kotlin.Int?>() {
      override operator fun get(conValues: ContentValues) = conValues.getAsInteger(name)
      override operator fun set(conValues: ContentValues, value: kotlin.Int?) = conValues.put(name, value)
    }

//    abstract class Long: Column<kotlin.Long?>() {
//      override operator fun get(conValues: ContentValues) = conValues.getAsLong(name)
//      override operator fun set(conValues: ContentValues, value: kotlin.Long?) = conValues.put(name, value)
//    }

    abstract class String: Column<kotlin.String?>() {
      override operator fun get(conValues: ContentValues) = conValues.get(name)?.toString()
      override operator fun set(conValues: ContentValues, value: kotlin.String?) = conValues.put(name, value?.trim())
    }
  }

  //
  // COLUMN DEFINITIONS
  //

  object Author: Column.String()
  object AverageRating: Column.Double()
  object CoverArtURL: Column.String()
  object Description: Column.String()
  object FirstPublished: Column.String()
  object Forgiveness: Column.String()
  object Genre: Column.String()
  object IFID: Column.String()
  object _ID: Column.String()
  object Language: Column.String()
  object Link: Column.String()
  object LookedUp: Column.String()
  object Path: Column.String()
  object Series: Column.String()
  object SeriesNumber: Column.Int()


  //
  // NON-PUBLICLY-EDITABLE PROPERTIES
  //

  var lookedUp
    get() = LookedUp[this]
    private set(value) {
      LookedUp[this] = value
    }

  init {
    lookedUp = Date().toString()
  }


  //
  // SIMPLE MAPPED PROPERTIES
  //

    }

  var averageRating
    get() = AverageRating[this]
    set(value) {
      AverageRating[this] = value
    }

  var coverArtURL
    get() = CoverArtURL[this]
    set(value) {
      CoverArtURL[this] = value
    }

  var description
    get() = Description[this]
    set(value) {
      Description[this] = value
  var genre
    get() = Genre[this]
    set(value) {
      Genre[this] = value
    }

  var id
    get() = _ID[this]
    set(value) {
      _ID[this] = value
    }

  var language
    get() = Language[this]
    set(value) {
      Language[this] = value
