package com.kie;

//KIE WIH imports
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

//jbpm imports
import org.jbpm.process.workitem.core.AbstractLogOrThrowWorkItemHandler;

//Logs imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TLS and REST imports
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;




public class CustomRESTWIH extends AbstractLogOrThrowWorkItemHandler implements WorkItemHandler {
	
    //Logger (entiendo que es para imprimir los logs).
    private static final Logger logger = LoggerFactory.getLogger(CustomRESTWIH.class);
	
	//Variables REST WIH
    //public static final String PARAM_AUTH_TYPE = "AuthType";
    //public static final String PARAM_CONNECT_TIMEOUT = "ConnectTimeout";
    //public static final String PARAM_READ_TIMEOUT = "ReadTimeout";
    //public static final String PARAM_CONTENT_TYPE = "ContentType";
    //public static final String PARAM_CONTENT_TYPE_CHARSET = "ContentTypeCharset";
    public static final String PARAM_HEADERS = "Headers";
    //public static final String PARAM_CONTENT = "Content";
    //public static final String PARAM_CONTENT_DATA = "ContentData";
    //public static final String PARAM_USERNAME = "Username";
    //public static final String PARAM_PASSWORD = "Password";
    //public static final String PARAM_AUTHURL = "AuthUrl";
    public static final String PARAM_RESULT = "Result";
    //public static final String PARAM_STATUS = "Status";
    //public static final String PARAM_STATUS_MSG = "StatusMsg";
    //public static final String PARAM_COOKIE = "Cookie";
    //public static final String PARAM_COOKIE_PATH = "CookiePath"
    
