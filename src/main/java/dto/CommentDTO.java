package dto;

import entities.JSONPlaceholderComment;

/**
 *
 * @author Henning
 */
public class CommentDTO {

    private int postId;
    private String name;
    private String email;
    private String body;

    public CommentDTO(JSONPlaceholderComment comment) {
        this.postId = comment.getPostId();
        this.name = comment.getName();
        this.email = comment.getEmail();
        this.body = comment.getBody();
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
