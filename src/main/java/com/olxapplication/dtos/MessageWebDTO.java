package com.olxapplication.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageWebDTO {
    String id;
    String msg;
    String sender;
    String receiver;
}
