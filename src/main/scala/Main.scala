import java.util.logging.Logger

import com.typesafe.config.{Config, ConfigFactory}
import helper.Helper
import repository.Repository
import scalikejdbc.config.DBs

object Main extends App {

  override def main(args: Array[String]): Unit = {
    val logger = Logger.getLogger(Main.getClass.getName)
    DBs.setupAll()

    val repository = new Repository
    repository.getTableRowCount match {
      case x if x.get > 0 => repository.clearTable()
      case Some(0) => logger.info("Table 'TEST' already clear.")
    }

    val config: Config = ConfigFactory.load()
    repository.insertRowsIntoTable(config.getInt("recordsnumber"))
    val rows = repository.getAllRowsFromTable

    val firstXmlPath = config.getString("firstxmlpath")
    val secondXmlPath = config.getString("secondxmlpath")
    val stylesheetpath = config.getString("stylesheetpath")

    Helper.createXml(rows, firstXmlPath)
    Helper.transformXml(stylesheetpath, firstXmlPath, secondXmlPath)
    logger.info(s"Result = ${Helper.parseXml(secondXmlPath)}.")
  }
}
