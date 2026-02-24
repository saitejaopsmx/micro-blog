# Micro-Blogging API

A simple micro-blogging application that uses AWS S3 for data storage.

## Prerequisites

*   Java 17
*   Maven
*   AWS CLI
*   Docker
*   Configured AWS credentials

## Configuration

### Storage

You can configure the application to use either `local` file storage or `s3` for storing posts. This can be configured in the `src/main/resources/application.properties` file:

```properties
storage.type=local
```

If you use `s3`, you will also need to configure your AWS credentials.

### AWS Credentials

The application requires AWS credentials to be configured in your environment. The application uses the default credential chain to authenticate with AWS. You can configure credentials in the following ways:

*   **Environment Variables**: Set the `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` environment variables.
*   **AWS credentials file**: Create a file at `~/.aws/credentials` with the following format:

```
[default]
aws_access_key_id = YOUR_ACCESS_KEY
aws_secret_access_key = YOUR_SECRET_KEY
```

*   **EC2 Instance Profile**: If the application is running on an EC2 instance, it will automatically use the IAM role assigned to the instance.

### Application Properties

You can also configure the AWS region and S3 bucket name in the `src/main/resources/application.properties` file:

```properties
aws.region=us-west-2
aws.s3.bucket-name=micro-blogging
```

## How to Run

### Locally

You can run the application using the following command:

```bash
./mvnw spring-boot:run
```

### With Docker

#### Simple Docker build

1.  Build the application:

```bash
./mvnw package
```

2.  Build the Docker image:

```bash
docker build -t micro-blog .
```

3.  Run the Docker container:

```bash
docker run -p 8080:8080 -e AWS_ACCESS_KEY_ID=<YOUR_ACCESS_KEY> -e AWS_SECRET_ACCESS_KEY=<YOUR_SECRET_KEY> micro-blog
```

#### Multi-stage Docker build

1.  Build the Docker image using the multi-stage Dockerfile:

```bash
docker build -f Dockerfile.multistage -t micro-blog-multistage .
```

2.  Run the Docker container:

```bash
docker run -p 8080:8080 -e AWS_ACCESS_KEY_ID=<YOUR_ACCESS_KEY> -e AWS_SECRET_ACCESS_KEY=<YOUR_SECRET_KEY> micro-blog-multistage
```

## API Documentation

The API documentation is available through Swagger UI at the following URL:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Authentication

The application uses basic authentication. The following users are available:

| Username   | Password | Roles             |
|------------|----------|-------------------|
| user1      | password | USER              |
| user2      | password | USER              |
| user3      | password | USER              |
| moderator1 | password | MODERATOR         |
| moderator2 | password | MODERATOR, USER   |


## API Endpoints

### Create a Post

*   **Method**: `POST`
*   **URL**: `/posts`
*   **Roles**: `USER`, `MODERATOR`
*   **Request Body**:

```json
{
    "title": "My First Post",
    "content": "This is the content of my first post."
}
```

*   **Response**:

```json
{
    "id": "c2a7b2a0-8b1a-4b1a-9b1a-0a0b0c0d0e0f",
    "title": "My First Post",
    "content": "This is the content of my first post."
}
```

*   **cURL**:

```bash
curl -u user1:password -X POST -H "Content-Type: application/json" -d '{"title":"My First Post","content":"This is the content of my first post."}' http://localhost:8080/posts
```

### Get a Post

*   **Method**: `GET`
*   **URL**: `/posts/{id}`
*   **Roles**: `permitAll`
*   **Response**:

```json
{
    "id": "c2a7b2a0-8b1a-4b1a-9b1a-0a0b0c0d0e0f",
    "title": "My First Post",
    "content": "This is the content of my first post."
}
```

*   **cURL**:

```bash
curl http://localhost:8080/posts/c2a7b2a0-8b1a-4b1a-9b1a-0a0b0c0d0e0f
```

### Get All Posts

*   **Method**: `GET`
*   **URL**: `/posts`
*   **Roles**: `permitAll`
*   **Response**:

```json
[
    {
        "id": "c2a7b2a0-8b1a-4b1a-9b1a-0a0b0c0d0e0f",
        "title": "My First Post",
        "content": "This is the content of my first post."
    },
    {
        "id": "d3b8c3b1-9c2b-5c2b-ac2b-1b1c1d1e1f10",
        "title": "My Second Post",
        "content": "This is the content of my second post."
    }
]
```

*   **cURL**:

```bash
curl http://localhost:8080/posts
```

### Delete a Post

*   **Method**: `DELETE`
*   **URL**: `/posts/{id}`
*   **Roles**: `MODERATOR` (Can delete any user's post)
*   **cURL**:

```bash
curl -u moderator1:password -X DELETE http://localhost:8080/posts/c2a7b2a0-8b1a-4b1a-9b1a-0a0b0c0d0e0f
```

### Get Post URLs by Title

*   **Method**: `GET`
*   **URL**: `/posts/urls?keyword={keyword}`
*   **Roles**: `permitAll`
*   **Response**:

```json
[
    "https://s3.us-west-2.amazonaws.com/micro-blogging/c2a7b2a0-8b1a-4b1a-9b1a-0a0b0c0d0e0f?...",
    "https://s3.us-west-2.amazonaws.com/micro-blogging/d3b8c3b1-9c2b-5c2b-ac2b-1b1c1d1e1f10?..."
]
```

*   **cURL**:

```bash
curl "http://localhost:8080/posts/urls?keyword=First"
```

### Get URL Content

*   **Method**: `GET`
*   **URL**: `/url?url={url}`
*   **Roles**: Authenticated Users
*   **Response**: The content of the provided URL.
*   **cURL**:

```bash
curl -u user1:password "http://localhost:8080/url?url=https://www.google.com"
```
