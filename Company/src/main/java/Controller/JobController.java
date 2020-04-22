package Controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import model.Company;

@Configuration
@ConfigurationProperties(prefix="api")
@RestController
public class JobController {
	private String clientId;
	private String clientSecret;
	private String redirectUrl;
	
	public String getClientId() { return this.clientId; }

	public void setClientId(final String clientId) { this.clientId = clientId; }

	public String getClientSecret() { return this.clientSecret; }
	
	public void setClientSecret(final String clientSecret) { this.clientSecret = clientSecret; }
	
	public String getRedirecUrl() {	return this.redirectUrl; }
	
	public void setRedirectUrl(final String redirectUrl) { this.redirectUrl = redirectUrl; }
	
	
	@GetMapping(value = "/authorization")
	public RedirectView authorization() {
		final var authorizationBuilder = new StringBuilder();
		authorizationBuilder.append("https://www.linkedin.com/oauth/v2/authorization?response_type=code")
							.append("&client_id=" + getClientId())
							.append("&redirect_uri=" + getRedirecUrl())
							.append("&state=fooobar&scope=r_liteprofile%20r_emailaddress%20w_member_social");
		
		return new RedirectView(authorizationBuilder.toString());
	}
	
	@GetMapping("/companies")
	public ResponseEntity<String> getCompaniesByLinkedin(@RequestParam("code") final String code) {
		final var accesToken = getAccesToken(code);
		final var restTemplate = new RestTemplate();
		final var headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " +accesToken);
		final var entity = new HttpEntity<String>("parameters", headers);
		final var profileInfoUrl = "https://api.linkedin.com/v2/me";
		final var response = restTemplate.exchange(profileInfoUrl, HttpMethod.GET, entity, String.class);
		handleImportJobs(null);
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping("/companiesIndeed")
	public HttpEntity<String> getCompaniesByIndeed() {
		final var url = "https://api.indeed.com/ads/apisearch?publisher=123412341234123&q=java+developer&l=austin%2C+tx&sort=&radius=&st=&jt=&start=&limit=&fromage=&filter=&latlong=1&co=us&chnl=&userip=1.2.3.4&useragent=Mozilla/%2F4.0%28Firefox%29&v=2";
		final var restTemplate = new RestTemplate();
		final var response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		
		return new HttpEntity<String>(response.getBody());
	}
	
	@PostMapping(value = "/createCompany")
	public ResponseEntity<Object> createCompany(@RequestBody final Company company) {
		final var companies = new ArrayList<Company>();
		companies.add(company);		
		return new ResponseEntity<>("company is created successfully", HttpStatus.CREATED);
	}
		
	private String getAccesToken(final String code) {
		var requestBuilder = new StringBuilder();
		requestBuilder.append("https://www.linkedin.com/oauth/v2/accessToken?grant_type=authorization_code")
						.append("&code=" + code)
						.append("&redirect_uri=" + getRedirecUrl())
						.append("&client_id=" + getClientId())
						.append("&client_secret=" + getClientSecret());
		
		final var restTemplate = new RestTemplate();
		final var accesTokenRequest = restTemplate.getForObject(requestBuilder.toString(), String.class);
		final var accesTokenJsonObject = new JSONObject(accesTokenRequest);
		final var accesToken = accesTokenJsonObject.get("access_token").toString();
		
		return accesToken;
	}
	
	private void handleImportJobs(JSONArray json) {
		 final var jsonArray = new JSONArray();

		 final var job1 = new JSONObject();
		 final var job2 = new JSONObject();
		 
		 job1.put("job", "java");
		 job2.put("job", ".net");

		 jsonArray.put(job1);
		 jsonArray.put(job2);
		 
		 for (int i=0; i<jsonArray.length(); i++) {
			 var obj = jsonArray.getJSONObject(i);
			 System.out.println(obj);
		 }
		 
	}
		 
	
}
