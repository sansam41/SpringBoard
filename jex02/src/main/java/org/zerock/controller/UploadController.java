package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {

		@GetMapping("/uploadForm")
		public void uploadForm() {
			log.info("upload form");
		}
		
		@PostMapping("/uploadFormAction")
		public void uploadFormPost(MultipartFile[] uploadFile,Model model) {
			
			String uploadFolder="C:\\upload";
			for(MultipartFile multipartFile : uploadFile) {
				log.info("--------------------------");
				log.info("Upload File Name: "+multipartFile.getOriginalFilename());
				log.info("Upload File Size: "+multipartFile.getSize());
				
				File saveFile=new File(uploadFolder, multipartFile.getOriginalFilename());
				
				try {
					multipartFile.transferTo(saveFile);
				}catch (Exception e) {
					// TODO: handle exception
					log.error(e.getMessage());
				}
			}
		}
		
		@GetMapping("/uploadAjax")
		public void uploadAjax() {
			log.info("upload ajax");
		}
		
		
		@PostMapping(value="/uploadAjaxAction",
				produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		@ResponseBody
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<List<AttachFileDTO>> 
		uploadAjaxPost(MultipartFile[] uploadFile) {
			List<AttachFileDTO> list=new ArrayList<AttachFileDTO>();
			
			log.info("update ajax post.........");
			
			String uploadFolder="C:\\upload";
			String uploadFolderPath=getFolder();
			
			File uploadPath=new File(uploadFolder,getFolder());
			log.info("upload path: "+uploadPath);
			
			if(uploadPath.exists()==false) {
				uploadPath.mkdirs();
			}
			
			for(MultipartFile multipartFile: uploadFile) {
				AttachFileDTO attachDTO=new AttachFileDTO();
				
				log.info("------------------------------");
				log.info("upload File Name"+multipartFile.getOriginalFilename());
				log.info("upload File Size"+multipartFile.getSize());
				
				String uploadFileName=multipartFile.getOriginalFilename();
				
				uploadFileName=uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
				attachDTO.setFileName(uploadFileName);
				
				log.info("only file name: " + uploadFileName);
				
				UUID uuid=UUID.randomUUID();
				
				uploadFileName=uuid.toString()+"_"+uploadFileName;
				
				

				
				try {
					File saveFile=new File(uploadPath,uploadFileName);
					multipartFile.transferTo(saveFile);
					
					attachDTO.setUuid(uuid.toString());
					attachDTO.setUploadPath(uploadFolderPath);
					
					if(checkImageType(saveFile)) {
						attachDTO.setImage(true);
						FileOutputStream thumbnail=new FileOutputStream(new File(uploadPath,"s_"+uploadFileName));
						Thumbnailator.createThumbnail(multipartFile.getInputStream(),thumbnail,100,100);
						thumbnail.close();
					}
					list.add(attachDTO);
				}catch (Exception e) {
					log.error(e.getMessage());
					// TODO: handle exception
				}
			}
			return new ResponseEntity<List<AttachFileDTO>>(list,HttpStatus.OK);
		}
		
		private String getFolder() {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date=new java.util.Date();
			String str=sdf.format(date);
			
			return str.replace("-", File.separator);
		}
		
		private boolean checkImageType(File file) {
			try {
				String contentType=Files.probeContentType(file.toPath());
				
				return contentType.startsWith("image");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return false;
		}
		
		@GetMapping("/display")
		@ResponseBody
		public ResponseEntity<byte[]> getFile(String fileName){
			log.info("fileName: "+fileName);
			File file=new File("c:\\upload\\"+fileName);
			
			log.info("file: "+file);
			ResponseEntity<byte[]> result =null;
			try {
				HttpHeaders header=new HttpHeaders();
				
				header.add("Content-Type", Files.probeContentType(file.toPath()));
				result=new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return result;
		}
		
		@GetMapping(value="/download",produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
		public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName){
			
			log.info("download file: "+fileName);
			Resource resource=new FileSystemResource("c:\\upload\\"+fileName);
			log.info("resource: "+resource);
			
			if(resource.exists()==false) {
				return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
			}
			
			String resourceName=resource.getFilename();
			String resourceOriginalName=resourceName.substring(resourceName.indexOf("_")+1);
			
			HttpHeaders headers=new HttpHeaders();
			
			try {	
				String downloadName=null;
				if(userAgent.contains("Trident")) {
					log.info("IE browser");
					downloadName=URLEncoder.encode(resourceOriginalName,"UTF-8");
					
					log.info("Edge name:"+downloadName);
				}else {
					log.info("Chrome browser");
					downloadName=new String(resourceOriginalName.getBytes("UTF-8"),"ISO-8859-1");
				}
				headers.add("Content-Disposition", "attachment; filename="+downloadName);
				
			}catch (UnsupportedEncodingException e) {
				// TODO: handle exception
				e.printStackTrace();
			
			}
			return new ResponseEntity<Resource>(resource,headers,HttpStatus.OK);
		}
		
		@PostMapping("/deleteFile")
		@ResponseBody
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<String> deleteFile(String fileName,String type){
			log.info("deleteFile: "+fileName);
			
			File file;
			
			try {
				file=new File("c:\\upload\\"+URLDecoder.decode(fileName,"UTF-8"));
				log.info(type);
				file.delete();
				if(type.equals("image")) {
					String largeFileName=file.getAbsolutePath().replace("s_", "");
					
					log.info("largeFileName: "+largeFileName);
					
					file = new File(largeFileName);
					
					file.delete();
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<String>("delete",HttpStatus.OK);
		}
}
