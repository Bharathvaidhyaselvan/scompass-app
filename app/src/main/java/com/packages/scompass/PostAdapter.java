package com.packages.scompass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private final Context context;
    private final List<Post> posts;
    private final String currentUserId;
    private String userName;

    public PostAdapter(Context context, List<Post> posts, String currentUserId, String userName) {
        this.context = context;
        this.posts = posts;
        this.currentUserId = currentUserId;
        this.userName = userName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);

        // Update the post caption with custom formatted hyperlinks
        updatePostCaption(holder.captionTextView, post.getCaption());

        // Set the username to the usernameTextView
        holder.usernameTextView.setText(post.getUsername());

        holder.likesCountTextView.setText(String.valueOf(post.getLikesCount()));
        holder.commentsCountTextView.setText(String.valueOf(post.getCommentsCount()));

        // Load the post image using Glide
        Glide.with(context)
                .load(post.getImageUrl())
                .into(holder.postImageView);

        // Show the profile image if it's not "-1"
        if (!post.getProfileImageUrl().equals("-1")) {
            Glide.with(context)
                    .load(post.getProfileImageUrl())
                    .into(holder.postProfileImageView);
        }

        // Check if the current user has already liked the post
        if (post.isLikedByCurrentUser(currentUserId)) {
            // Change the image of the like button to 'Liked Image'
            holder.likeButton.setEnabled(false);
        } else {
            // Change the image of the like button to 'Like Image'
            holder.likeButton.setEnabled(true);
        }

        // Handle like button click
        holder.likeButton.setOnClickListener(v -> {
            // Update the post's likes count and user's likedPosts list
            post.addLike(currentUserId);

            // Check if the post has a valid ID
            if (post.getPostId() != null && !post.getPostId().isEmpty()) {
                // Update the post in Firestore
                FirebaseFirestore.getInstance()
                        .collection("posts")
                        .document(post.getPostId())
                        .set(post, SetOptions.merge())
                        .addOnSuccessListener(aVoid -> {
                            // Change the image of the like button to 'Liked Image'
                            holder.likeButton.setEnabled(false);
                            holder.likesCountTextView.setText(String.valueOf(post.getLikesCount()));
                            Toast.makeText(context, "You liked the post!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Change the image of the like button to 'Like Image'
                            holder.likeButton.setEnabled(true);
                            Toast.makeText(context, "Failed to like the post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Handle the case where the post does not have a valid ID
                Toast.makeText(context, "Invalid post ID, cannot update like status.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView postImageView;
        ImageView postProfileImageView;
        TextView usernameTextView;
        TextView captionTextView;
        TextView likesCountTextView;
        TextView commentsCountTextView;
        ImageView likeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImageView = itemView.findViewById(R.id.post_image);
            postProfileImageView = itemView.findViewById(R.id.profile_post_image);
            usernameTextView = itemView.findViewById(R.id.post_username);
            captionTextView = itemView.findViewById(R.id.post_caption);
            likesCountTextView = itemView.findViewById(R.id.post_likes);
            commentsCountTextView = itemView.findViewById(R.id.post_comments);
            likeButton = itemView.findViewById(R.id.like_button);
        }
    }

    public void updatePostCaption(TextView postCaptionTextView, String caption) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(caption);

        // Define the patterns for different URLs
        Pattern youtubePattern = Pattern.compile("https://youtu\\.be/([\\w-]+).*");
        Pattern youtubeShortsPattern = Pattern.compile("https://youtube\\.com/shorts/([\\w-]+).*");
        Pattern youtubeLivePattern = Pattern.compile("https://www\\.youtube\\.com/live/([\\w-]+).*");
        Pattern youtubeChannelPattern = Pattern.compile("https://www\\.youtube\\.com/channel/([\\w-]+).*");

        // Apply the custom formatting and make links clickable
        applyCustomLink(spannableString, youtubePattern, "@youtube/video/");
        applyCustomLink(spannableString, youtubeShortsPattern, "@youtube/shorts/");
        applyCustomLink(spannableString, youtubeLivePattern, "@youtube/live/");
        applyCustomLink(spannableString, youtubeChannelPattern, "@youtube/channel/");

        postCaptionTextView.setText(spannableString);
        postCaptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void applyCustomLink(SpannableStringBuilder spannableString, Pattern pattern, String replacement) {
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String group = matcher.group(1);
            String customText = replacement + group;

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    // Handle link click if needed
                }
            };
            spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.replace(start, end, customText);
        }
    }

    // Add the setPosts method to set the posts in the adapter
    @SuppressLint("NotifyDataSetChanged")
    public void setPosts(List<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }
}
