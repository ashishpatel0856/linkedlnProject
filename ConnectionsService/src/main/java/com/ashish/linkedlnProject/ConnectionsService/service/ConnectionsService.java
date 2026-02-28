package com.ashish.linkedlnProject.ConnectionsService.service;

import com.ashish.linkedlnProject.ConnectionsService.auth.AuthContextHolder;
import com.ashish.linkedlnProject.ConnectionsService.entity.Person;
import com.ashish.linkedlnProject.ConnectionsService.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionsService {

    private final PersonRepository personRepository;
    public static final Logger log = LoggerFactory.getLogger(ConnectionsService.class);
    public ConnectionsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    public List<Person> getFirstDegreeConnectionsOfUser(Long userId){
        log.info("getting first degree connections of user with id:{}",userId);
        return personRepository.getFirstDegreeConnectionsOfUser(userId);

    }

    public void sendConnectionRequest(Long receiverId){
        Long senderId = AuthContextHolder.getCurrentUser();
        log.info("sending connection request to sender with id:{}",senderId,receiverId);
        if(senderId.equals(receiverId)){
            throw new RuntimeException("both sender and receiver are equal");
        }
        boolean alreadySentRequest = personRepository.connectionRequestExits(senderId,receiverId);
        if(alreadySentRequest){
            throw new RuntimeException("connection request already exits, can not send again");
        }
        boolean alreadyConnected= personRepository.alreadyConnected(senderId,receiverId);
        if(alreadyConnected){
            throw new RuntimeException("already connected users, can not add connection request");
        }
        log.info("successfully sent the connection request to sender and receiver with id:{}",senderId);
        personRepository.addConnectionRequest(senderId, receiverId);
    }


    public void acceptConnectionRequest(Long senderId){
        Long receiverId = AuthContextHolder.getCurrentUser();
        log.info("accepting connection request to sender  with id:{}",senderId,receiverId);
        if(senderId.equals(receiverId)){
            throw new RuntimeException("both sender and receiver are equal");
        }
        boolean alreadyConnected= personRepository.alreadyConnected(senderId,receiverId);
        if(alreadyConnected){
            throw new RuntimeException("already connected users, can not accept connection request again");
        }
        boolean alreadySentRequest = personRepository.connectionRequestExits(senderId,receiverId);
        if(alreadySentRequest){
            throw new RuntimeException("no connection request already exits, can not accept without request");
        }
        personRepository.acceptConnectionRequest(senderId,receiverId);
        log.info("successfully accepted connection request to sender and receiver with id:{}",senderId);
    }




    public void rejectConnectionRequest(Long senderId) {
        Long receiverId = AuthContextHolder.getCurrentUser();
        log.info("rejecting a connection request to sender  with id:{}", senderId, receiverId);
        if (senderId.equals(receiverId)) {
            throw new RuntimeException("both sender and receiver are equal");
        }
        boolean alreadySentRequest = personRepository.connectionRequestExits(senderId, receiverId);
        if (alreadySentRequest) {
            throw new RuntimeException("no connection request  exits, can not reject it");
        }

        personRepository.rejectConnectionRequest(senderId, receiverId);
        log.info("successfully reject connection request to sender and receiver with id:{}", senderId);
    }
}
