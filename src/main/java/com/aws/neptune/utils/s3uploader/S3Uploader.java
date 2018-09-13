package com.aws.neptune.utils.s3uploader;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.S3ClientOptions;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;


public class S3Uploader {

	
	public void upload(File data, String bucketName, String key) throws Exception
	{
		//AWSCredentials ac =  new BasicAWSCredentials("", "");
		AmazonS3 s3 = new AmazonS3Client();

        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
      
		try
		{
        s3.setRegion(usEast1);
		s3.putObject(new PutObjectRequest(bucketName, key, data));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
