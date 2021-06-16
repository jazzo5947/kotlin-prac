package com.kotlin.ktdemo.Util

import com.kotlin.ktdemo.Log
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
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
            //폴더면 파일경로를 만들어준다. 이 때 최상위 폴더여도 슬래쉬가 붙게 된다
            val entry = ZipEntry(entryPath
            //    if(file.name.endsWith("/")) entryPath
                // 이걸로 끝날 때가 있나? 왜 이걸로 구분해야 하지?
            //    else entryPath + File.separator
                // 세퍼레이터를 붙이는 이유 -> 디렉토리면 디렉토리 내부로 들어갈거니까?
            )
            zipOut.putNextEntry(entry)
            zipOut.closeEntry()
            file.listFiles()?.let {
                logger().info("entryPath: {}", entryPath)
                it.toList().forEach { f -> executeZip(f, zipOut, entryPath) }
            }
        }else{
            logger().info("is Not Directory!!!!!")
            val entry = ZipEntry(
                if(!parentPath.equals("")) parentPath.substring(1) + File.separator + file.name
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

    fun makeDir(entryName: String, targetPath: String): Map<String, String>{
        var next = entryName
        var nextTargetPath = targetPath
        while(next.contains('/')){
            var entryCut = next.substring(0, next.indexOf('/'))
            var nextEntry = next.substring(next.indexOf('/')+1)
            logger().info("entryCut={}, nextEntry={}", entryCut, nextEntry)
            logger().info("nextTargetPath={}", nextTargetPath)
            File(nextTargetPath, entryCut).mkdir()
            next = nextEntry
            nextTargetPath += "\\$entryCut"
        }
        var map = HashMap<String, String>()
        map.put("fileName", next)
        map.put("targetPath", nextTargetPath)
        return map
    }

    fun makeFile(zip: ZipFile, entry: ZipEntry, targetPath: String){
        zip.getInputStream(entry).use { input ->
            File(targetPath, entry.name).outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    fun unZip2(zipFilePath: String, targetPath: String) {
        val buffer = ByteArray(1024)
        try {
            // create output directory is not exists
            val folder = File(targetPath)
            if (!folder.exists()) {
                folder.mkdir()
            }
            // get the zip file content
            val zis = ZipInputStream(FileInputStream(zipFilePath))
            // get the zipped file list entry
            var ze = zis.nextEntry
            while (ze != null) {
                logger().info("ze.name={}", ze.name)
                val fileName = ze.name
                val newFile = File(targetPath + File.separator.toString() + fileName)
                // create all non exists folders
                // else you will hit FileNotFoundException for compressed folder
                File(newFile.parent).mkdirs()
                val fos = FileOutputStream(newFile)
                var len: Int
                while (zis.read(buffer).also { len = it } > 0) {
                    fos.write(buffer, 0, len)
                }
                fos.close()
                ze = zis.nextEntry
            }
            zis.closeEntry()
            zis.close()
        } catch (ex: IOException) {
            logger().error("error!")
        }
    }

    //포스팅에 있던 함수
    fun unZip3(zipFilePath: String, targetPath: String) {
        ZipFile(zipFilePath).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                logger().info("entry name: {}", entry.name)
                if (entry.isDirectory) {
                    File(targetPath, entry.name).mkdirs()
                } else {
                    zip.getInputStream(entry).use { input ->
                        File(targetPath, entry.name).outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }
    }

    fun unZip4(zipFilePath: String, targetPath: String) {
        // 집파일 위치, 압축해제 위치를 받는다
        ZipFile(zipFilePath).use { zip ->
            zip.entries().asSequence().forEach { entry -> //엔트리 각각에 대하여
                logger().info("entry: {}, entry.name: {}", entry, entry.name)
                val fileName = entry.name
                val newFile = File(targetPath + File.separator + fileName)
                File(newFile.parent).mkdirs()
                makeFile(zip, entry, targetPath)
            }
        }
    }


}