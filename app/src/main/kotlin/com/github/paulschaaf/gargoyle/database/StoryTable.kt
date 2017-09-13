package com.github.paulschaaf.gargoyle.database

object StoryTable: SqlTable {
  override val name = "Story"

  val AverageRating = DoubleColumn("AverageRating")

  val Author = StringColumn("Author")
  val CoverArtURL = StringColumn("coverArtURL")
  val Description = StringColumn("description")
  val FirstPublished = StringColumn("FirstPublished")
  val Forgiveness = StringColumn("Forgiveness")
  val Genre = StringColumn("Genre")
  val IFID = StringColumn("IFID", "UNIQUE NOT NULL")
  val Language = StringColumn("Language")
  val Link = StringColumn("Link")
  val LookedUp = StringColumn("LookedUp")
  val Path = StringColumn("Path")
  val Series = StringColumn("Series")
  val TUID = StringColumn("TUID")
  val Title = StringColumn("Title")

  val _ID = IntColumn("_id", "PRIMARY KEY")
  val RatingCountAvg = IntColumn("RatingCountAvg")
  val RatingCountTotal = IntColumn("RatingCountTotal")
  val SeriesNumber = IntColumn("SeriesNumber")
  val StarRating = IntColumn("StarRating")

  override val columns: List<IColumn<*>> = listOf(
      _ID,
      Author,
      AverageRating,
      CoverArtURL,
      Description,
      FirstPublished,
      Forgiveness,
      Genre,
      IFID,
      Language,
      Link,
      LookedUp,
      Path,
      RatingCountAvg,
      RatingCountTotal,
      Series,
      SeriesNumber,
      TUID,
      Title
  )
}
