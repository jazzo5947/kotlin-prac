package com.kotlin.ktdemo.Util
import com.kotlin.ktdemo.Log
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class Unzip {

    companion object : Log {}

    /**
     * Windows 환경에서 동작 보장
     *
     * 압축(Zip)
     */
    fun zip(files: List<File>, targetPath: String){
        logger().info("call zip, files={}, targetPath={}", files, targetPath)
        ZipOutputStream(BufferedOutputStream(FileOutputStream(targetPath))).use { output ->
            for(file in files) {
                executeZip(file, output)
            }
        }
    }

    private fun executeZip(file: File, zipOut: ZipOutputStream, parentPath: String = ""){
        logger().info("file: {}, parentPath: {}", file, parentPath)

        if(file.isDirectory){
            logger().info("is Directory!!!!!")
            val entryPath = parentPath + File.separator + file.name
            logger().info("entryPath: {}", entryPath)
            zipOut.putNextEntry(ZipEntry(
                if(file.name.endsWith("/")) entryPath
                else entryPath + File.separator
            ))
            zipOut.closeEntry()
            file.listFiles()?.let {
                logger().info("entryPath: {}", entryPath)
                it.toList().forEach { f -> executeZip(f, zipOut, entryPath)}
            }
        }else{
            logger().info("is Not Directory!!!!!")
            val entry = ZipEntry(
                if(!parentPath.equals("")) parentPath + File.separator + file.name
                else file.name
            )
            logger().info("entry: {}", entry)
            zipOut.putNextEntry(entry)
            FileInputStream(file).use { fileInputStream ->
                BufferedInputStream(fileInputStream).use { bufferedInputStream ->
                    bufferedInputStream.copyTo(zipOut)
                }
            }
        }
    }
    /**
     * Windows 동작 보장
     *
     * 압축 해제(zip)
     */
    fun unZip(zipFilePath: String, targetPath: String) {
        logger().info("call unZip")
        ZipFile(zipFilePath).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                logger().info(entry.name)
                if (entry.isDirectory) {
                    logger().info("make dir name: "+entry.name)
                    File(targetPath, entry.name).mkdirs()
                } else {
                    zip.getInputStream(entry).use { input ->
                        logger().info("not make dir")
                        if(entry.name.contains('/')){ //디렉토리째 압축할 경우 해제할 때 해당 경로를 못 찾아감
                            logger().info("make dir even isNotDirectory")
                            File(targetPath, entry.name.substring(0, entry.name.indexOf('/')+1)).mkdirs()
                        }
                        File(targetPath, entry.name).outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }
    }

}