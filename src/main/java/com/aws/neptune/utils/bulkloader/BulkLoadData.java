package com.aws.neptune.utils.bulkloader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class BulkLoadData {

	
	public HttpResponse sendLoadDataRequest(String neptuneEndpoint, String iamRoleArn) throws Exception
	{
		String payload = 
				"{\"source\": \"s3://neptune-s3/twitterlikeapp/\", " +
                "\"format\": \"csv\", " +
                "\"iamRoleArn\": \"arn:aws:iam::213930781331:role/s3-from-neptune-2\"," +
                "\"region\": \"us-east-1\", " +
                "\"failOnError\": \"FALSE\" " +
                "}";
		System.out.println("Payload => \n"+payload);
		
        StringEntity entity = new StringEntity(payload,
        		ContentType.create("application/json"));

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://neptune360.cluajh6rcbti.us-east-1.neptune.amazonaws.com:8182/loader");
        request.setHeader("Content-Type", "application/json");
        request.setEntity(entity);

        System.out.println("Request => \n"+request);
        
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.getStatusLine().getStatusCode());
        return response;
        
	}
	
	 public static void main(String[] args) throws Exception {
	        if (null == args || args.length < 2) {
	            System.err.println("Usage: BulkLoadData <neptune-endpoint> <iam-s3-role-arn>");
	            System.exit(1);
	        }
	        
	        
	        HttpResponse response = new BulkLoadData().sendLoadDataRequest("http://neptune360.cluajh6rcbti.us-east-1.neptune.amazonaws.com",  "test");
	        
	        System.out.println(response);
	        
	 }
	 
}
