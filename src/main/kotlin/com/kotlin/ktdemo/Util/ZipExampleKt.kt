package com.kotlin.ktdemo.Util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipExampleKt {
    /**
     * 압축 메소드
     * @param path 경로
     * @param outputFileName 출력파일명
     */
    @Throws(Exception::class)
    fun compress(path: String, outputFileName: String){
        val file = File(path)
        val pos = outputFileName.lastIndexOf('.')
        var outputFileName2 = outputFileName
        if(!outputFileName.substring(pos).contentEquals(".zip")){
            outputFileName2 += ".zip"
        }

        if(!file.exists()){
            throw Exception("Not File!")
        }

        val zos = ZipOutputStream(FileOutputStream(File(outputFileName2)))
        searchDirectory(file, zos)

    }

    /**
     * 디렉토리 탐색
     * @param file 현재 파일
     * @param root 루트 경로
     * @param zos 압축 스트림
     */
    @Throws(Exception::class)
    private fun searchDirectory(file: File, root: String, zos: ZipOutputStream){
        if(file.isDirectory){
            val files = file.listFiles()
            for(f in files){
                searchDirectory(f, root, zos)
            }
        }else{
            compressZip(file, root, zos)
        }
    }

    /**
     * 다형성
     */
    @Throws(Throwable::class)
    private fun searchDirectory(file: File, zos: ZipOutputStream) {
        this.searchDirectory(file, file.path, zos)
    }

    /**
     * 압축 메소드
     * @param file
     * @param root
     * @param zos
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun compressZip(file: File, root: String, zos: ZipOutputStream){
        val fis = FileInputStream(file)
        try {
           // val zipName = file.path.replace(root+"\\", "")
            zos.putNextEntry(ZipEntry(file.path.replace(root+"\\", "")))
            val length = file.length().toInt()
            val buffer = ByteArray(file.length().toInt())

            fis.read(buffer, 0, length)
            zos.write(buffer, 0, length)
            zos.closeEntry()
        }catch (e: Throwable){
            throw e
        }finally {
            if(fis != null) fis.close()
        }
    }
}