package com.invoices.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * @author psoutzis
 * This class represents the Client entity. A client can have 0 - many companies, but
 * a client should always have at least one corresponding portfolio.
 */

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @Column(name = "client_name")
    private String clientName;
}
