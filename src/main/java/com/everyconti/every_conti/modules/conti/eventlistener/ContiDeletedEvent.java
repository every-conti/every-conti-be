package com.everyconti.every_conti.modules.conti.eventlistener;

import lombok.Data;

@Data
public class ContiDeletedEvent {
    private Long id;
    private String title;

}
