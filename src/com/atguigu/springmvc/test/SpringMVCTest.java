package com.atguigu.springmvc.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView; 
import com.atguigu.springmvc.crud.entities.Employee;

@Controller("/admin/")
public class SpringMVCTest {

	 
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@RequestMapping("/testSimpleMappingExceptionResolver")
	public String testSimpleMappingExceptionResolver(@RequestParam("i") int i){
		String [] vals = new String[10];
		System.out.println(vals[i]);
		return "success";
	}
	
	@RequestMapping(value="/testDefaultHandlerExceptionResolver",method=RequestMethod.POST)
	public String testDefaultHandlerExceptionResolver(){
		System.out.println("testDefaultHandlerExceptionResolver...");
		return "success";
	}
	
	@ResponseStatus(reason="����",value=HttpStatus.NOT_FOUND)
	@RequestMapping("/testResponseStatusExceptionResolver")
	public String testResponseStatusExceptionResolver(@RequestParam("i") int i){
		if(i == 13){
			throw new UserNameNotMatchPasswordException();
		}
		System.out.println("testResponseStatusExceptionResolver...");
		
		return "success";
	}
	
//	@ExceptionHandler({RuntimeException.class})
//	public ModelAndView handleArithmeticException2(Exception ex){
//		System.out.println("[���쳣��]: " + ex);
//		ModelAndView mv = new ModelAndView("error");
//		mv.addObject("exception", ex);
//		return mv;
//	}
	
	/**
	 * 1. �� @ExceptionHandler ����������п��Լ��� Exception ���͵Ĳ���, �ò�������Ӧ�������쳣����
	 * 2. @ExceptionHandler ����������в��ܴ��� Map. ��ϣ�����쳣��Ϣ����ҳ����, ��Ҫʹ�� ModelAndView ��Ϊ����ֵ
	 * 3. @ExceptionHandler ������ǵ��쳣�����ȼ�������. 
	 * 4. @ControllerAdvice: ����ڵ�ǰ Handler ���Ҳ��� @ExceptionHandler ������������ǰ�������ֵ��쳣, 
	 * ��ȥ @ControllerAdvice ��ǵ����в��� @ExceptionHandler ��ǵķ����������쳣. 
	 */
//	@ExceptionHandler({ArithmeticException.class})
//	public ModelAndView handleArithmeticException(Exception ex){
//		System.out.println("���쳣��: " + ex);
//		ModelAndView mv = new ModelAndView("error");
//		mv.addObject("exception", ex);
//		return mv;
//	}
	
	@RequestMapping("/testExceptionHandlerExceptionResolver")
	public String testExceptionHandlerExceptionResolver(@RequestParam("i") int i){
		System.out.println("result: " + (10 / i));
		return "success";
	}
	
	@RequestMapping("/testFileUpload")
	public String testFileUpload(@RequestParam("desc") String desc, 
			@RequestParam("file") MultipartFile file) throws IOException{
		System.out.println("desc: " + desc);
		System.out.println("OriginalFilename: " + file.getOriginalFilename());
		System.out.println("InputStream: " + file.getInputStream());
		return "success";
	}
	
	@RequestMapping("/i18n")
	public String testI18n(Locale locale){
		String val = messageSource.getMessage("i18n.user", null, locale);
		System.out.println(val); 
		return "i18n";
	}
	
	@RequestMapping("/testResponseEntity")
	public ResponseEntity<byte[]> testResponseEntity(HttpSession session) throws IOException{
		byte [] body = null;
		ServletContext servletContext = session.getServletContext();
		InputStream in = servletContext.getResourceAsStream("/files/abc.txt");
		body = new byte[in.available()];
		in.read(body);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=abc.txt");
		
		HttpStatus statusCode = HttpStatus.OK;
		
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(body, headers, statusCode);
		return response;
	}
	
	@ResponseBody
	@RequestMapping("/testHttpMessageConverter")
	public String testHttpMessageConverter(@RequestBody String body){
		System.out.println(body);
		return "helloworld! " + new Date();
	}
	
	@ResponseBody
	@RequestMapping(value="jsontest")
	public Map<String,Object> testJson(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("k1","k1");
		map.put("k2","k2");
		return map;
	}
	
	@RequestMapping("/testConversionServiceConverer")
	public String testConverter(@RequestParam("employee") Employee employee){
		System.out.println("save: " + employee);
		 
		return "redirect:/emps";
	}
	
}
