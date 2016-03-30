package cl.acid.rest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.acid.rest.model.User;


@Controller
public class ClientController {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Value("${service.url}")
	private String serviceUrl;
	
	@RequestMapping(method = RequestMethod.GET, value = "/rest_client")
	public String provideUploadInfo(Model model) {		
		return "userForm";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/rest_client")
	public String sendUser(@RequestParam("name") String name,
								   @RequestParam("file") MultipartFile file,
								   RedirectAttributes redirectAttributes) {
		
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);
		User user;
		try {
			user = new User(name, file.getBytes());
			HttpEntity<User> entity = new HttpEntity<User>(user, headers);
			ResponseEntity<?> result = restTemplate.exchange(serviceUrl, HttpMethod.POST, entity, User.class);
			if (result.getStatusCode().value() == HttpStatus.CREATED.value()){
				redirectAttributes.addFlashAttribute("message", result.getStatusCode().value() + ": OK");
				redirectAttributes.addFlashAttribute("location", result.getHeaders().getLocation().toString());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			redirectAttributes.addFlashAttribute("message",
					"Error al leer archivo");
		} catch (HttpClientErrorException e){
			redirectAttributes.addFlashAttribute("message", e.getStatusCode().value() + ": " + e.getStatusText());
		}
				
		return "redirect:rest_client";		
	}
}
