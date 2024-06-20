package com.packages.scompass;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Post {
    private String imageUrl;
    private String profileImageUrl;
    private String postName;
    private String caption;
    private String username;
    private int likesCount; // Corrected method name
    private int commentsCount;
    private String postId;
    private List<Comment> comments;
    private List<String> likedByUserIds;

    public Post() {
        // Initialize any default values here
        this.comments = new ArrayList<>();
        this.likedByUserIds = new ArrayList<>();
    }

    public Post(String imageUrl, String profileImageUrl, String postName, String caption, String username, int likesCount, int commentsCount, String postId) { // Corrected method name
        this.profileImageUrl = profileImageUrl;
        this.imageUrl = imageUrl;
        this.postName = postName;
        this.caption = caption;
        this.username = username;
        this.likesCount = likesCount; // Corrected method name
        this.commentsCount = commentsCount;
        this.postId = postId;
        this.comments = new ArrayList<>();
        this.likedByUserIds = new ArrayList<>();
    }

    // Getter methods
    public String getImageUrl() {
        return imageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getPostName() {
        return postName;
    }

    public String getCaption() {
        return caption;
    }

    public String getUsername() {
        return username;
    }

    public int getLikesCount() { // Corrected method name
        return likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public String getPostId() {
        return postId;
    }

    public void addComment(Comment comment) {
        commentsCount++;
        // Add the comment to the list of comments
        comments.add(comment);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addLike(String currentUserId) {
        likedByUserIds.add(currentUserId);
        likesCount++;
    }

    public boolean isLikedByCurrentUser(String currentUserId) {
        return likedByUserIds.contains(currentUserId);
    }

    public static class Comment {
        private String username;
        private String text;

        public Comment(String username, String text) {
            this.username = username;
            this.text = text;
        }

        public String getUsername() {
            return username;
        }

        public String getText() {
            return text;
        }
    }
}
