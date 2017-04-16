package repository

import java.sql.{Connection, ResultSet, Statement}
import java.util.logging.Logger

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class Repository(connection: Connection, statement: Statement) {

  val logger: Logger = Logger.getLogger(classOf[Repository].getName)

  def this(c: Connection) {
    this(c, c.createStatement())
  }

  def getTableRowCount: Int = {
    val sql = "SELECT COUNT(*) FROM test"
    val resultSet: ResultSet = statement.executeQuery(sql)
    resultSet.next()
    val result = resultSet.getInt(1)
    logger.info(s"Found $result rows in the table 'TEST'.")
    result
  }

  def clearTable(): Unit = {
    val sql = "DELETE FROM test"
    statement.execute(sql)
    val result = statement.getUpdateCount
    logger.info(s"Deleted $result rows from table 'TEST'.")
  }

  def insertRowsIntoTable(numberOfRecords: Int): Unit = {
    val sql = "INSERT INTO test(field) VALUES(?)"
    connection.setAutoCommit(false)
    val preparedStatement = connection.prepareStatement(sql)
    for (i <- 1 to numberOfRecords) {
      preparedStatement.setInt(1, i)
      preparedStatement.addBatch()
    }
    val batch = preparedStatement.executeBatch()
    connection.commit()
    connection.setAutoCommit(true)
    preparedStatement.close()
    logger.info(s"Inserted ${batch.length} rows into table 'TEST'.")
  }

  def getAllRowsFromTable: mutable.Buffer[Int] = {
    val sql = "SELECT * FROM test"
    val result = new ArrayBuffer[Int]()
    val resultSet = statement.executeQuery(sql)
    while (resultSet.next()) {
      result += resultSet.getInt(1)
    }
    logger.info(s"Selected ${result.size} rows from table 'TEST'.")
    result
  }

  def closeConnection(): Unit = {
    statement match {
      case null => logger.info("Statement already closed.")
      case _ => statement.close()
    }
    connection match {
      case null => logger.info("Connection already closed.")
      case _ => connection.close()
    }
  }
}
