package configuration

import java.sql.Connection

class DataBaseConfiguration() {
  var dataBaseUrl: String = ""
  var dataBaseName: String = ""
  var userName: String = ""
  var password: String = ""
  var driver: String = ""
  var numberOfRecords: Int = 0
  var connection: Connection = _
}
