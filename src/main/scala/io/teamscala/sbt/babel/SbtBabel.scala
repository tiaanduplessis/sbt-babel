package io.teamscala.sbt.babel

import com.typesafe.sbt.jse.SbtJsTask
import com.typesafe.sbt.web.Import.WebJs.JS
import com.typesafe.sbt.web._
import sbt.Keys._
import sbt._

object Import {

  object BabelKeys {
    val babel = TaskKey[Seq[File]]("babel", "Perform Babel transpilation.")
    val options = SettingKey[JS.Object]("babel-options", "Options for babel transpilation.")
  }

}

object SbtBabel extends AutoPlugin {

  override def requires = SbtJsTask

  override def trigger = AllRequirements

  val autoImport = Import

  import SbtJsTask.autoImport.JsTaskKeys._
  import SbtWeb.autoImport._
  import WebKeys._
  import autoImport.BabelKeys._

  val babelUnscopedSettings = Seq(
    includeFilter := "*.js" || "*.jsx",
    jsOptions := options.value.js
  )

  private val NodeModules = "node_modules"

  override def projectSettings = Seq(options := JS.Object.empty) ++ inTask(babel)(
    SbtJsTask.jsTaskSpecificUnscopedSettings ++
    inConfig(Assets)(babelUnscopedSettings) ++
    inConfig(TestAssets)(babelUnscopedSettings) ++
    Seq(
      moduleName := "babel",
      shellFile := getClass.getClassLoader.getResource("babel.js"),
      taskMessage in Assets := "Babel transpilation",
      taskMessage in TestAssets := "Babel test transpilation"
    )
  ) ++ SbtJsTask.addJsSourceFileTasks(babel) ++ Seq(
    babel in Assets := (babel in Assets).dependsOn(nodeModules in Assets).value,
    babel in TestAssets := (babel in TestAssets).dependsOn(nodeModules in TestAssets).value,
    nodeModuleDirectories in Plugin += baseDirectory.value / NodeModules
  )

}
