package com.linyang.energy.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/** 
* @Description 压缩文件工具类
* @author Jijialu
* @date 2018年5月23日 
*/
public class LoadZipUtil {
	
	private static int BUF_SIZE = 1024*10;
	
	/** 
     * 创建压缩文件 
     * @param sourcePath 要压缩的文件 
     * @param zipFilePath 文件存放路徑 
     * @param zipfileName 压缩文件名称 
     * @return File 
     * @throws IOException 
     */  
    public static File createZip(String sourcePath ,String zipFilePath,String zipfileName) throws IOException{  
         //打包文件名称  
         zipfileName = zipfileName+".zip";  
           
         /**在服务器端创建打包下载的临时文件夹*/  
         File zipFiletmp = new File(zipFilePath+"/tmp");  
         if(!zipFiletmp.exists() && !(zipFiletmp.isDirectory())){  
            zipFiletmp.mkdirs();  
         }  
           
         File fileName = new File(zipFiletmp,zipfileName);  
         //打包文件  
         createZip(sourcePath,fileName);  
        return fileName;  
    }  
      
     /** 
     * 创建ZIP文件 
     * @param sourcePath 文件或文件夹路径 
     * @param zipPath 生成的zip文件存在路径（包括文件名） 
     */  
    public static void createZip(String sourcePath, File zipFile) {  
        ZipOutputStream zos = null;  
        try {  
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile),BUF_SIZE));  
            writeZip(new File(sourcePath), "", zos);  
        } catch (FileNotFoundException e) {  
             throw new RuntimeException(e);   
        } finally {  
            try {  
                if (zos != null) {  
                    zos.close();  
                }  
            } catch (IOException e) {  
                 throw new RuntimeException(e);   
            }  
   
        }  
    }  
      
    /** 
     * 创建ZIP文件 
     * @param sourcePath 文件或文件夹路径 
     * @param zipPath 生成的zip文件存在路径（包括文件名） 
     */  
    public static void createZip(String sourcePath, String zipPath) {  
        ZipOutputStream zos = null;  
        try {  
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipPath),BUF_SIZE));  
            writeZip(new File(sourcePath), "", zos);  
        } catch (FileNotFoundException e) {  
             throw new RuntimeException(e);   
        } finally {  
            try {  
                if (zos != null) {  
                    zos.close();  
                }  
            } catch (IOException e) {  
                 throw new RuntimeException(e);   
            }  
   
        }  
    }  
    /** 
     *  
     * @param file 
     * @param parentPath 
     * @param zos 
     */  
    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {  
        if(file.exists()){  
            if(file.isDirectory()){//处理文件夹  
                parentPath+=file.getName()+File.separator;  
                File [] files=file.listFiles();  
                for(File f:files){  
                    writeZip(f, parentPath, zos);  
                }  
            }else{  
                DataInputStream dis=null;  
                try {  
                    dis=new DataInputStream(new BufferedInputStream(new FileInputStream(file)));  
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());  
                    zos.putNextEntry(ze);  
                    byte [] content=new byte[BUF_SIZE];  
                    int len;  
                    while((len=dis.read(content))!=-1){  
                        zos.write(content,0,len);  
                        zos.flush();  
                    }  
                       
                 zos.closeEntry();   
                } catch (FileNotFoundException e) {  
                     throw new RuntimeException(e);   
                } catch (IOException e) {  
                     throw new RuntimeException(e);   
                }finally{  
                    try {  
                        if(dis!=null){  
                            dis.close();  
                        }  
                    }catch(IOException e){  
                         throw new RuntimeException(e);   
                    }  
                }  
            }  
        }  
    }     
      
    /** 
     * 刪除文件 
     * @param file 
     * @return 
     * @throws Exception 
     */  
    public static boolean delFile(File file) throws Exception {  
        boolean result = false;  
        if(file.exists()&&file.isFile())   
        {  
            file.delete();  
            file.getParentFile().delete();  
            result = true;  
        }  
        return result;  
    }  
}