	//Variables para TLS
    /*
	private static final String KEYSTOREPATH = "C:\\Users\\usuario\\jbpm-server-7.73.0.Final-dist\\standalone\\configuration\\wildfly-ts.jks"; // or .p12
    private static final String KEYSTOREPASS = "josrodmor11";
    private static final String KEYPASS = "josrodmor11";
    
    KeyStore readStore() throws Exception {
        try (InputStream keyStoreStream = this.getClass().getResourceAsStream(KEYSTOREPATH)) {
            KeyStore keyStore = KeyStore.getInstance("JKS"); // or "PKCS12"
            keyStore.load(keyStoreStream, KEYSTOREPASS.toCharArray());
            return keyStore;
        }
    }
    
    
    
    public void readKeyStore() throws Exception {
        assertNotNull(readStore());
    }
    */
    private static HttpClient getHttpClient() {

	    try {
	        SSLContext sslContext = SSLContext.getInstance("SSL");

	        sslContext.init(null,
	                new TrustManager[]{new X509TrustManager() {
	                    public X509Certificate[] getAcceptedIssuers() {

	                        return null;
	                    }

	                    public void checkClientTrusted(
	                            X509Certificate[] certs, String authType) {

	                    }

	                    public void checkServerTrusted(
	                            X509Certificate[] certs, String authType) {

	                    }
	                }}, new SecureRandom());
	        

	        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	      
			
	        HttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(socketFactory).build();
	        
	       
	        
	        
	        return httpClient;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return HttpClientBuilder.create().build();
	    }
	}
    
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) { //Lo que pasará cuando se ejecute bien el WIH
		
		String URL = (String) workItem.getParameter("Url");
		String method = (String) workItem.getParameter("Method");
		//String headers = (String) workItem.getParameter(PARAM_HEADERS);
		String JSONStringResponse = (String) workItem.getParameter(PARAM_RESULT);
		String JSONStringtoSend = (String) workItem.getParameter("Entity");
		
	
		HttpClient httpclient = getHttpClient();
		
		if(method.equals(Methods.GET.getMethod())) {
			
			try {
				
				final String auth = "guardiansUser" + ":" + "dqFtNDsavFuQT4fjHBqj";
				final String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.ISO_8859_1));
				final String authHeader = "Basic " + new String(encodedAuth);
				
				HttpGet httpGet = new HttpGet(URL);
				httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
				
				
				HttpResponse response = httpclient.execute(httpGet);
				
				
				
				HttpEntity entity = response.getEntity();
		        JSONStringResponse = EntityUtils.toString(entity);
		        logger.info("Consulta realizada");
		        logger.debug(JSONStringResponse);
		        
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			   Map<String,Object> map = new HashMap<String,Object>();
			   map.put("Result", JSONStringResponse);
		       manager.completeWorkItem(workItem.getId(), map); 
		       logger.info("WorkItem completado");
		}
		
		else if(method.equals(Methods.PUT.getMethod())) {
			
			int codigoEstadoREST = 0;
			
			try {
				
				final String auth = "guardiansUser" + ":" + "dqFtNDsavFuQT4fjHBqj";
				final String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.ISO_8859_1));
				final String authHeader = "Basic " + new String(encodedAuth);
				
				
				HttpPut httpPut = new HttpPut(URL);
				httpPut.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
				
				StringEntity stringEntity = new StringEntity(JSONStringtoSend);
				httpPut.setEntity(stringEntity);
				HttpResponse response = httpclient.execute(httpPut);
				
				if(response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300)
				{
					logger.info("Put realizado");
					codigoEstadoREST = response.getStatusLine().getStatusCode();					
					
					HttpEntity entity = response.getEntity();
			        JSONStringResponse = EntityUtils.toString(entity);
			        logger.debug(JSONStringResponse);
				}
				     
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			   Map<String,Object> map = new HashMap<String,Object>();
			   map.put("Result", codigoEstadoREST);
		       manager.completeWorkItem(workItem.getId(), map); 
		       logger.info("WorkItem completado");			
			
			
		}
		
		else if(method.equals(Methods.POST.getMethod())) {
			
			int codigoEstadoREST = 0;
			
			try {
				
				final String auth = "guardiansUser" + ":" + "dqFtNDsavFuQT4fjHBqj";
				final String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.ISO_8859_1));
				final String authHeader = "Basic " + new String(encodedAuth);
				
				
				
				HttpPost httpPost = new HttpPost(URL);
				httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
				
				StringEntity stringEntity = new StringEntity(JSONStringtoSend);
				httpPost.setEntity(stringEntity);
				HttpResponse response = httpclient.execute(httpPost);
				
				if(response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300)
				{
					logger.info("Post realizado");
					codigoEstadoREST = response.getStatusLine().getStatusCode();
					
					HttpEntity entity = response.getEntity();
			        JSONStringResponse = EntityUtils.toString(entity);
			        logger.debug(JSONStringResponse);
				}
				     
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			   Map<String,Object> map = new HashMap<String,Object>();
			   map.put("Result", codigoEstadoREST);
		       manager.completeWorkItem(workItem.getId(), map); 
		       logger.info("WorkItem completado");			
			
			
		}
		
		else if(method.equals(Methods.DELETE.getMethod())) {
			
			int codigoEstadoREST = 0;
			
			try {
				
				final String auth = "guardiansUser" + ":" + "dqFtNDsavFuQT4fjHBqj";
				final String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.ISO_8859_1));
				final String authHeader = "Basic " + new String(encodedAuth);
				
				HttpDelete httpDelete = new HttpDelete(URL);
				
				
				httpDelete.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
				
			
				HttpResponse response = httpclient.execute(httpDelete);
				
				if(response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300)
				{
					codigoEstadoREST = response.getStatusLine().getStatusCode();
					logger.info("Delete realizado");
				}
				     
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			   Map<String,Object> map = new HashMap<String,Object>();
			   map.put("Result", codigoEstadoREST);
		       manager.completeWorkItem(workItem.getId(), map); 
		       logger.info("WorkItem completado");			
			
		}
		else {
			logger.error("Introduce un Metodo HTTP adedcuado");
		}
		
		

	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// TODO Auto-generated method stub
		
	}
	
	public enum Methods 
	{
	    GET("GET"), 
	    PUT("PUT"), 
	    POST("POST"), 
	    DELETE("DELETE");
	 
	    private String method;
	 
	    Methods(String method) {
	        this.method = method;
	    }
	 
	    public String getMethod() {
	        return method;
	    }
	}
	

}




//COSAS ANTIGUAS
//String url ="https://restguardians.westeurope.cloudapp.azure.com:8080/guardians/api/doctors";

//SSLContext sslContext;
//try {
//	sslContext = SSLContexts.custom()
//		    .loadTrustMaterial(new TrustSelfSignedStrategy())
//		    .build();
//	SSLConnectionSocketFactory socketFactory =
//		    new SSLConnectionSocketFactory(sslContext);
//		Registry<ConnectionSocketFactory> reg =
//		    RegistryBuilder.<ConnectionSocketFactory>create()
//		    .register("https", socketFactory)
//		    .build();
//		logger.error("Ssl context construido");
//		HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg);        
//		CloseableHttpClient httpClient = HttpClients.custom()
//		    .setConnectionManager(cm)
//		    .build();
//		HttpGet httpGet = new HttpGet(url);
//		CloseableHttpResponse sslResponse = httpClient.execute(httpGet);
//		logger.error("Consulta fallida");
//		
//		 Map<String,Object> map = new HashMap<String,Object>();
//        map.put("Result", sslResponse.getEntity().toString());
//        manager.completeWorkItem(workItem.getId(), map); 
//        System.out.println("WorkItem completado");
//		
//		
//} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//} catch (ClientProtocolException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//} catch (IOException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
