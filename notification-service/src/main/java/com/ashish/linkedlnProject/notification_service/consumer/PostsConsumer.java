package com.ashish.linkedlnProject.notification_service.consumer;

import com.ashish.linkedlnProject.notification_service.PostsService.event.PostCreated;
import com.ashish.linkedlnProject.notification_service.PostsService.event.PostLiked;
import com.ashish.linkedlnProject.notification_service.entity.Notification;
import com.ashish.linkedlnProject.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsConsumer {

    private final NotificationService notificationService;


    @KafkaListener(topics = "post_created_topic")
    public void handlePostCreated(PostCreated postCreated) {
        log.info("handlePostCreated:", postCreated);
        String message = String.format("your connection with id %d has created this post %s",postCreated.getOwnerUserId(),postCreated.getContent());
        Notification notification =Notification.builder()
                .message(message)
                .userId(postCreated.getUserId())
                .build();
        notificationService.addNotification(notification);

    }
    @KafkaListener(topics = "post_liked_topic")
    public void handlePostLiked(PostLiked postLiked) {
        log.info("handlePostLiked :", postLiked);

        String message = String.format("your connection with id %d has liked this post %s",postLiked.getLikedByUserId(),postLiked.getPostId());

        Notification notification= Notification.builder()
                .message(message)
                .userId(postLiked.getOwnerUserId())
                .build();
        notificationService.addNotification(notification);
    }

}
