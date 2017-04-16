package repository

import java.util.logging.Logger

import scalikejdbc.{DB, _}

class Repository {

  val logger: Logger = Logger.getLogger(classOf[Repository].getName)

  def getTableRowCount: Option[Int] = DB.readOnly { implicit session =>
    val result = sql"SELECT COUNT(*) FROM test".map(_.int(1)).single().apply()
    logger.info(s"Found $result rows in the table 'TEST'.")
    result
  }

  def clearTable(): Unit = DB.localTx { implicit session =>
    val result = sql"DELETE FROM test".update().apply()
    logger.info(s"Deleted $result rows from table 'TEST'.")
  }

  def insertRowsIntoTable(numberOfRecords: Int): Unit = DB.localTx { implicit session =>
    val seq: Seq[Seq[Any]] = (1 to numberOfRecords).map { i => Seq(i) }
    sql"INSERT INTO test(field) VALUES(?)".batch(seq: _*).apply()
    logger.info(s"Inserted $numberOfRecords rows into table 'TEST'.")
  }

  def getAllRowsFromTable: List[Int] = DB.readOnly { implicit session =>
    val result = sql"SELECT * FROM test".map(_.int(1)).list().apply()
    logger.info(s"Selected ${result.size} rows from table 'TEST'.")
    result
  }
}
