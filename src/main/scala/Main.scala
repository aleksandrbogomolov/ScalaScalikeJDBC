import java.sql.DriverManager
import java.util.Properties
import java.util.logging.Logger

import configuration.DataBaseConfiguration
import helper.Helper
import repository.Repository

object Main {

  val properties: Properties = Helper.readProperties

  def main(args: Array[String]): Unit = {
    val configuration = initDataBaseConfiguration()
    val repository = new Repository(configuration.connection)
    repository.getTableRowCount match {
      case x if x > 0 => repository.clearTable()
    }
    repository.insertRowsIntoTable(configuration.numberOfRecords)
    val rows = repository.getAllRowsFromTable
    repository.closeConnection()

    val firstXmlPath = properties.getProperty("firstxmlpath")
    val secondXmlPath = properties.getProperty("secondxmlpath")
    val stylesheetpath = properties.getProperty("stylesheetpath")

    Helper.createXml(rows, firstXmlPath)
    Helper.transformXml(stylesheetpath, firstXmlPath, secondXmlPath)
    Logger.getLogger(Main.getClass.getName).info(s"Result = ${Helper.parseXml(secondXmlPath)}.")
  }

  def initDataBaseConfiguration(): DataBaseConfiguration = {
    val cnfg = new DataBaseConfiguration()
    cnfg.dataBaseUrl = properties.getProperty("url")
    cnfg.dataBaseName = properties.getProperty("database")
    cnfg.userName = properties.getProperty("user")
    cnfg.password = properties.getProperty("password")
    cnfg.driver = properties.getProperty("driver")
    cnfg.numberOfRecords = properties.getProperty("recordsnumber").toInt

    Class.forName(properties.getProperty("driver"))
    cnfg.connection = DriverManager.getConnection(cnfg.dataBaseUrl + cnfg.dataBaseName, cnfg
      .userName, cnfg.password)
    cnfg
  }
}
