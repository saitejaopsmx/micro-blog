# Micro-Blogging API

A simple micro-blogging application that uses AWS S3 for data storage.

## Prerequisites

*   Java 17
*   Maven
*   AWS CLI
*   Configured AWS credentials

## Configuration

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

You can run the application using the following command:

```bash
./mvnw spring-boot:run
```

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
*   **Roles**: `MODERATOR`
*   **cURL**:

```bash
curl -u moderator1:password -X DELETE http://localhost:8080/posts/c2a7b2a0-8b1a-4b1a-9b1a-0a0b0c0d0e0f
```
