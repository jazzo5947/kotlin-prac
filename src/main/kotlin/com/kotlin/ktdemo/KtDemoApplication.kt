package com.kotlin.ktdemo

import com.kotlin.ktdemo.Util.Unzip
import com.kotlin.ktdemo.Util.ZipExampleKt
import com.kotlin.ktdemo.Util.compJava
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.zeroturnaround.zip.ZipUtil
import java.io.File
import java.util.ArrayList

@SpringBootApplication
class KtDemoApplication

fun main(args: Array<String>) {
    runApplication<KtDemoApplication>(*args)

    println("let's get some info from open API")

    val zipfile = listOf(File("C:\\Users\\PARKSUNGHO\\Documents\\ziptest\\test.txt"),
                         File("C:\\Users\\PARKSUNGHO\\Documents\\ziptest\\test2.txt"))

    val zipfolder = listOf(File("C:\\zipzip\\testfolder"))

    //val zipfile = "C:\\Users\\planit\\ktDemo\\ziptest\\test.txt"
    val targetDir = "C:\\Users\\PARKSUNGHO\\Documents\\ziptest"
    val zipResName = "\\zipResult.zip"

    //ZipUtil.unpack(zipfile, targetDir)

    val zipUtil = Unzip()
    //zipUtil.unZip("C:\\Users\\PARKSUNGHO\\Documents\\ziptest\\testdirindir.zip", "C:\\Users\\PARKSUNGHO\\Documents\\ziptest")
    zipUtil.zip(zipfile, targetDir+zipResName)

    val zipExkt = ZipExampleKt()
    //zipExkt.compress(targetDir, "zipzipzip.zip")
}
