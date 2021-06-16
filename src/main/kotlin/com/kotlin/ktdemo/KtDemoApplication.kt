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
    println("let's get some info from open API")
    println("third commit test")

    val zipfile = listOf(File("C:\\Users\\PARKSUNGHO\\Documents\\ziptest\\zipzipTest"))
    //val zipfile = listOf(File("C:\\Users\\PARKSUNGHO\\Documents\\ziptest\\test.txt"), File("C:\\Users\\PARKSUNGHO\\Documents\\ziptest\\test2.txt"))

    val zipUtil = Unzip()
    zipUtil.unZip4("C:\\Users\\planit\\HyperEx\\LT_CRE_ADM_CD.zip", "C:\\Users\\planit\\HyperEx\\ziptest")

    val zipExkt = ZipExampleKt()
    //zipExkt.compress(targetDir, "zipzipzip.zip")
}
