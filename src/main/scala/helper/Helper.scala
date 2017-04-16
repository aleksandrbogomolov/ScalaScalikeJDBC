package helper

import java.io.FileInputStream
import java.util.Properties
import java.util.logging.Logger
import javax.xml.transform.{OutputKeys, TransformerFactory}
import javax.xml.transform.stream.{StreamResult, StreamSource}

import scala.collection.mutable
import scala.xml._

object Helper {

  val logger: Logger = Logger.getLogger(Helper.getClass.getName)

  def readProperties: Properties = {
    val properties = new Properties()
    properties.load(new FileInputStream("src/main/resources/application.properties"))
    logger.info("Readed properties from source file.")
    properties
  }

  def createXml(rows: mutable.Buffer[Int], filePath: String): Unit = {
    val nodeBuffer: NodeBuffer = new NodeBuffer
    rows.foreach(r => {
      nodeBuffer += <entry><field>{r.toString}</field></entry>
    })
    val entries = <entries>{nodeBuffer}</entries>
    XML.save(filePath, entries, "UTF-8", xmlDecl = true, null)
    logger.info(s"Created new $filePath file, append ${rows.size} entry and saved to the disk.")
  }

  def transformXml(xsl: String, firstXmlPath: String, secondXmlPath: String): Unit = {
    val transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xsl))
    transformer.setOutputProperty(OutputKeys.INDENT, "yes")
    transformer.transform(new StreamSource(firstXmlPath), new StreamResult(secondXmlPath))
    logger.info(s"Transformed $firstXmlPath to the $secondXmlPath.")
  }

  def parseXml(filePath: String): Int = {
    val xml = XML.loadFile(filePath)
    val entries = xml \ "entry"
    val result = entries.map(e => (e \ "@field").text.toInt).sum
    result
  }
}
