[![Build Status](https://travis-ci.org/IBM/janusgraph-utils.svg?branch=master)](https://travis-ci.org/IBM/janusgraph-utils)

# Develop a graph database app using Amazon Neptune

This gitHub lab contains sample data and code for running a Twitter-like application in Amazon Neptune. The utility code illustrates how to use Amazon Neptune APIs, ingest data, and query graph. Developers can use or modify the code to build and operate their custom graph applications, or create similar java and groovy files to interact with Amazon Neptune.

When the reader has completed this Code Pattern, they will understand how to:
* Generate a synthetic twitter-like graph dataset
* Upload graph dataset to Amazon S3
* Use Amazon Neptune Bulk Loader to import graph data in csv files into Amazon Neptune graph database
* Query and update graph data using Apache Gremlin Console and REST API

![](doc/source/images/architecture.png)

## Prerequisites

- Provision Amazon Neptune Cluster (single node)
- Create Amazon S3 bucket 
- Provision an Amazon EC2 instance with Instance Profile that allows to read/write to Amazon S3
- Install and configure Java and Maven on the above EC2 instance. For more details please visit https://docs.aws.amazon.com/neptune/latest/userguide/access-graph-gremlin-java.html. Follow previous doc link upto step#4.
- Install Apache Tinkerpop Gremlin client and configure the connectivity to Amazon Neptune as described here - https://docs.aws.amazon.com/neptune/latest/userguide/access-graph-gremlin-console.html

## Flow

1. The user downloads neptune-utils from this gitHub repo
2. The user generates Twitter sample data using Neptune java utility in this repo (graph data would be available in Amazon S3)
3. The user loads this data into Amazon Neptune using Amazon Neptune Bulk Loader utility
4. The user makes search queries and update the graph using Apache Tinkerpop Gremlin client

## Features
* This is fully extensible code wherein developers can change the number/type of vertices and edges by modifying the config JSON files
* Sample large, medium and tiny config files are provided to test large (upto millions of vertices and edges), medium and small datasets.


# Steps
## Run locally
1. [Install prerequisites](#1-install-prerequisites)
2. [Clone the repo](#2-clone-the-repo)
3. [Generate the graph sample](#3-generate-the-graph-sample)
4. [Load graph data into Amazon Neptune](#4-load-graph-data-into-amazon-neptune)
5. [Run interactive remote queries](#5-run-interactive-remote-queries)

### 1. Install prerequisites
Please install and configure all the softwares as mentioned in the prerequisites section above.
Note that Amazon Neptune is only accessible within a VPC. If you want to access Amazon Neptune instance from outside VPC consider implementing a reverse proxy solution in front of Amazon Netune.


### 2. Clone the repo

Clone the `neptune-utils` on the EC2 instance and run `mvn package`.

```
git clone https://github.com/EjazSayyed/neptune-utils.git
cd neptune-utils/
mvn package
```

### 3. Generate the graph sample

Run the command in `neptune-utils` folder to generate data into `/tmp` folder on a local filesystem and then upload it to the Amazon S3 bucket you would specify as an argument.
```
./run.sh gencsv csv-conf/twitter-like-w-date.json <s3-bucket> <bucket-folder>
```

### 4. Load graph data into Amazon Neptune

Amazon Neptune provides a process for loading data from external files directly into a Neptune DB instance. You can use this process instead of executing a large number of INSERT statements, addVertex and addEdge steps, or other API calls.
This utility is called the Neptune Loader. For more information on Loader command please refer - https://docs.aws.amazon.com/neptune/latest/userguide/bulk-load.html

Below is the sample `curl` command to load data into Amazon Neptune using Neptune Loader.
```
curl -X POST -H 'Content-Type: application/json' http://<amazon-neptune-cluster-endpoint>:8182/loader -d '
{
"source" : "s3://bucket-name/bucket-folder/",
"format" : "csv",
"iamRoleArn" : "arn:aws:iam::<account-number>:role/<role-name>",
"region" : "us-east-1",
"failOnError" : "FALSE"
}'
```

Alternatively, just run below command in `neptune-utils` folder.
```
./run.sh import <s3-bucket-name> <iam-role-arn> 
```

### 5. Run interactive remote queries

Now it is time to run few sample queries on the data set that we have just loaded into Amazon Neptune.

Once you install and configure the Apache Tinkerpop Gremlin Console and are able to connect to your Neptune cluster as mentioned @ https://docs.aws.amazon.com/neptune/latest/userguide/access-graph-gremlin-console.html, follow the below steps to run the sample queries.

```
[ec2-user@ip-172-31-59-189 bin]$ pwd
/home/ec2-user/apache-tinkerpop-gremlin-console-3.3.2/bin
[ec2-user@ip-172-31-59-189 bin]$ ./gremlin.sh 

         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----
plugin activated: tinkerpop.server
plugin activated: tinkerpop.utilities
plugin activated: tinkerpop.tinkergraph
gremlin> :remote connect tinkerpop.server conf/neptune-remote.yaml
==>[neptune-cluster-endpoint]/172.31.23.188:8182
gremlin> :remote console
==>All scripts will now be sent to Gremlin Server - [neptune-cluster-endpoint]/172.31.23.188:8182] - type ':remote console' to return to local mode
gremlin> 
```
Now, let us find a sample user from `User` vertices and use it for running few queries.

```
gremlin> g.V().has('User','~id','1').valueMap()
==>{name=[Brenden Johnson]}
```
We shall use the above name to run few queries here -

#1 Who follows ‘Brenden Johnson’?
```
gremlin> g.V().has('name', 'Brenden Johnson').in('Follows').values('name')
==>Jameson Kreiger
==>Yasmeen Casper
==>Maverick Altenwerth
==>Isabel Gibson
...
```

#2 Find Brenden Johnson' followers who retweeted his tweets
```
gremlin> g.V().has('name', 'Brenden Johnson').in('Follows').as('a').out('Retweets').in('Tweets').has('name', 'Brenden Johnson').select('a').values('name').dedup()
==>Quentin Watsica
==>Miss Vivianne Gleichner
==>Mr. Janet Ratke
...
```

You can find sample search and insert queries in [samples/twitter-like-queries.txt](samples/twitter-like-queries.txt).

# Sample output

![Sample output for "Find Indiana Jones' tweets that his followers retweeted"](doc/source/images/sample_output.png)

# Links
* [Amazon Neptune Samples](https://github.com/aws-samples/amazon-neptune-samples): Sample data sets and code examples for Amazon Neptune.
* [Amazon Neptune Tools](https://github.com/awslabs/amazon-neptune-tools): Tools and utilities related to Amazon Neptune.

# Learn more

* [Deep dive on Amazon Neptune](https://www.youtube.com/watch?v=6o1Ezf6NZ_E)
* [Amazon Neptune FAQs](https://aws.amazon.com/neptune/faqs/)

# License
[Apache 2.0](LICENSE)
