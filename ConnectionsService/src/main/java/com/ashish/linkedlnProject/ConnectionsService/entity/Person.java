package com.ashish.linkedlnProject.ConnectionsService.entity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Node
@Data
@Builder
public class Person {

    @Id
    private String id = UUID.randomUUID().toString();
    private Long userId;
    private String name;

}
