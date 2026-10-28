package com.msd.myChatApp.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("rooms")
public class Room {
    @Id
    private String id;
    private String roomId;
    private String ownerUsername;
    private Set<String> members = new HashSet<>();
    private List<Message> messages = new ArrayList<>();
}
