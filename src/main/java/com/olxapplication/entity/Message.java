package com.olxapplication.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages_table")
public class Message {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy =  "uuid2")
    private String id;

    @Column(name = "msg", nullable = false)
    private String msg;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name="date", nullable = false)
    private LocalDateTime date;

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", msg='" + msg + '\'' +
                ", sender=" + sender.getId() +
                ", receiver=" + receiver.getId() +
                ", date=" + date +
                '}';
    }
}
